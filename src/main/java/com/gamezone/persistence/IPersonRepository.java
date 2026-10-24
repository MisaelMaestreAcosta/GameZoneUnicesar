package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;

import java.util.List;

/**
 * Interface representing persistence operations for Person domain entities (Customer, Seller).
 * Applies Dependency Inversion Principle (DIP) by abstracting low-level data storage.
 */
public interface IPersonRepository {

    void saveCustomers(List<Customer> customers);

    List<Customer> loadCustomers();

    void saveSellers(List<Seller> sellers);

    List<Seller> loadSellers();
}