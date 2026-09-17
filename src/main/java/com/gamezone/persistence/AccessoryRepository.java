package com.gamezone.persistence;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.Videogame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of Product objects (Console, Videogame) to and from a plain text file.
 */
public class AccessoryRepository {

    private static final String FILE_PATH = "data/products.txt";
    private static final String FIELD_DELIMITER = ",";

    public void saveAll(List<Product> products) {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Product product : products) {
                writer.write(toTextLine(product));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar los productos en el archivo.", e);
        }
    }

    public List<Product> loadAll() {
        List<Product> products = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return products;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line != null && !line.trim().isEmpty()) {
                    products.add(fromTextLine(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer los productos desde el archivo.", e);
        }

        return products;
    }

    private String toTextLine(Product product) {
        if (product instanceof Console) {
            Console console = (Console) product;
            return String.join(FIELD_DELIMITER,
                    "CONSOLE",
                    console.getId(),
                    console.getTitle(),
                    String.valueOf(console.getPrice()),
                    String.valueOf(console.getAvailability()),
                    console.getBrand(),
                    console.getModel(),
                    console.getGeneration());
        }

        if (product instanceof Videogame) {
            Videogame videogame = (Videogame) product;
            return String.join(FIELD_DELIMITER,
                    "VIDEOGAME",
                    videogame.getId(),
                    videogame.getTitle(),
                    String.valueOf(videogame.getPrice()),
                    String.valueOf(videogame.getAvailability()),
                    videogame.getPlatform(),
                    videogame.getGenre(),
                    videogame.getAgeRating());
        }

        throw new IllegalArgumentException("Tipo de producto desconocido: " + product.getClass().getName());
    }

    private Product fromTextLine(String line) {
        String[] fields = line.split(FIELD_DELIMITER, -1);
        String type = fields[0];
        String id = fields[1];
        String title = fields[2];
        double price = Double.parseDouble(fields[3]);
        int availability = Integer.parseInt(fields[4]);

        switch (type) {
            case "CONSOLE": {
                String brand = fields[5];
                String model = fields[6];
                String generation = fields[7];
                return new Console(id, title, price, availability, brand, model, generation);
            }
            case "VIDEOGAME": {
                String platform = fields[5];
                String genre = fields[6];
                String ageRating = fields[7];
                return new Videogame(id, title, price, availability, platform, genre, ageRating);
            }
            default:
                throw new IllegalArgumentException("Discriminador de producto no reconocido: " + type);
        }
    }
}