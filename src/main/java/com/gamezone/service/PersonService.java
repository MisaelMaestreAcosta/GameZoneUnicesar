package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PersonService {
    private final PersonRepository personRepository;
    private final List<Customer> customers;
    private final List<Seller> sellers;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
        this.customers = personRepository.loadCustomers();
        this.sellers = personRepository.loadSellers();
    }

    public void registerCustomer(Customer customer) {
        if (customer == null || customer.getId() == null || customer.getId().isBlank()) {
            throw new IllegalArgumentException("El cliente o su ID no pueden estar vacíos.");
        }

        if (findCustomerById(customer.getId()).isPresent()) {
            throw new IllegalStateException("Ya existe un cliente registrado con el ID: " + customer.getId());
        }

        customers.add(customer);
        personRepository.saveCustomers(customers);
    }

    public List<Customer> listCustomers() {
        // Retorna una vista inmodificable para proteger el estado interno
        return Collections.unmodifiableList(customers);
    }

    public Optional<Customer> findCustomerById(String id) {
        if (id == null) return Optional.empty();
        return customers.stream()
                .filter(c -> id.equalsIgnoreCase(c.getId()))
                .findFirst();
    }

    public void registerSeller(Seller seller) {
        if (seller == null || seller.getId() == null || seller.getId().isBlank()) {
            throw new IllegalArgumentException("El vendedor o su ID no pueden estar vacíos.");
        }

        if (findSellerById(seller.getId()).isPresent()) {
            throw new IllegalStateException("Ya existe un vendedor registrado con el ID: " + seller.getId());
        }

        sellers.add(seller);
        personRepository.saveSellers(sellers);
    }

    public List<Seller> listSellers() {
        return Collections.unmodifiableList(sellers);
    }

    public Optional<Seller> findSellerById(String id) {
        if (id == null) return Optional.empty();
        return sellers.stream()
                .filter(s -> id.equalsIgnoreCase(s.getId()))
                .findFirst();
    }
}