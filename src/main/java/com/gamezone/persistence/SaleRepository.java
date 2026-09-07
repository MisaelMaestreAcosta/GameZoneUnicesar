package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.SalesLineItem;
import com.gamezone.model.Seller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles flat-file data persistence for sales transactions in the GameZone
 * system.
 */
public class SaleRepository {

    private final Path filePath;
    private static final String SEPARATOR = ";";

    public SaleRepository() {
        this.filePath = Paths.get("data", "sales.txt");
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (filePath.getParent() != null && Files.notExists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.err.println("Error initializing sales file: " + e.getMessage());
        }
    }

    /**
     * Appends a newly completed sale to the sales.txt file.
     *
     * @param sale The sale to persist
     */
    public void save(Sale sale) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, java.nio.file.StandardOpenOption.APPEND)) {
            // Write Sale Header
            writer.write(String.join(SEPARATOR,
                    "SALE",
                    sale.getId(),
                    sale.getDate().toString(),
                    sale.getCustomer().getId(),
                    sale.getSeller().getId(),
                    String.valueOf(sale.calculateTotal())
            ));
            writer.newLine();

            // Write Line Items
            for (SalesLineItem item : sale.getItems()) {
                writer.write(String.join(SEPARATOR,
                        "ITEM",
                        sale.getId(),
                        item.getProduct().getId(),
                        String.valueOf(item.getQuantity()),
                        String.valueOf(item.getUnitPrice())
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving sale transaction: " + e.getMessage());
        }
    }

    /**
     * Reads all recorded sales headers and items, linking them to existing
     * products and persons.
     */
    public List<Sale> loadAll(List<Customer> customers, List<Seller> sellers, List<Product> products) {
        List<Sale> sales = new ArrayList<>();
        if (Files.notExists(filePath)) {
            return sales;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            Sale currentSale = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] fields = line.split(SEPARATOR);

                if ("SALE".equalsIgnoreCase(fields[0]) && fields.length >= 6) {
                    String saleId = fields[1];
                    LocalDateTime date = LocalDateTime.parse(fields[2]);
                    String customerId = fields[3];
                    String sellerId = fields[4];

                    Customer customer = customers.stream()
                            .filter(c -> c.getId().equalsIgnoreCase(customerId))
                            .findFirst().orElse(null);

                    Seller seller = sellers.stream()
                            .filter(s -> s.getId().equalsIgnoreCase(sellerId))
                            .findFirst().orElse(null);

                    if (customer != null && seller != null) {
                        currentSale = new Sale(saleId, customer, seller);
                        currentSale.setDate(date);
                        sales.add(currentSale);
                    }
                } else if ("ITEM".equalsIgnoreCase(fields[0]) && fields.length >= 5 && currentSale != null) {
                    String productId = fields[2];
                    int quantity = Integer.parseInt(fields[3]);

                    Product product = products.stream()
                            .filter(p -> p.getId().equalsIgnoreCase(productId))
                            .findFirst().orElse(null);

                    if (product != null) {
                        currentSale.addLineItem(product, quantity);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading sales from file: " + e.getMessage());
        }
        return sales;
    }
}
