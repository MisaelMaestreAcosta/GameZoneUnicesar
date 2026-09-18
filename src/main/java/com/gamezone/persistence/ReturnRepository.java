package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.service.PersonService;
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
 * Handles persistence of Return objects to and from a plain text file.
 * Resolves references to the original Sale and to the returned Product
 * instances through SaleService and ProductService when reconstructing
 * objects from the file. Customers and sellers needed to rebuild sales
 * are obtained from PersonService, following the same pattern already
 * used elsewhere in the system.
 */
public class ReturnRepository {

    private static final String FILE_PATH = "data/returns.txt";
    private static final String FIELD_DELIMITER = ",";
    private static final String LIST_DELIMITER = "\\|";

    private final SaleService saleService;
    private final ProductService productService;
    private final PersonService personService;

    /**
     * Creates the repository with the services needed to resolve sale and
     * product references while loading returns from the text file.
     *
     * @param saleService    used to look up the original sale by id
     * @param productService used to look up returned products by id
     * @param personService  used to obtain the customers and sellers needed
     *                       by SaleService to reconstruct sales
     */
    public ReturnRepository(SaleService saleService, ProductService productService, PersonService personService) {
        this.saleService = saleService;
        this.productService = productService;
        this.personService = personService;
    }

    /**
     * Persists the full list of returns to the text file, overwriting its content.
     *
     * @param returns the returns to persist
     */
    public void saveAll(List<Return> returns) {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Return ret : returns) {
                writer.write(toTextLine(ret));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar las devoluciones en el archivo.", e);
        }
    }

    /**
     * Loads all returns from the text file, resolving each original sale
     * against the customers and sellers known to PersonService.
     *
     * @return the list of returns, or an empty list if the file does not exist
     */
    public List<Return> loadAll() {
        List<Return> returns = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return returns;
        }

        List<Sale> allSales = saleService.listAllSales(
                personService.listCustomers(),
                personService.listSellers(),
                productService.listAllProducts());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    returns.add(fromTextLine(line, allSales));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer las devoluciones desde el archivo.", e);
        }

        return returns;
    }

    private String toTextLine(Return ret) {
        StringBuilder productIds = new StringBuilder();
        for (Product product : ret.getListOfRetornedProduct()) {
            if (productIds.length() > 0) {
                productIds.append("|");
            }
            productIds.append(product.getId());
        }

        return String.join(FIELD_DELIMITER,
                ret.getReturnid(),
                ret.getDateReturn().toString(),
                ret.getOriginalSale().getId(),
                productIds.toString(),
                ret.getReasonReturn(),
                String.valueOf(ret.getRefund()));
    }

    private Return fromTextLine(String line, List<Sale> allSales) {
        String[] fields = line.split(FIELD_DELIMITER, -1);
        String returnId = fields[0];
        LocalDate dateReturn = LocalDate.parse(fields[1]);
        String saleId = fields[2];
        String[] productIds = fields[3].isBlank() ? new String[0] : fields[3].split(LIST_DELIMITER);
        String reasonReturn = fields[4];
        double refund = Double.parseDouble(fields[5]);

        Sale originalSale = findSaleById(allSales, saleId);

        List<Product> returnedProducts = new ArrayList<>();
        for (String productId : productIds) {
            returnedProducts.add(productService.findById(productId));
        }

        return new Return(returnId, dateReturn, originalSale, returnedProducts, reasonReturn, refund);
    }

    private Sale findSaleById(List<Sale> sales, String saleId) {
        for (Sale sale : sales) {
            if (sale.getId().equals(saleId)) {
                return sale;
            }
        }
        throw new IllegalArgumentException("No existe una venta con el identificador: " + saleId);
    }
}
