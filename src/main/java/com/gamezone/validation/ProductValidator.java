package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Product;

/** Centraliza las validaciones de datos e inventario de productos. */
public final class ProductValidator {

    private ProductValidator() {
    }

    /** Valida los campos comunes requeridos para registrar un producto. */
    public static void validateProductData(String id, String title, double price, int stock) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("identificador", "no puede estar vacío");
        }
        if (title == null || title.isBlank()) {
            throw new InvalidDataException("título", "no puede estar vacío");
        }
        if (!Double.isFinite(price) || price <= 0) {
            throw new InvalidDataException("precio", "debe ser un número mayor que cero");
        }
        if (stock < 0) {
            throw new InvalidDataException("stock", "no puede ser negativo");
        }
    }

    /** Comprueba que el producto tenga unidades suficientes para la operación. */
    public static void validateStockAvailability(Product product, int requestedQuantity) {
        validateProductExists(product, product == null ? "desconocido" : product.getId());
        if (requestedQuantity <= 0) {
            throw new InvalidDataException("cantidad", "debe ser mayor que cero");
        }
        if (product.getAvailability() < requestedQuantity) {
            throw new BusinessRuleException(String.format(
                    "Stock insuficiente para '%s'. Disponible: %d, solicitado: %d",
                    product.getTitle(), product.getAvailability(), requestedQuantity));
        }
    }

    /** Comprueba que el producto solicitado exista. */
    public static void validateProductExists(Product product, String productId) {
        if (product == null) {
            throw new ResourceNotFoundException("producto", productId);
        }
    }
}
