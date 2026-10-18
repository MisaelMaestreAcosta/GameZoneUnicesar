package com.gamezone.persistence;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

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
 * Handles persistence of Warranty records to and from a TXT file.
 */
public class WarrantyRepository {

    private static final String FIELD_DELIMITER = ",";

    private final SaleService saleService;
    private final ProductService productService;
    private final String filePath;

    /**
     * Creates a repository that uses the default warranties file.
     *
     * @param saleService    used to look up sales by id
     * @param productService used to look up products by id
     */
    public WarrantyRepository(SaleService saleService, ProductService productService) {
        this(saleService, productService, "data/warranties.txt");
    }

    /**
     * Creates a repository that reads and writes the given file.
     *
     * @param saleService    used to look up sales by id
     * @param productService used to look up products by id
     * @param filePath       path of the warranties file
     */
    public WarrantyRepository(SaleService saleService, ProductService productService, String filePath) {
        this.saleService = saleService;
        this.productService = productService;
        this.filePath = filePath;
    }

    public void saveAll(List<Warranty> warranties) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Warranty warranty : warranties) {
                writer.write(toTextLine(warranty));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar las garantías en el archivo.", e);
        }
    }

    public List<Warranty> loadAll() {
        List<Warranty> warranties = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return warranties;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    Warranty warranty = fromTextLine(line);
                    if (warranty != null) {
                        warranties.add(warranty);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer las garantías desde el archivo.", e);
        }

        return warranties;
    }

    private String toTextLine(Warranty warranty) {
        String type = (warranty instanceof BasicWarranty) ? "BASIC" : "EXTENDED";
        return String.join(FIELD_DELIMITER,
                type,
                warranty.getId(),
                warranty.getProduct().getId(),
                warranty.getSale().getId(),
                warranty.getStartDate().toString());
    }

    private Warranty fromTextLine(String line) {
        String[] fields = line.split(FIELD_DELIMITER, -1);
        String type = fields[0];
        String id = fields[1];
        String productId = fields[2];
        String saleId = fields[3];
        LocalDate startDate = LocalDate.parse(fields[4]);

        Product product = productService.findById(productId);
        Sale sale = saleService.findById(saleId);

        if (product == null || sale == null) {
            return null;
        }

        if ("BASIC".equalsIgnoreCase(type)) {
            return new BasicWarranty(id, product, sale, startDate);
        } else if ("EXTENDED".equalsIgnoreCase(type)) {
            return new ExtendedWarranty(id, product, sale, startDate);
        }
        throw new IllegalArgumentException("Tipo de garantía no reconocido: " + type);
    }
}