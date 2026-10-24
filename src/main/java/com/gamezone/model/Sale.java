package com.gamezone.model;

import java.time.LocalDateTime;
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
     * Calculates the subtotal of the items in the sale without discounts or warranties.
     * 
     * @return Subtotal of items
     */
    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (SalesLineItem item : items) {
            subtotal += item.calculateSubtotal();
        }
        return subtotal;
    }

    /**
     * Information Expert: aggregates individual item subtotals, applies discounts and adds warranty costs.
     * 
     * @return Total monetary cost of the sale
     */
    public double calculateTotal() {
        return calculateSubtotal() - discountAmount + warrantyCost;
    }

    /**
     * Validates that the sale complies with core business rules before committing.
     */
    public void validateSale() {
        if (this.items.isEmpty()) {
            throw new IllegalStateException("A sale must contain at least one line item.");
        }
    }

    /**
     * Checks if the sale is eligible for returns based on the 30-day policy.
     *
     * @return true if 30 days or less have passed since the sale date, false otherwise
     */
    public boolean canBeReturned() {
        if (this.date == null) {
            return false;
        }
        long daysSinceSale = java.time.temporal.ChronoUnit.DAYS.between(this.date.toLocalDate(), java.time.LocalDate.now());
        return daysSinceSale <= 30;
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

    public String getAppliedPromotionName() {
        return appliedPromotionName;
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
        return SaleReceiptFormatter.formatReceipt(this);
    }
}