package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles persistence of Accessory objects to and from a TXT file.
 * Uses a type discriminator (CONTROLLER, CABLE, MEMORY) to rebuild subclasses.
 */
public class AccessoryRepository {

    private static final String FILE_PATH = "data/accessories.txt";
    private static final String FIELD_DELIMITER = ",";
    private static final String LIST_DELIMITER = "\\|";

    /**
     * Persists all accessories to the TXT file.
     *
     * @param accessories list of accessories to save
     */
    public void saveAll(List<Accessory> accessories) {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Accessory accessory : accessories) {
                writer.write(toTextLine(accessory));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar los accesorios en el archivo.", e);
        }
    }

    /**
     * Loads all accessories from the TXT file.
     *
     * @return list of loaded accessories, or empty list if file doesn't exist
     */
    public List<Accessory> loadAll() {
        List<Accessory> accessories = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return accessories;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    accessories.add(fromTextLine(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer los accesorios desde el archivo.", e);
        }

        return accessories;
    }

    private String toTextLine(Accessory accessory) {
        String compatibleConsoles = String.join("|", accessory.getCompatibleConsoleIds());

        if (accessory instanceof Controller controller) {
            return String.join(FIELD_DELIMITER,
                    "CONTROLLER",
                    controller.getId(),
                    controller.getTitle(),
                    String.valueOf(controller.getPrice()),
                    String.valueOf(controller.getAvailability()),
                    compatibleConsoles,
                    controller.getConnectionType());
        } else if (accessory instanceof Cable cable) {
            return String.join(FIELD_DELIMITER,
                    "CABLE",
                    cable.getId(),
                    cable.getTitle(),
                    String.valueOf(cable.getPrice()),
                    String.valueOf(cable.getAvailability()),
                    compatibleConsoles,
                    String.valueOf(cable.getLenght()),
                    cable.getConnectorType());
        } else if (accessory instanceof Memory memory) {
            return String.join(FIELD_DELIMITER,
                    "MEMORY",
                    memory.getId(),
                    memory.getTitle(),
                    String.valueOf(memory.getPrice()),
                    String.valueOf(memory.getAvailability()),
                    compatibleConsoles,
                    String.valueOf(memory.getCapacityInGb()),
                    memory.getTypeMemory());
        }
        throw new IllegalArgumentException("Tipo de accesorio desconocido.");
    }

    private Accessory fromTextLine(String line) {
        String[] fields = line.split(FIELD_DELIMITER, -1);
        String type = fields[0];
        String id = fields[1];
        String title = fields[2];
        double price = Double.parseDouble(fields[3]);
        int availability = Integer.parseInt(fields[4]);
        List<String> compatibleConsoles = fields[5].isBlank()
                ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(fields[5].split(LIST_DELIMITER)));

        switch (type) {
            case "CONTROLLER": {
                String connectionType = fields[6];
                Controller controller = new Controller(id, title, price, availability, compatibleConsoles, connectionType);
                return controller;
            }
            case "CABLE": {
                double lengthInMeters = Double.parseDouble(fields[6]);
                String connectorType = fields[7];
                Cable cable = new Cable(id, title, price, availability, compatibleConsoles, lengthInMeters, connectorType);
                return cable;
            }
            case "MEMORY": {
                int capacityInGb = Integer.parseInt(fields[6]);
                String memoryType = fields[7];
                Memory memory = new Memory(id, title, price, availability, compatibleConsoles, capacityInGb, memoryType);
                return memory;
            }
            default:
                throw new IllegalArgumentException("Tipo de accesorio desconocido: " + type);
        }
    }
}