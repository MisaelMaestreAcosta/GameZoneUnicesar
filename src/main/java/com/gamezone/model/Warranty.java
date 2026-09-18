package com.gamezone.model;

import java.time.LocalDate;

/**
 * abstract class representing a general warranty associated with a product and sale
 * @author Usuario
 */
public abstract class Warranty {
    private String id;
    private Product product;
    private Sale sale;
    private LocalDate startDate;
    private LocalDate endDate;
    
    /**
     * Constructs a Warranty and automatically calculates the end date based on duration.
     *
     * @param id the unique identifier of the warranty
     * @param product the associated product
     * @param sale the associated sale
     * @param startDate the start date of the warranty
     */
    
    public Warranty(String id, Product product, Sale sale, LocalDate startDate) {
        this.id = id;
        this.product = product;
        this.sale = sale;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(getDurationInMonths());
    }

    public String getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Sale getSale() {
        return sale;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    
    /**
     * Returns the duration of the warranty in months.
     *
     * @return duration in months
     */
    public abstract int getDurationInMonths();

    /**
     * Returns the formal name of the warranty type.
     *
     * @return warranty type name
     */
    public abstract String getWarrantyType();

    /**
     * Returns the additional monetary cost of the warranty.
     *
     * @return additional cost
     */
    public abstract double getAdditionalCost();

    /**
     * Checks if the warranty is active on a given date.
     *
     * @param date the date to check
     * @return true if the date is within range, false otherwise
     */
    public boolean isActive(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    /**
     * Generates a formatted certificate summary in Spanish.
     *
     * @return formatted string certificate
     */
    public String generateWarrantyCertificate() {
        return String.format("Certificado de %s | ID: %s | Producto: %s | Fecha inicio: %s | Fecha vencimiento: %s | Costo adicional: $%.2f",
                getWarrantyType(), 
                getId(), 
                (product != null ? product.getDescription() : "N/A"), 
                getStartDate(), 
                getEndDate(), 
                getAdditionalCost());
    }
    
}
