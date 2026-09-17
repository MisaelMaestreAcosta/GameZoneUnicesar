package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.WarrantyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service managing warranty assignments, active coverage, and upcoming expiration checks.
 */
public class WarrantyService {

    private final WarrantyRepository repository;
    private final List<Warranty> warranties;

    public WarrantyService(WarrantyRepository repository) {
        this.repository = repository;
        this.warranties = repository.loadAll();
    }

    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) {
        String id = "WAR-BAS-" + UUID.randomUUID().toString().substring(0, 8);
        BasicWarranty warranty = new BasicWarranty(id, product, sale, startDate);
        warranties.add(warranty);
        repository.saveAll(warranties);
        return warranty;
    }

    public ExtendedWarranty assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) {
        String id = "WAR-EXT-" + UUID.randomUUID().toString().substring(0, 8);
        ExtendedWarranty warranty = new ExtendedWarranty(id, product, sale, startDate);
        warranties.add(warranty);
        repository.saveAll(warranties);
        return warranty;
    }

    public Warranty findWarrantyByProduct(String productId, String saleId) {
        return warranties.stream()
                .filter(w -> w.getProduct().getId().equalsIgnoreCase(productId) 
                          && w.getSale().getId().equalsIgnoreCase(saleId))
                .findFirst()
                .orElse(null);
    }

    public List<Warranty> listAllWarranties() {
        return new ArrayList<>(warranties);
    }

    public List<Warranty> listActiveWarranties() {
        LocalDate today = LocalDate.now();
        return warranties.stream()
                .filter(w -> w.isActive(today))
                .collect(Collectors.toList());
    }

    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.plusDays(daysAhead);

        return warranties.stream()
                .filter(w -> w.isActive(today) 
                          && !w.getEndDate().isAfter(targetDate))
                .collect(Collectors.toList());
    }
}