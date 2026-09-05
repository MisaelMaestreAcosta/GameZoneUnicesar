package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.Videogame;
import com.gamezone.persistence.ProductRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductService {
    private final ProductRepository repository;
    private final List<Product> products;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
        this.products = new ArrayList<>(repository.loadAll());
    }

    /**
     * Registers a new video game in the inventory and persists the change
     */
    public void registerVideoGame(String id, String title, double price, int availability,String platform, String genre, String ageRating) {
        validateCommonFields(id, title, price, availability);
        
        Videogame videoGame = new Videogame(id, title, price, availability, platform, genre, ageRating);
        products.add(videoGame);
        repository.saveAll(products);
    }

    /**
     *registers a new console in the inventory and persists the change
     */
    public void registerConsole(String id, String title, double price, int availability, String brand, String model, String generation) {
        validateCommonFields(id, title, price, availability);

        Console console = new Console(id, title, price, availability, brand, model, generation);
        products.add(console);
        repository.saveAll(products);
    }

    /**
     * returns an unmodifiable view of every product currently in the inventory
     */
    public List<Product> listAllProducts() {
        return Collections.unmodifiableList(products);
    }

    /**
     *adjusts the stock of the product with the given id, if found, and persists the change
     */
    public void updateStock(String productId, int quantity) {
        Product product = findById(productId);
        if (product != null) {
            
            int newAvailability = product.getAvailability() + quantity;
            if (newAvailability < 0) {
                throw new IllegalArgumentException("Stock cannot be negative.");
            }
            product.setAvailability(newAvailability);
            repository.saveAll(products);
        } else {
            throw new IllegalArgumentException("Product with ID " + productId + " not found.");
        }
    }
    /**
     * finds a product by its id
     */
    public Product findById(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    /**
     * validates domain rules before instantiating products
     */
    private void validateCommonFields(String id, String title, double price, int availability) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty.");
        }
        if (findById(id) != null) {
            throw new IllegalArgumentException("A product with ID " + id + " already exists.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Product title cannot be empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (availability < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
    }
    
}
