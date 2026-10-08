package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.SalesLineItem;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.model.Accessory;
import com.gamezone.service.AccessoryService;



import java.util.List;

/**
 * Service class responsible for managing sales operations, business rules,
 * and orchestrating stock updates through ProductService.
 */
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;

    private WarrantyService warrantyService;

    private AccessoryService accessoryService;



    public SaleService(SaleRepository saleRepository, ProductService productService, WarrantyService warrantyService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.warrantyService = warrantyService;
    }

    public void setWarrantyService(WarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    public Sale findById(String id) {
        return null; // Stub to satisfy compilation. Real implementation requires PersonService.
    }


    public SaleService(SaleRepository saleRepository, ProductService productService, AccessoryService accessoryService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.accessoryService = accessoryService;
    }


    private PromotionService promotionService;

    public void setPromotionService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    /**
     * Finalizes and records a sale transaction following the unified sales flow (A3):
     * 1. Validate stock, 2. Calculate subtotal, 3. Apply best promotion,
     * 4. Assign warranties, 5. Update stock, 6. Persist the sale.
     *
     * @param sale The sale transaction to complete
     * @param productIdsWithExtendedWarranty List of product IDs that should receive an extended warranty
     */
    public void registerSale(Sale sale, List<String> productIdsWithExtendedWarranty) {
        sale.validateSale();

        // 1. Validar stock: resolver cada ítem como producto o accesorio y verificar disponibilidad.
        for (SalesLineItem item : sale.getItems()) {
            Product currentProduct = null;
            if (item.getProduct() instanceof Accessory && accessoryService != null) {
                currentProduct = accessoryService.findById(item.getProduct().getId());
            } else {
                currentProduct = productService.findById(item.getProduct().getId());
            }
            if (currentProduct == null) {
                throw new IllegalStateException("Product not found: " + item.getProduct().getTitle());
            }
            if (currentProduct.getAvailability() < item.getQuantity()) {
                throw new IllegalStateException(String.format(
                        "Stock insuficiente para '%s'. Disponible: %d, Solicitado: %d",
                        currentProduct.getTitle(), currentProduct.getAvailability(), item.getQuantity()
                ));
            }
        }

        // 2. Calcular subtotal (solo ítems, sin descuentos ni garantías).
        double subtotal = sale.calculateSubtotal();

        // 3. Aplicar la mejor promoción vigente sobre el subtotal.
        if (promotionService != null && subtotal > 0) {
            com.gamezone.model.Promotion bestPromotion = promotionService.findBestPromotionFor(sale);
            if (bestPromotion != null) {
                double discount = Math.min(bestPromotion.calculateDiscount(sale), subtotal);
                sale.setAppliedPromotionName(bestPromotion.getName());
                sale.setDiscountAmount(discount);
            }
        }

        // 4. Asignar garantía básica a cada consola y las extendidas solicitadas.
        if (warrantyService != null) {
            for (SalesLineItem item : sale.getItems()) {
                Product product = item.getProduct();
                if (product instanceof com.gamezone.model.Console) {
                    warrantyService.assignBasicWarranty(product, sale, sale.getDate().toLocalDate());

                    if (productIdsWithExtendedWarranty != null && productIdsWithExtendedWarranty.contains(product.getId())) {
                        com.gamezone.model.ExtendedWarranty ew = warrantyService.assignExtendedWarranty(product, sale, sale.getDate().toLocalDate());
                        sale.addWarrantyCost(ew.getAdditionalCost());
                    }
                }
            }
        }

        // 5. Actualizar inventario delegando en ProductService o AccessoryService.
        for (SalesLineItem item : sale.getItems()) {
            Product product = item.getProduct();
            if (product instanceof Accessory && accessoryService != null) {
                accessoryService.updateStock(product.getId(), -item.getQuantity());
            } else {
                productService.updateStock(product.getId(), -item.getQuantity());
            }
        }

        // 6. Persistir la venta.
        saleRepository.save(sale);
    }

    public List<Sale> listAllSales(List<Customer> customers, List<Seller> sellers, List<Product> products) {
        return saleRepository.loadAll(customers, sellers, products);
    }
}