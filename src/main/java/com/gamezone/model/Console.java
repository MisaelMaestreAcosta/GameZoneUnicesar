package com.gamezone.model;

/**
 * represents a gaming console product, with brand, model, and generation
 * in addition to the attributes shared with every {@link Product}.
 * @author Usuario
 */
public class Console extends Product{
    
    private String brand; 
    private String model;
    private String generation;
    
    /**
     *
     * @param id the unique identifier for the console
     * @param title the display title of the console
     * @param price the selling price of the console
     * @param availability the available stock quantity
     * @param brand the manufacturer brand of the console
     * @param model the specific hardware model name
     * @param generation the hardware generation classification
     */
    public Console(String id, String title, double price, int availability, String brand, String model, String generation) {
        super(id, title, price, availability);
        this.brand = brand;
        this.model = model;
        this.generation = generation;
    }

    /**
     * gets the manufacturer brand of the console
     * @return the brand
     */ 
    public String getBrand() {
        return brand;
    }

    /**
     * sets the brand of this console
     * @param brand the new brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * gets the specific hardware model of the console
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * sets the specific hardware model of the console
     * @param model the new model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * returns the generation of this console
     * @return the generation
     */
    public String getGeneration() {
        return generation;
    }

    /**
     * sets the generation of this console
     * @param generation the new generation
     */
    public void setGeneration(String generation) {
        this.generation = generation;
    }
    
    /**
     * Returns a formatted summary string containing detailed information about the console
     * including inherited product details and hardware specifications
     * @return
     */
    @Override
    public String getDescription() {
        return String.format("Console [ID: %s] %s | Brand: %s | Model: %s | Gen: %s | Price: $%.2f | Stock: %d",
                getId(), getTitle(), brand, model, generation, getPrice(), getAvailability());
    }
    
}
