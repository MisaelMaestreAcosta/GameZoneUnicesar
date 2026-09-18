package com.gamezone.model;

import java.time.LocalDate;

/**
 * represents an extended warranty covering defects and damage for 12 months with a 10% product price fee
 * @author Usuario
 */
public class ExtendedWarranty extends Warranty{
    
    /**
     * Constructs an ExtendedWarranty.
     * @param id the unique identifier
     * @param product the associated product
     * @param sale the associated sale
     * @param startDate the start date
     */

    public ExtendedWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    /**
     * returns the duration of the extended warranty in months
     * @return 12 months duration
     */
    @Override
    public int getDurationInMonths() {
        return 12;
    }

    /**
     * returns the formal display name of this warranty type
     * @return "Garantía Extendida"
     */
    @Override
    public String getWarrantyType() {
        return "Garantía Extendida";
    }

    /**
     * calculates the additional cost of the extended warranty, which is 10% of the associated product price
     * @return 10% of the product price, or 0.0 if no product is associated
     */
    @Override
    public double getAdditionalCost() {
        if (getProduct() != null) {
            return getProduct().getPrice() * 0.10;
        }
        return 0.0;
    }
    
}
