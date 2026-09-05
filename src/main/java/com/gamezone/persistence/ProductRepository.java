package com.gamezone.persistence;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.Videogame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private final Path filePath;
    private static final String SEPARATOR = ";";

    public ProductRepository() {
        this.filePath = Paths.get("data", "products.txt");
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (filePath.getParent() != null && Files.notExists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar el archivo de productos: " + e.getMessage());
        }
    }

    /**
     *loads all products stored in the .txt file
     */
    public List<Product> loadAll() {
        List<Product> products = new ArrayList<>();
        if (Files.notExists(filePath)) {
            return products;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Product product = parseRecord(line);
                    if (product != null) {
                        products.add(product);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading products from the file: " + e.getMessage());
        }
        return products;
    }

    /**
     *overwrites the .txt file, saving the complete list of products
     */
    public void saveAll(List<Product> products) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Product product : products) {
                String record = toRecord(product);
                if (!record.isEmpty()) {
                    writer.write(record);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving products to the file.: " + e.getMessage());
        }
    }

    /**
     *converts a Product entity into a delimited text line format
     */
    private String toRecord(Product product) {
        if (product instanceof Videogame game) {
            return String.join(SEPARATOR,
                    "VIDEOGAME",
                    game.getId(),
                    game.getTitle(),
                    String.valueOf(game.getPrice()),
                    String.valueOf(game.getAvailability()),
                    game.getPlatform(),
                    game.getGenre(),
                    game.getAgeRating()
            );
        } else if (product instanceof Console console) {
            return String.join(SEPARATOR,
                    "CONSOLE",
                    console.getId(),
                    console.getTitle(),
                    String.valueOf(console.getPrice()),
                    String.valueOf(console.getAvailability()),
                    console.getBrand(),
                    console.getModel(),
                    console.getGeneration()
            );
        }
        return "";
    }

    /**
     *converts a line from the .txt file into a Product object
     */
    private Product parseRecord(String line) {
        String[] fields = line.split(SEPARATOR);
        if (fields.length == 0) return null;

        String type = fields[0];

        if ("VIDEOGAME".equalsIgnoreCase(type) && fields.length >= 8) {
            String id = fields[1];
            String title = fields[2];
            double price = Double.parseDouble(fields[3]);
            int stock = Integer.parseInt(fields[4]);
            String platform = fields[5];
            String genre = fields[6];
            String ageRating = fields[7];

            return new Videogame(id, title, price, stock, platform, genre, ageRating);
        } else if ("CONSOLE".equalsIgnoreCase(type) && fields.length >= 8) {
            String id = fields[1];
            String title = fields[2];
            double price = Double.parseDouble(fields[3]);
            int stock = Integer.parseInt(fields[4]);
            String brand = fields[5];
            String model = fields[6];
            String generation = fields[7];

            return new Console(id, title, price, stock, brand, model, generation);
        }

        return null;
    }
}
