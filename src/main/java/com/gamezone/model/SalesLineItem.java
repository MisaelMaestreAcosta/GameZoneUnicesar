package com.gamezone.model;

/**
 * Represents an individual line item within a sale transaction.
 * Acts as the Information Expert for its own subtotal calculation.
 */
public class SalesLineItem {

    private Product product;
    private int quantity;
    private double unitPrice;

    public SalesLineItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }

    /**
     * Calculates the subtotal for this specific line item.
     * 
     * @return Subtotal amount (quantity * unitPrice)
     */
    public double calculateSubtotal() {
        return this.quantity * this.unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return String.format("%s | Cantidad: %d | Precio U.: $%.2f | Subtotal: $%.2f",
                product.getTitle(), quantity, unitPrice, calculateSubtotal());
    }
}