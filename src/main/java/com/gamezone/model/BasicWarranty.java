package com.gamezone.model;

import java.time.LocalDate;

/**
 * represents a basic warranty covering factory defects for 6 months at no additional cost
 * @author Usuario
 */
public class BasicWarranty extends Warranty {
    
    /**
     * Constructs a BasicWarranty.
     *
     * @param id the unique identifier
     * @param product the associated product
     * @param sale the associated sale
     * @param startDate the start date
     */

    public BasicWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }
    
    /**
     * returns the duration of a basic warranty: 6 months
     * @return 6
     */
    
    @Override
    public int getDurationInMonths() {
        return 6;
    }
    
    /**
     * returns the type label for a basic warranty
     * @return "Garantia Basica"
     */

    @Override
    public String getWarrantyType() {
        return "Garantia basica";
    }
    
    /**
     * returns the additional cost of a basic warranty, which is free
     * @return 0.0
     */

    @Override
    public double getAdditionalCost() {
        return 0.0;
    }
    
}
