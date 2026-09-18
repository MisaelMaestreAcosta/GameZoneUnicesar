package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.SalesLineItem;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.model.Accessory;
import com.gamezone.service.AccessoryService;



import java.util.List;

/**
 * Service class responsible for managing sales operations, business rules,
 * and orchestrating stock updates through ProductService.
 */
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;

    private WarrantyService warrantyService;

    private AccessoryService accessoryService;



    public SaleService(SaleRepository saleRepository, ProductService productService, WarrantyService warrantyService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.warrantyService = warrantyService;
    }

    public void setWarrantyService(WarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    public Sale findById(String id) {
        return null; // Stub to satisfy compilation. Real implementation requires PersonService.
    }


    public SaleService(SaleRepository saleRepository, ProductService productService, AccessoryService accessoryService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.accessoryService = accessoryService;
    }


    /**
     * Finalizes and records a sale transaction.
     * Validates business invariants, verifies stock, decrements inventory, and commits persistence.
     *
     * @param sale The sale transaction to complete
     * @param productIdsWithExtendedWarranty List of product IDs that should receive an extended warranty
     */
    public void registerSale(Sale sale, List<String> productIdsWithExtendedWarranty) {
        sale.validateSale();

        // 1. Verify stock availability for all items before applying changes
        for (SalesLineItem item : sale.getItems()) {
            Product currentProduct = null;
            if (item.getProduct() instanceof Accessory && accessoryService != null) {
                currentProduct = accessoryService.findById(item.getProduct().getId());
            } else {
                currentProduct = productService.findById(item.getProduct().getId());
            }
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

        // 2. Decrement inventory through ProductService or AccessoryService
        for (SalesLineItem item : sale.getItems()) {

            productService.updateStock(item.getProduct().getId(), -item.getQuantity());
            
            // Assign warranties
            Product product = item.getProduct();
            if (product instanceof com.gamezone.model.Console) {
                if (warrantyService != null) {
                    warrantyService.assignBasicWarranty(product, sale, sale.getDate().toLocalDate());
                }
            }
            if (productIdsWithExtendedWarranty != null && productIdsWithExtendedWarranty.contains(product.getId())) {
                if (warrantyService != null) {
                    com.gamezone.model.ExtendedWarranty ew = warrantyService.assignExtendedWarranty(product, sale, sale.getDate().toLocalDate());
                    sale.addWarrantyCost(ew.getAdditionalCost());
                }
            }

            if (item.getProduct() instanceof Accessory && accessoryService != null) {
                accessoryService.updateStock(item.getProduct().getId(), -item.getQuantity());
            } else {
                productService.updateStock(item.getProduct().getId(), -item.getQuantity());
            }


        }


        // 4. Persist transaction in sales.txt
        saleRepository.save(sale);
    }

    public List<Sale> listAllSales(List<Customer> customers, List<Seller> sellers, List<Product> products) {
        return saleRepository.loadAll(customers, sellers, products);
    }
}