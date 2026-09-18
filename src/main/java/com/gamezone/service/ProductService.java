package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.Videogame;
import com.gamezone.persistence.ProductRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * service class responsible for managing product operations within the GameZone system
 * @author Usuario
 */
public class ProductService {
    private final ProductRepository repository;
    private final List<Product> products;

    /**
     * constructs a new {@code ProductService} and loads initial products from the repository
     * @param repository the persistence repository implementation for products
     */
    public ProductService(ProductRepository repository) {
        this.repository = repository;
        this.products = new ArrayList<>(repository.loadAll());
    }

    /**
     * Registers a new video game in the inventory and persists the change
     * @param id the unique identifier of the videogame
     * @param title the videogame title
     * @param platform the gaming platform
     * @param availability the available stock quantity
     * @param price the selling price of the video game
     * @param genre the game genre
     * @param ageRating the age rating of the game
     */
    public void registerVideoGame(String id, String title, double price, int availability,String platform, String genre, String ageRating) {
        validateCommonFields(id, title, price, availability);
        
        Videogame videoGame = new Videogame(id, title, price, availability, platform, genre, ageRating);
        products.add(videoGame);
        repository.saveAll(products);
    }

    /**
     *registers a new console in the inventory and persists the change
     * @param id the unique identifier for the console
     * @param title the display title of the console
     * @param price the selling price of the console
     * @param availability the available stock quantity
     * @param brand  the console's brand
     * @param generation the console's generation
     * @param model the console's model
     */
    public void registerConsole(String id, String title, double price, int availability, String brand, String model, String generation) {
        validateCommonFields(id, title, price, availability);

        Console console = new Console(id, title, price, availability, brand, model, generation);
        products.add(console);
        repository.saveAll(products);
    }

    /**
     * returns an unmodifiable view of every product currently in the inventory
     * @return the list of all products
     */
    public List<Product> listAllProducts() {
        return Collections.unmodifiableList(products);
    }

    /**
     *adjusts the stock of the product with the given id, if found, and persists the change
     * @param productId the id of the product to update
     * @param quantity the amount to add to the current stock
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
    void restoreStock(String productId, int quantity){
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
     * @param id the product ID to search for
     * @return  the matching {@link Product} if found, or {@code null} otherwise
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
