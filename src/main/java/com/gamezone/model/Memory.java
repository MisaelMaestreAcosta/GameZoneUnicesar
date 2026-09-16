package com.gamezone.model;

import java.util.List;

/**
 * class representing a storage memory accessory
 * @author Usuario
 */
public class Memory extends Accessory{
    private int capacityInGb;
    private String TypeMemory;
    
    /**
     * Constructs a new Memory accessory.
     *
     * @param id unique identifier
     * @param title title of the memory unit
     * @param price unit price
     * @param availability available inventory quantity
     * @param compatibleConsoleIds list of compatible console IDs
     * @param capacityInGb storage capacity in GB
     * @param TypeMemory type of storage (e.g., SD, microSD, Internal)
     */
    
    public Memory(String id, String title, double price, int availability, List<String> compatibleConsoleIds, int capacityInGb, String TypeMemory ) {
        super(id, title, price, availability, compatibleConsoleIds);
    
        this.capacityInGb= capacityInGb;
        this.TypeMemory=TypeMemory;
    }

    public int getCapacityInGb() {
        return capacityInGb;
    }

    public void setCapacityInGb(int capacityInGb) {
        this.capacityInGb = capacityInGb;
    }

    public String getTypeMemory() {
        return TypeMemory;
    }

    public void setTypeMemory(String TypeMemory) {
        this.TypeMemory = TypeMemory;
    }

    @Override
    public String getDescription() {
        return String.format(
            "[%s] %s - Capacity: %dGB, Type: %s, Price: %.2f, Compatible consoles: %s",
            getId(), getTitle(), capacityInGb, TypeMemory, getPrice(), getCompatibleConsoleIds());
    }
    
}
