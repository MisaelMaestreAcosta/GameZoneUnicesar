package com.gamezone.model;

/**
 * represents an abstract product in the GameZone store.
 * serving as a base class, it encapsulates common attributes such as identifier,title, price, and stock availability for all specific product types
 * @author Usuario
 */
public abstract class Product {
    
    private String id;
    private String title;
    private double price;
    private int availability;
    
    /**
     *
     * @param id the unique identifier of the product
     * @param title the display title of the product
     * @param price the selling price of the product
     * @param availability the available stock quantity
     */
    public Product(String id, String title, double price, int availability){
        this.id=id;
        this.title=title;
        this.price=price;
        this.availability=availability;
    }

    /**
     * gets the unique identifier of the product
     * @return the product id
     */
    public String getId() {
        return id;
    }

    /**
     * sets the unique identifier of the product
     * @param id the new product id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * gets the title of the product
     * @return the product title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the title of the product
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets the unit price of the product
     * @return the product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * sets the unit price of the product
     * @param price the product price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * gets the stock availability count of the product
     * @return the available quantity in stock
     */
    public int getAvailability() {
        return availability;
    }

    /**
     * sets the stock availability count of the product
     * @param availability
     */
    public void setAvailability(int availability) {
        this.availability = availability;
    }

    /**
     * retrieves a detailed description specific to the concrete product implementation
     * @return a readable description of the product
     */
    public abstract String getDescription();
}
