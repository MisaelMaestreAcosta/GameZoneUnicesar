package com.gamezone.model;

import java.time.LocalDate;

/**
 * abstract class representing a general promotional discount in the system
 * @author Usuario
 */
public abstract class Promotion {
   private String id;
   private String name;
   private LocalDate startDate;
   private LocalDate endDate;
   
   /**
     * Constructs a Promotion with the specified details.
     *
     * @param id the unique identifier of the promotion
     * @param name the commercial name of the promotion
     * @param startDate the start date of validity
     * @param endDate the end date of validity
     */
   
   public Promotion(String id, String name, LocalDate startDate, LocalDate endDate){
       this.id=id;
       this.name=name;
       this.startDate=startDate;
       this.endDate=endDate;
   }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    /**
     * Checks if the promotion is active on a given date.
     *
     * @param date the date to check
     * @return true if the date is within the start and end dates inclusive; false otherwise
     */
    
    public boolean isActive(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    
    public abstract double calculateDiscount(Sale sale);
}
