package com.gamezone.model;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class Return {
    
    private String returnid;
    private LocalDate dateReturn;
    private String reasonReturn;
    private double refund;
    private Sale originalSale; 
    private List<Product> listOfRetornedProduct;
    
    
    public Return(String returnid, LocalDate datereturn, Sale originalSale, List<Product> listOfRetornedProduct, String reasonReturn, double refund) {
        this.returnid = returnid;
        this.dateReturn = datereturn;  
        this.originalSale = originalSale;
        this.listOfRetornedProduct = listOfRetornedProduct;
        this.reasonReturn = reasonReturn;
        this.refund = refund;
    }
    
    public String getReturnid() {
        return returnid;
    }

    public LocalDate getReturnDate() {
        return dateReturn;
    }

    public String getReasonReturn() {
        return reasonReturn;
    }

    public double getRefundAmount() {
        return refund;
    }

    public Sale getOriginalSale() {
        return originalSale;
    }

    public List<Product> getListOfRetornedProduct() {
        return listOfRetornedProduct;
    }
    
    public double calculateRefundAmount() {
        if (originalSale == null || listOfRetornedProduct == null|| listOfRetornedProduct.isEmpty()) {
            refund = 0.0;
            return refund;
        }

        double saleSubtotal = 0.0;
        for (SalesLineItem item : originalSale.getItems()) {
            saleSubtotal += item.getUnitPrice() * item.getQuantity();
        }

        // Avoid division by zero when the original sale has no priced items.
        if (saleSubtotal <= 0.0) {
            refund = 0.0;
            return refund;
        }

        double discountRatio = originalSale.getDiscountAmount() / saleSubtotal;
        double totalRefund = 0.0;

        for (Product returnedProduct : listOfRetornedProduct) {
            totalRefund += returnedProduct.getPrice() * (1.0 - discountRatio);
        }

        refund = totalRefund;
        return refund;
    }



    public String generateReturnReceipt() {
        StringBuilder details = new StringBuilder();
        double saleSubtotal = 0.0;

        if (originalSale != null) {
            for (SalesLineItem item : originalSale.getItems()) {
                saleSubtotal += item.getUnitPrice() * item.getQuantity();
            }
        }

        double discountRatio = saleSubtotal > 0.0 ? originalSale.getDiscountAmount() / saleSubtotal: 0.0;

        if (listOfRetornedProduct != null) {
            for (Product product : listOfRetornedProduct) {
                double listPrice = product.getPrice();
                double proportionalDiscount = listPrice * discountRatio;
                double itemRefund = listPrice - proportionalDiscount;

                details.append(String.format(
                        "- %s | Precio de lista: $%.2f | Descuento proporcional: $%.2f"
                                + " | Reembolso: $%.2f%n",
                        product.getTitle(),
                        listPrice,
                        proportionalDiscount,
                        itemRefund));
            }
        }

        return String.format(
            "========================================%n" +
            "           RECIBO DE DEVOLUCIÓN        %n" +
            "========================================%n" +
            "ID Devolución:  %s%n" +
            "Fecha:          %s%n" +
            "ID Venta Orig.: %s%n" +
            "Motivo:         %s%n" +
            "----------------------------------------%n" +
            "Productos Devueltos:%n" +
            "%s" +
            "----------------------------------------%n" +
            "TOTAL REEMBOLSADO: $%.2f%n" +
            "========================================%n",
            returnid,
            dateReturn,
            originalSale != null ? originalSale.getId() : "N/A", 
            reasonReturn,
            details.toString(),
            refund
        );
    }
}
