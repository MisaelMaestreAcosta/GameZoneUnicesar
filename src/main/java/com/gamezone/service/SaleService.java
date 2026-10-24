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
import java.util.UUID;

/**
 * Service class responsible for managing sales operations, business rules,
 * and orchestrating stock updates through ProductService.
 */
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;

    private WarrantyService warrantyService;
    private AccessoryService accessoryService;
    private PromotionService promotionService;
    private PersonService personService;

    public SaleService(SaleRepository saleRepository, ProductService productService, WarrantyService warrantyService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.warrantyService = warrantyService;
    }

    public void setWarrantyService(WarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Looks up a sale transaction by its unique identifier.
     * Fulfills GRASP Controller and Indirection patterns by resolving collaborators internally.
     *
     * @param id the sale identifier to search
     * @return the matching Sale, or null if not found
     */
    public Sale findById(String id) {
        if (id == null) {
            return null;
        }
        return listAllSales().stream()
                .filter(sale -> sale.getId().equalsIgnoreCase(id.trim()))
                .findFirst()
                .orElse(null);
    }

    public SaleService(SaleRepository saleRepository, ProductService productService, AccessoryService accessoryService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.accessoryService = accessoryService;
    }

    public void setPromotionService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    /**
     * Creates a new Sale instance with an automatically generated unique transaction identifier.
     * Follows the GRASP Creator pattern by shielding the presentation layer from entity instantiation.
     *
     * @param customer customer associated with the transaction
     * @param seller seller attending the transaction
     * @return initialized Sale instance
     */
    public Sale createSale(Customer customer, Seller seller) {
        String saleId = "V-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return new Sale(saleId, customer, seller);
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

    /**
     * Retrieves all recorded sales by resolving person and product collaborators internally.
     * Upholds the GRASP Indirection pattern by decoupling clients from secondary service dependencies.
     *
     * @return list of reconstructed sales
     */
    public List<Sale> listAllSales() {
        if (personService == null || productService == null) {
            return java.util.Collections.emptyList();
        }
        return listAllSales(personService.listCustomers(), personService.listSellers(), productService.listAllProducts());
    }

    public List<Sale> listAllSales(List<Customer> customers, List<Seller> sellers, List<Product> products) {
        return saleRepository.loadAll(customers, sellers, products);
    }
}