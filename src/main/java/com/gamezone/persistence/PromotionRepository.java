package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of Promotion objects to and from a TXT file.
 */
public class PromotionRepository {

    private static final String FILE_PATH = "data/promotions.txt";
    private static final String FIELD_DELIMITER = ",";

    public void saveAll(List<Promotion> promotions) {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Promotion promotion : promotions) {
                writer.write(toTextLine(promotion));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar las promociones en el archivo.", e);
        }
    }

    public List<Promotion> loadAll() {
        List<Promotion> promotions = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return promotions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    promotions.add(fromTextLine(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer las promociones desde el archivo.", e);
        }

        return promotions;
    }

    private String toTextLine(Promotion promotion) {
        if (promotion instanceof PercentageDiscount pd) {
            return String.join(FIELD_DELIMITER,
                    "PERCENTAGE",
                    pd.getId(),
                    pd.getName(),
                    pd.getStartDate().toString(),
                    pd.getEndDate().toString(),
                    String.valueOf(pd.getDiscountPercentage()));
        } else if (promotion instanceof CategoryDiscount cd) {
            return String.join(FIELD_DELIMITER,
                    "CATEGORY",
                    cd.getId(),
                    cd.getName(),
                    cd.getStartDate().toString(),
                    cd.getEndDate().toString(),
                    String.valueOf(cd.getDiscountPercentage()),
                    cd.getTargetCategory());
        } else if (promotion instanceof BulkPurchaseDiscount bd) {
            return String.join(FIELD_DELIMITER,
                    "BULK",
                    bd.getId(),
                    bd.getName(),
                    bd.getStartDate().toString(),
                    bd.getEndDate().toString(),
                    String.valueOf(bd.getDiscountPercentage()),
                    String.valueOf(bd.getMinimumQuantity()));
        }
        throw new IllegalArgumentException("Tipo de promoción desconocido.");
    }

    private Promotion fromTextLine(String line) {
        String[] fields = line.split(FIELD_DELIMITER, -1);
        String type = fields[0];
        String id = fields[1];
        String name = fields[2];
        LocalDate startDate = LocalDate.parse(fields[3]);
        LocalDate endDate = LocalDate.parse(fields[4]);

        switch (type) {
            case "PERCENTAGE":
                double percentage = Double.parseDouble(fields[5]);
                return new PercentageDiscount(id, name, startDate, endDate, percentage);
            case "CATEGORY":
                double catPercentage = Double.parseDouble(fields[5]);
                String targetCategory = fields[6];
                return new CategoryDiscount(id, name, startDate, endDate, catPercentage, targetCategory);
            case "BULK":
                double bulkPercentage = Double.parseDouble(fields[5]);
                int minQuantity = Integer.parseInt(fields[6]);
                return new BulkPurchaseDiscount(id, name, startDate, endDate, minQuantity, bulkPercentage);
            default:
                throw new IllegalArgumentException("Discriminador de promoción no reconocido: " + type);
        }
    }
}