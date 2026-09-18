package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;
import com.gamezone.persistence.AccessoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic service for managing accessory registration and queries.
 */
public class AccessoryService {

    private final AccessoryRepository repository;
    private final List<Accessory> accessories;

    public AccessoryService(AccessoryRepository repository) {
        this.repository = repository;
        this.accessories = repository.loadAll();
    }

    public void registerController(String id, String title, double price, int quantity, String connectionType, List<String> compatibleConsoles) {
        Controller controller = new Controller(id, title, price, quantity, connectionType);
        if (compatibleConsoles != null) {
            controller.setCompatibleConsoles(compatibleConsoles);
        }
        accessories.add(controller);
        repository.saveAll(accessories);
    }

    public void registerCable(String id, String title, double price, int quantity, double lengthInMeters, String connectorType, List<String> compatibleConsoles) {
        Cable cable = new Cable(id, title, price, quantity, lengthInMeters, connectorType);
        if (compatibleConsoles != null) {
            cable.setCompatibleConsoles(compatibleConsoles);
        }
        accessories.add(cable);
        repository.saveAll(accessories);
    }

    public void registerMemory(String id, String title, double price, int quantity, int capacityInGb, String memoryType, List<String> compatibleConsoles) {
        Memory memory = new Memory(id, title, price, quantity, capacityInGb, memoryType);
        if (compatibleConsoles != null) {
            memory.setCompatibleConsoles(compatibleConsoles);
        }
        accessories.add(memory);
        repository.saveAll(accessories);
    }

    public List<Accessory> listAllAccessories() {
        return new ArrayList<>(accessories);
    }

    public List<Accessory> listAccessoriesByType(String type) {
        return accessories.stream()
                .filter(a -> a.getClass().getSimpleName().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<Accessory> findAccessoriesCompatibleWith(String consoleId) {
        return accessories.stream()
                .filter(a -> a.getCompatibleConsoles().contains(consoleId))
                .collect(Collectors.toList());
    }

    public Accessory findById(String id) {
        return accessories.stream()
                .filter(a -> a.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public void updateStock(String accessoryId, int quantity) {
        Accessory accessory = findById(accessoryId);
        if (accessory != null) {
            accessory.setAvailability(accessory.getAvailability() + quantity);
            repository.saveAll(accessories);
        } else {
            throw new IllegalArgumentException("Accesorio no encontrado con ID: " + accessoryId);
        }
    }
}