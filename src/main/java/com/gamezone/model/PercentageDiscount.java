package com.gamezone.model;

import java.time.LocalDate;

/**
 * represents a promotion that applies a percentage discount over the sale total
 * @author Usuario
 */

public class PercentageDiscount extends Promotion {
    private double discountPercentage;
    
    /**
     * Constructs a PercentageDiscount promotion.
     *
     * @param id the unique identifier
     * @param name the commercial name
     * @param startDate the start date of validity
     * @param endDate the end date of validity
     * @param discountPercentage the percentage to apply (0-100)
     */

    public PercentageDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage) {
        super(id, name, startDate, endDate);
        this.discountPercentage=discountPercentage;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    /**
     * calculates the discount amount by applying the percentage to the total subtotal of the sale items
     * @param sale the sale to calculate the discount for
     * @return the calculated monetary discount amount
     */
    
    @Override
    public double calculateDiscount(Sale sale) {
        return sale.getTotalAmount() * (discountPercentage / 100.0);
    }
    
}
