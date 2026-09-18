package com.gamezone.service;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.persistence.PromotionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing promotion evaluations and applying optimal discounts.
 */
public class PromotionService {

    private final PromotionRepository repository;
    private final List<Promotion> promotions;

    public PromotionService(PromotionRepository repository) {
        this.repository = repository;
        this.promotions = repository.loadAll();
    }

    public void registerPercentageDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double percentage) {
        PercentageDiscount promo = new PercentageDiscount(id, name, startDate, endDate, percentage);
        promotions.add(promo);
        repository.saveAll(promotions);
    }

    public void registerCategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double percentage, String targetCategory) {
        CategoryDiscount promo = new CategoryDiscount(id, name, startDate, endDate, percentage, targetCategory);
        promotions.add(promo);
        repository.saveAll(promotions);
    }

    public void registerBulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate, int minQuantity, double percentage) {
        BulkPurchaseDiscount promo = new BulkPurchaseDiscount(id, name, startDate, endDate, minQuantity, percentage);
        promotions.add(promo);
        repository.saveAll(promotions);
    }

    public List<Promotion> listAllPromotions() {
        return new ArrayList<>(promotions);
    }

    public List<Promotion> listActivePromotions() {
        LocalDate today = LocalDate.now();
        return promotions.stream()
                .filter(p -> p.isActive(today))
                .collect(Collectors.toList());
    }

    public Promotion findBestPromotionFor(Sale sale) {
        LocalDate today = sale.getDate().toLocalDate();
        List<Promotion> activePromotions = promotions.stream()
                .filter(p -> p.isActive(today))
                .collect(Collectors.toList());

        Promotion bestPromotion = null;
        double maxDiscount = 0.0;

        for (Promotion promotion : activePromotions) {
            double discount = promotion.calculateDiscount(sale);
            if (discount > maxDiscount) {
                maxDiscount = discount;
                bestPromotion = promotion;
            }
        }

        return bestPromotion;
    }

    public Promotion findById(String id) {
        return promotions.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
}