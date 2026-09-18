package com.gamezone.model;

import java.util.List;

/**
 * class representing a cable accessory
 * @author Usuario
 */
public class Cable extends Accessory {
    
    private double length;
    private String connectorType;
    
    /**
     * Constructs a new Cable accessory.
     *
     * @param id unique identifier
     * @param title title of the cable
     * @param price unit price
     * @param availability available inventory quantity
     * @param compatibleConsoleIds list of compatible console ID
     * @param length length in meters
     * @param connectorType        connector specification 
     */
    
    public Cable(String id, String title, double price, int availability, List<String> compatibleConsoleIds, double length, String connectorType) {
        super(id, title, price, availability, compatibleConsoleIds);
        this.length= length;
        this.connectorType= connectorType;
    }

    public double getLenght() {
        return length;
    }

    public void setLenght(double length) {
        this.length = length;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }
    

    @Override
    public String getDescription() {
        return String.format(
            "[%s] %s - Length: %.1fm, Connector: %s, Price: %.2f, Compatible consoles: %s",
            getId(), getTitle(), length, connectorType, getPrice(), getCompatibleConsoleIds());
    }
    
}
