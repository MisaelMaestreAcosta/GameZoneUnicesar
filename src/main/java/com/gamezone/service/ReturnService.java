package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.model.SalesLineItem;
import com.gamezone.persistence.ReturnRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Provides business operations for managing returns: registration with
 * deadline and ownership validation, stock restoration, queries, and
 * monthly balance reporting.
 * <p>
 * Sale does not currently expose a {@code canBeReturned()} method, so the
 * 30-day eligibility check is performed here directly from
 * {@code sale.getDate()}. If that method is added to Sale later, this
 * check can be replaced with a single call to it.
 * <p>
 * Note: a returned product is currently tracked as one unit per entry in
 * the product id list (matching Return's {@code List<Product>} design).
 * If a customer needs to return more than one unit of the same product
 * from a single sale line, pass that product's id multiple times.
 */
public class ReturnService {

    private static final int RETURN_WINDOW_DAYS = 30;

    private final ReturnRepository returnRepository;
    private final SaleService saleService;
    private final ProductService productService;
    private final PersonService personService;
    private final List<Return> returns;

    /**
     * Creates the service and loads existing returns from persistence.
     *
     * @param returnRepository the repository used to persist and load returns
     * @param saleService      used to look up the original sale
     * @param productService   used to restore stock of returned products
     * @param personService    used to obtain customers and sellers needed to
     *                         reconstruct sales through SaleService
     */
    public ReturnService(ReturnRepository returnRepository, SaleService saleService, ProductService productService,
                          PersonService personService) {
        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.productService = productService;
        this.personService = personService;
        this.returns = new ArrayList<>(returnRepository.loadAll());
    }

    /**
     * Registers a return for one or more products of an existing sale.
     * Validates that the sale exists, that it is within the 30-day return
     * window, and that every product indicated belongs to that sale.
     * Restores the stock of each returned product upon success.
     *
     * @param saleId     identifier of the original sale
     * @param productIds identifiers of the products being returned
     * @param reason     reason for the return
     * @return the registered return
     * @throws IllegalArgumentException if any validation fails; the message is in Spanish
     */
    public Return registerReturn(String saleId, List<String> productIds, String reason) {
        Sale sale = findSaleById(saleId);

        LocalDate saleDate = sale.getDate().toLocalDate();
        long daysSinceSale = ChronoUnit.DAYS.between(saleDate, LocalDate.now());
        if (daysSinceSale > RETURN_WINDOW_DAYS) {
            throw new IllegalArgumentException("El plazo de 30 días para devoluciones ha expirado.");
        }

        List<Product> returnedProducts = new ArrayList<>();
        for (String productId : productIds) {
            returnedProducts.add(findProductInSale(sale, productId));
        }

        String returnId = UUID.randomUUID().toString();
        Return ret = new Return(returnId, LocalDate.now(), sale, returnedProducts, reason, 0.0);
        ret.calculateRefundAmount();

        for (Product product : returnedProducts) {
            productService.restoreStock(product.getId(), 1);
        }

        returns.add(ret);
        returnRepository.saveAll(returns);

        return ret;
    }

    /**
     * Returns all registered returns.
     */
    public List<Return> viewAllReturns() {
        return new ArrayList<>(returns);
    }

    /**
     * Returns the returns whose original sale belongs to the given customer.
     */
    public List<Return> viewReturnsByCustomer(String customerId) {
        List<Return> result = new ArrayList<>();
        for (Return ret : returns) {
            if (ret.getOriginalSale().getCustomer().getId().equals(customerId)) {
                result.add(ret);
            }
        }
        return result;
    }

    /**
     * Returns the returns associated with the given sale.
     */
    public List<Return> viewReturnsBySale(String saleId) {
        List<Return> result = new ArrayList<>();
        for (Return ret : returns) {
            if (ret.getOriginalSale().getId().equals(saleId)) {
                result.add(ret);
            }
        }
        return result;
    }

    /**
     * Calculates the net balance (total sales minus total returns) for the
     * given month and year.
     */
    public double generateMonthlyBalance(int month, int year) {
        YearMonth target = YearMonth.of(year, month);

        double totalSales = 0.0;
        List<Sale> allSales = saleService.listAllSales(
                personService.listCustomers(),
                personService.listSellers(),
                productService.listAllProducts());
        for (Sale sale : allSales) {
            if (YearMonth.from(sale.getDate()).equals(target)) {
                totalSales += sale.calculateTotal();
            }
        }

        double totalReturns = 0.0;
        for (Return ret : returns) {
            if (YearMonth.from(ret.getDateReturn()).equals(target)) {
                totalReturns += ret.getRefund();
            }
        }

        return totalSales - totalReturns;
    }

    private Sale findSaleById(String saleId) {
        List<Sale> allSales = saleService.listAllSales(
                personService.listCustomers(),
                personService.listSellers(),
                productService.listAllProducts());
        for (Sale sale : allSales) {
            if (sale.getId().equals(saleId)) {
                return sale;
            }
        }
        throw new IllegalArgumentException("No existe una venta con el identificador indicado.");
    }

    private Product findProductInSale(Sale sale, String productId) {
        for (SalesLineItem item : sale.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                return item.getProduct();
            }
        }
        throw new IllegalArgumentException(
                "El producto " + productId + " no pertenece a la venta indicada.");
    }
}