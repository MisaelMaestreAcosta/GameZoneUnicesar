package com.gamezone.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a completed sales transaction in GameZone.
 * Acts as Information Expert for calculating the total sale price
 * and enforcing transactional business rules.
 */
public class Sale {

    private String id;
    private LocalDateTime date;
    private Customer customer;
    private Seller seller;
    private final List<SalesLineItem> items;

    private double warrantyCost;

    private String appliedPromotionName;
    private double discountAmount;


    public Sale(String id, Customer customer, Seller seller) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (seller == null) {
            throw new IllegalArgumentException("Seller cannot be null");
        }
        this.id = id;
        this.date = LocalDateTime.now();
        this.customer = customer;
        this.seller = seller;
        this.items = new ArrayList<>();
        this.warrantyCost = 0.0;
    }

    /**
     * Adds an item to the sale transaction.
     * 
     * @param product  The product to add
     * @param quantity Number of units purchased
     */
    public void addLineItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.items.add(new SalesLineItem(product, quantity));
    }

    /**
     * Information Expert: aggregates individual item subtotals.
     * 
     * @return Total monetary cost of the sale
     */
    public double calculateTotal() {
        double total = 0.0;
        for (SalesLineItem item : items) {
            total += item.calculateSubtotal();
        }
        return total + warrantyCost;
    }

    /**
     * Validates that the sale complies with core business rules before committing.
     */
    public void validateSale() {
        if (this.items.isEmpty()) {
            throw new IllegalStateException("A sale must contain at least one line item.");
        }
    }

    public boolean canBeReturned() {
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        this.customer = customer;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        if (seller == null) {
            throw new IllegalArgumentException("Seller cannot be null");
        }
        this.seller = seller;
    }

    public List<SalesLineItem> getItems() {
        return Collections.unmodifiableList(items);
    }


    public double getWarrantyCost() {
        return warrantyCost;
    }

    public void addWarrantyCost(double cost) {
        this.warrantyCost += cost;
    }

    public void setAppliedPromotionName(String appliedPromotionName) {
        this.appliedPromotionName = appliedPromotionName;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;

    }

    @Override
    public String toString() {
        return generateReceipt();
    }

    public String generateReceipt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append(String.format("FACTURA DE VENTA #%s\n", id));
        sb.append(String.format("Fecha: %s\n", date.format(formatter)));
        sb.append(String.format("Cliente: %s (ID: %s)\n", customer.getName(), customer.getId()));
        sb.append(String.format("Vendedor: %s (ID: %s)\n", seller.getName(), seller.getId()));
        sb.append("-----------------------------------------\n");
        sb.append("DETALLE DE COMPRA:\n");
        for (SalesLineItem item : items) {
            sb.append(String.format("- %s\n", item.toString()));
        }
        if (warrantyCost > 0) {
            sb.append(String.format("- Costo Adicional Garantías: $%.2f\n", warrantyCost));
        }
        sb.append("-----------------------------------------\n");
        
        double subtotal = calculateTotal();
        sb.append(String.format("SUBTOTAL: $%.2f\n", subtotal));
        
        if (appliedPromotionName != null && !appliedPromotionName.isEmpty()) {
            sb.append(String.format("DESCUENTO (%s): -$%.2f\n", appliedPromotionName, discountAmount));
        }
        
        sb.append(String.format("TOTAL A PAGAR: $%.2f\n", subtotal - discountAmount));
        sb.append("=========================================");
        return sb.toString();
    }
}