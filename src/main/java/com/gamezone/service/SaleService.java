package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.SalesLineItem;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRepository;

import java.util.List;

/**
 * Service class responsible for managing sales operations, business rules,
 * and orchestrating stock updates through ProductService.
 */
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;

    public SaleService(SaleRepository saleRepository, ProductService productService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
    }

    /**
     * Finalizes and records a sale transaction.
     * Validates business invariants, verifies stock, decrements inventory, and commits persistence.
     *
     * @param sale The sale transaction to complete
     */
    public void processSale(Sale sale) {
        sale.validateSale();

        // 1. Verify stock availability for all items before applying changes
        for (SalesLineItem item : sale.getItems()) {
            Product currentProduct = productService.findById(item.getProduct().getId());
            if (currentProduct == null) {
                throw new IllegalStateException("Product not found: " + item.getProduct().getTitle());
            }
            if (currentProduct.getAvailability() < item.getQuantity()) {
                throw new IllegalStateException(String.format(
                        "Stock insuficiente para '%s'. Disponible: %d, Solicitado: %d",
                        currentProduct.getTitle(), currentProduct.getAvailability(), item.getQuantity()
                ));
            }
        }

        // 2. Decrement inventory through ProductService
        for (SalesLineItem item : sale.getItems()) {
            productService.updateStock(item.getProduct().getId(), -item.getQuantity());
        }

        // 3. Persist transaction in sales.txt
        saleRepository.save(sale);
    }

    public List<Sale> listAllSales(List<Customer> customers, List<Seller> sellers, List<Product> products) {
        return saleRepository.loadAll(customers, sellers, products);
    }
}