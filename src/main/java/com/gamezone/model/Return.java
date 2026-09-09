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
        this.dateReturn = dateReturn;
        this.originalSale = originalSale;
        this.listOfRetornedProduct = listOfRetornedProduct;
        this.reasonReturn = reasonReturn;
        this.refund = refund;
    }
    
    public String getReturnid() {
        return returnid;
    }

    public LocalDate getDateReturn() {
        return dateReturn;
    }

    public String getReasonReturn() {
        return reasonReturn;
    }

    public double getRefund() {
        return refund;
    }

    public Sale getOriginalSale() {
        return originalSale;
    }

    public List<Product> getListOfRetornedProduct() {
        return listOfRetornedProduct;
    }
    
    public double calculateRefundAmount() {
        double total = 0.0;
        if ( listOfRetornedProduct!= null) {
            for (Product product : listOfRetornedProduct) {
                total += product.getPrice();
            }
        }
        this.refund = total;
        return total;
    }
    public String generateReturnReceip(String returnid, LocalDate datereturn, Sale originalSale, List<Product> listOfRetornedProduct, String reasonReturn, double refund){
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
            originalSale.getId(), 
            reasonReturn,
            listOfRetornedProduct.toString(),
            refund
        );
    }
}
