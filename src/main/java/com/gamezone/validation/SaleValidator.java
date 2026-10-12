package com.gamezone.validation;

import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import java.util.List;

/** Centralizes pre-sales validation checks. */
public final class SaleValidator {

    private SaleValidator() {
    }

    /** Validate the participants and ensure the sale contains products. */
    public static void validateSaleData(Customer customer, Seller seller, List<Product> products) {
        if (customer == null) {
            throw new InvalidDataException("cliente", "es obligatorio para registrar una venta");
        }
        if (seller == null) {
            throw new InvalidDataException("vendedor", "es obligatorio para registrar una venta");
        }
        if (products == null || products.isEmpty()) {
            throw new InvalidDataException("productos", "la venta debe contener al menos un producto");
        }
        for (Product product : products) {
            if (product == null) {
                throw new InvalidDataException("productos", "la lista no puede contener valores nulos");
            }
        }
    }

    /** Verify that the requested sale exists. */
    public static void validateSaleExists(Sale sale, String saleId) {
        if (sale == null) {
            throw new ResourceNotFoundException("venta", saleId);
        }
    }
}
