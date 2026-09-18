package com.gamezone.model;

import java.util.List;

/**
 * class representing a game controller accessory
 * @author Usuario
 */
public class Controller extends Accessory{
    
    private String connectionType;
    
    /**
     * Constructs a new Controller accessory.
     *
     * @param id unique identifier
     * @param title title of the controller
     * @param price unit price
     * @param availability available inventory quantity
     * @param compatibleConsoleIds list of compatible console IDs
     * @param connectionType type of connection (e.g., Wireless, Wired)
     */

    public Controller(String id, String title, double price, int availability, List<String> compatibleConsoleIds, String connectionType) {
        super(id, title, price, availability, compatibleConsoleIds);
        this.connectionType= connectionType;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
    


    @Override
    public String getDescription() {
        return String.format("Controller: %s | Connection: %s | Price: $%.2f | Stock: %d",
                getTitle(), connectionType, getPrice(), getAvailability());
    }
    
}
