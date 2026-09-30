package com.gamezone.model;

import java.time.LocalDate;

/**
 *
 * @author Usuario
 */
public class BulkPurchaseDiscount extends Promotion {
    private int minimumQuantity;
    private double discountPercentage;
    
    /**
     * Constructs a BulkPurchaseDiscount promotion.
     *
     * @param id the unique identifier
     * @param name the commercial name
     * @param startDate the start date of validity
     * @param endDate the end date of validity
     * @param minimumQuantity the minimum total items required
     * @param discountPercentage the percentage discount (0-100)
     */

    public BulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate, int minimumQuantity, double discountPercentage) {
        super(id, name, startDate, endDate);
        this.minimumQuantity=minimumQuantity;
        this.discountPercentage=discountPercentage;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    

    @Override
    public double calculateDiscount(Sale sale) {
        if (sale == null || sale.getItems() == null) {
            return 0.0;
        }

        int totalItems = 0;
        for (SalesLineItem item : sale.getItems()) {
            totalItems += item.getQuantity();
        }

        if (totalItems >= minimumQuantity) {
            return sale.calculateSubtotal() * (discountPercentage / 100.0);
        }
        return 0.0;
    }
    
    
}
