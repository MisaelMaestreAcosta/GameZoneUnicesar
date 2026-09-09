package com.gamezone.persistence;

import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.model.Product;
import com.gamezone.service.SaleService;
import com.gamezone.service.ProductService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of Return objects to and from the text file
 * data/returns.txt. Resolves Sale and Product references through the
 * injected services when loading returns from disk.
 */
public class ReturnRepository {

    private static final String FILE_PATH = "data/returns.txt";
    private static final String FIELD_DELIMITER = "|";
    private static final String PRODUCT_DELIMITER = ";";

    private final SaleService saleService;
    private final ProductService productService;

    /**
     * Constructs the repository, injecting the services needed to resolve
     * Sale and Product references when loading returns from the file.
     *
     * @param saleService    service used to resolve sale references
     * @param productService service used to resolve product references
     */
    public ReturnRepository(SaleService saleService, ProductService productService) {
        this.saleService = saleService;
        this.productService = productService;
    }

    /**
     * Persists all returns to the data/returns.txt file, overwriting any
     * previous content. Creates the data directory if it does not exist.
     *
     * @param returns list of Return objects to persist
     */
    public void saveAll(List<Return> returns) {
        Path path = Paths.get(FILE_PATH);

        // Ensure the 'data' directory exists
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException e) {
            System.err.println("Error al crear el directorio de datos: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Return ret : returns) {
                String line = String.join(FIELD_DELIMITER,
                        ret.getId(),
                        ret.getSale().getId(),
                        serializeProductIds(ret.getProducts()),
                        ret.getReturnDate().toString(),
                        escape(ret.getReason()),
                        String.valueOf(ret.getRefundAmount())
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar las devoluciones en el archivo: " + e.getMessage());
        }
    }

    /**
     * Loads all returns from the data/returns.txt file, resolving Sale and
     * Product references through the injected services.
     *
     * @return list of Return objects, or an empty list if the file does not exist
     */
    public List<Return> loadAll() {
        List<Return> returns = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return returns;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split("\\" + FIELD_DELIMITER, -1);
                String id = data[0];
                String saleId = data[1];
                String productIdsField = data[2];
                LocalDate returnDate = LocalDate.parse(data[3]);
                String reason = unescape(data[4]);
                double refundAmount = Double.parseDouble(data[5]);

                // Resolve references using the injected services
                Sale sale = saleService.findById(saleId);
                List<Product> products = new ArrayList<>();
                for (String productId : productIdsField.split(PRODUCT_DELIMITER)) {
                    if (!productId.isBlank()) {
                        Product product = productService.findById(productId);
                        if (product != null) {
                            products.add(product);
                        }
                    }
                }

                Return ret = new Return(id, sale, products, returnDate, reason, refundAmount);
                returns.add(ret);
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("Error al cargar las devoluciones desde el archivo: " + e.getMessage());
        }

        return returns;
    }

    /**
     * Serializes a list of products into a single string of product ids
     * separated by ';', so multiple returned products fit in one field.
     *
     * @param products list of returned products
     * @return semicolon-separated string of product ids
     */
    private String serializeProductIds(List<Product> products) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < products.size(); i++) {
            sb.append(products.get(i).getId());
            if (i < products.size() - 1) {
                sb.append(PRODUCT_DELIMITER);
            }
        }
        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(FIELD_DELIMITER, "/");
    }

    private String unescape(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }
}