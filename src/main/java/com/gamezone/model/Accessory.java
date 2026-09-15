package com.gamezone.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public abstract class Accessory extends Product {
    private List<String> compatibleConsoleIds;
    
    /**
     * Constructs a new Accessory with all general attributes.
     *
     * @param id                   unique identifier
     * @param title                title or name of the accessory
     * @param price                unit price
     * @param availability        available inventory quantity
     * @param compatibleConsoleIds list of compatible console IDs
     */
    
    public Accessory(String id, String title, double price, int availability, List<String> compatibleConsoleIds) {
        super(id, title, price, availability);
        this.compatibleConsoleIds = new ArrayList<>(compatibleConsoleIds);
    }

    public List<String> getCompatibleConsoleIds() {
        return compatibleConsoleIds;
    }

    public void setCompatibleConsoleIds(List<String> compatibleConsoleIds) {
        this.compatibleConsoleIds = compatibleConsoleIds;
    }
    
    public void addCompatibleConsole(String consoleId) {
        if (consoleId != null && !this.compatibleConsoleIds.contains(consoleId)) {
            this.compatibleConsoleIds.add(consoleId);
        }
    }
    
    public boolean isCompatibleWith(String consoleId) {
        return compatibleConsoleIds.contains(consoleId);
    }
    
    
}
