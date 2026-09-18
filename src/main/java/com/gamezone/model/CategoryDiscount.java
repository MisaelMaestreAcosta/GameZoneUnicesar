package com.gamezone.model;

import java.time.LocalDate;

/**
 *Represents a promotion that applies a percentage discount only to products belonging to a specific target category
 * @author Usuario
 */

public class CategoryDiscount extends Promotion{
    
    private double discountPercentage;
    private String targetCategory;
    
    /**
     * Constructs a CategoryDiscount promotion
     * @param id the unique identifier
     * @param name the commercial name
     * @param startDate the start date of validity
     * @param endDate the end date of validity
     * @param discountPercentage the percentage discount (0-100)
     * @param targetCategory the target category ("VIDEOGAME" or "CONSOLE")
     */

    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage, String targetCategory) {
        super(id, name, startDate, endDate);
        this.discountPercentage=discountPercentage;
        this.targetCategory=targetCategory;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getTargetCategory() {
        return targetCategory;
    }

    public void setTargetCategory(String targetCategory) {
        this.targetCategory = targetCategory;
    }
    
    /**
     * calculates the discount amount applying the percentage only to products matching the specified target category and product instance type
     * @param sale the sale to calculate the discount for
     * @return the calculated monetary discount amount
     */
    
    @Override
    public double calculateDiscount(Sale sale) {
        double matchingTotal = 0.0;
        for (SalesLineItem item : sale.getItems()) {
            Product product = item.getProduct();
            if (product != null) {
                if ("VIDEOGAME".equals(targetCategory) && product instanceof Videogame) {
                    matchingTotal += product.getPrice() * item.getQuantity();
                } else if ("CONSOLE".equals(targetCategory) && product instanceof Console) {
                    matchingTotal += product.getPrice() * item.getQuantity();
                }
            }
        }
        return matchingTotal * discountPercentage / 100;
    }
    
    
}
