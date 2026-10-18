package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonRepository {

    private static final Logger LOGGER = Logger.getLogger(PersonRepository.class.getName());
    private static final String DELIMITER = ",";

    private final Path customersPath;
    private final Path sellersPath;

    /**
     * Creates a repository that uses the default data files.
     */
    public PersonRepository() {
        this(Paths.get("data", "customers.txt"), Paths.get("data", "sellers.txt"));
    }

    /**
     * Creates a repository that reads and writes the given files.
     *
     * @param customersPath path of the customers file
     * @param sellersPath   path of the sellers file
     */
    public PersonRepository(Path customersPath, Path sellersPath) {
        this.customersPath = customersPath;
        this.sellersPath = sellersPath;
    }

    public void saveCustomers(List<Customer> customers) {
        List<String> lines = new ArrayList<>();
        for (Customer c : customers) {
            lines.add(String.join(DELIMITER, c.getName(), c.getId(), c.getPhone(), c.getEmail()));
        }
        writeToFile(customersPath, lines);
    }

    public List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        List<String> lines = readAllLines(customersPath);

        for (String line : lines) {
            String[] parts = line.split(DELIMITER);
            if (parts.length == 4) {
                customers.add(new Customer(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
            }
        }
        return customers;
    }

    public void saveSellers(List<Seller> sellers) {
        List<String> lines = new ArrayList<>();
        for (Seller s : sellers) {
            lines.add(String.join(DELIMITER, s.getName(), s.getId(), s.getPhone(), s.getEmployeeCode(), s.getShift()));
        }
        writeToFile(sellersPath, lines);
    }

    public List<Seller> loadSellers() {
        if (!Files.exists(sellersPath)) {
            return initializeDefaultSellers();
        }

        List<Seller> sellers = new ArrayList<>();
        List<String> lines = readAllLines(sellersPath);

        for (String line : lines) {
            String[] parts = line.split(DELIMITER);
            if (parts.length == 5) {
                sellers.add(new Seller(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim()));
            }
        }
        return sellers;
    }

    private void writeToFile(Path path, List<String> lines) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al escribir en el archivo: " + path, e);
        }
    }

    private List<String> readAllLines(Path path) {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer el archivo: " + path, e);
            return new ArrayList<>();
        }
    }

    private List<Seller> initializeDefaultSellers() {
        List<Seller> defaultSellers = List.of(
            new Seller("Carlos Ramírez", "S001", "3001112233", "EMP001", "Mañana"),
            new Seller("Laura Gómez", "S002", "3002223344", "EMP002", "Tarde"),
            new Seller("Andrés Torres", "S003", "3003334455", "EMP003", "Noche")
        );
        saveSellers(defaultSellers);
        return new ArrayList<>(defaultSellers);
    }
}