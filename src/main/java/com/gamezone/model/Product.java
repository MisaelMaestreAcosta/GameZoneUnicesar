
package com.gamezone.model;


public abstract class Product {
    
    private String id;
    private String title;
    private double price;
    private int availability;
    
    public Product(String id, String title, double price, int availability){
        this.id=id;
        this.title=title;
        this.price=price;
        this.availability=availability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }
    public abstract String getDescription();
}
