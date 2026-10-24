package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;

import java.util.List;

/**
 * Interface representing persistence operations for Sale domain entities.
 * Applies the Dependency Inversion Principle (DIP) by abstracting low-level data storage.
 */
public interface ISaleRepository {

    /**
     * Persists a completed sale transaction.
     *
     * @param sale The sale to persist
     */
    void save(Sale sale);

    /**
     * Loads all persisted sales reconstructing domain relationships.
     *
     * @param customers list of known customers
     * @param sellers   list of known sellers
     * @param products  list of known products and accessories
     * @return list of reconstructed sales
     */
    List<Sale> loadAll(List<Customer> customers, List<Seller> sellers, List<Product> products);
}
