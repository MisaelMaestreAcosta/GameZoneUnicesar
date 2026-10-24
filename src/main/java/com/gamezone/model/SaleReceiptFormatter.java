package com.gamezone.model;

import java.time.format.DateTimeFormatter;

/**
 * Formatter class responsible for generating textual receipt representations of sales.
 * Upholds the Single Responsibility Principle (SRP) by isolating presentation formatting from the Sale domain entity.
 */
public class SaleReceiptFormatter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Formats the given sale into a detailed human-readable receipt string.
     *
     * @param sale the sale transaction to format
     * @return formatted receipt text
     */
    public static String formatReceipt(Sale sale) {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append(String.format("FACTURA DE VENTA #%s\n", sale.getId()));
        sb.append(String.format("Fecha: %s\n", sale.getDate().format(FORMATTER)));
        sb.append(String.format("Cliente: %s (ID: %s)\n", sale.getCustomer().getName(), sale.getCustomer().getId()));
        sb.append(String.format("Vendedor: %s (ID: %s)\n", sale.getSeller().getName(), sale.getSeller().getId()));
        sb.append("-----------------------------------------\n");
        sb.append("DETALLE DE COMPRA:\n");
        for (SalesLineItem item : sale.getItems()) {
            sb.append(String.format("- %s\n", item.toString()));
        }
        sb.append("-----------------------------------------\n");
        double subtotal = sale.calculateSubtotal();
        sb.append(String.format("SUBTOTAL: $%.2f\n", subtotal));

        if (sale.getAppliedPromotionName() != null && !sale.getAppliedPromotionName().isEmpty()) {
            sb.append(String.format("DESCUENTO (%s): -$%.2f\n", sale.getAppliedPromotionName(), sale.getDiscountAmount()));
        }

        if (sale.getWarrantyCost() > 0) {
            sb.append(String.format("GARANTIAS EXTENDIDAS: +$%.2f\n", sale.getWarrantyCost()));
        }

        sb.append(String.format("TOTAL A PAGAR: $%.2f\n", sale.calculateTotal()));
        sb.append("=========================================");
        return sb.toString();
    }
}
