package com.gamezone.ui;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.model.SalesLineItem;
import com.gamezone.model.Seller;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

/**
 * Text-based console interface providing interactive menus for catalog, sales,
 * person management, product returns, and financial balance reporting.
 */
public class ConsoleMenu {

    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;
    private final ReturnService returnService;
    private final Scanner scanner;

    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService) {
        this(productService, personService, saleService, null);
    }

    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService, ReturnService returnService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.returnService = returnService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option = -1;
        do {
            System.out.println("\n=========================================");
            System.out.println("       GAMEZONE UNICESAR - SISTEMA       ");
            System.out.println("=========================================");
            System.out.println("1. Listar Catálogo de Productos");
            System.out.println("2. Registrar Nuevo Videojuego");
            System.out.println("3. Registrar Nueva Consola");
            System.out.println("4. Listar Clientes y Vendedores");
            System.out.println("5. Registrar una Venta");
            System.out.println("6. Ver Historial de Ventas");
            System.out.println("7. Gestión de Devoluciones");
            System.out.println("8. Consultar Balance Mensual");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine().trim());
                switch (option) {
                    case 1 -> showProductCatalog();
                    case 2 -> registerVideoGame();
                    case 3 -> registerConsole();
                    case 4 -> showPersons();
                    case 5 -> processNewSale();
                    case 6 -> showSalesHistory();
                    case 7 -> handleReturnsMenu();
                    case 8 -> showMonthlyBalance();
                    case 0 -> System.out.println("Saliendo del sistema... ¡Hasta luego!");
                    default -> System.out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (option != 0);
    }

    private void showProductCatalog() {
        System.out.println("\n--- CATÁLOGO DE PRODUCTOS DISPONIBLES ---");
        List<Product> products = productService.listAllProducts();
        if (products.isEmpty()) {
            System.out.println("No hay productos registrados en el inventario.");
            return;
        }
        for (Product p : products) {
            System.out.printf("[%s] %s | Stock: %d | Precio: $%.2f%n",
                    p.getId(), p.getDescription(), p.getAvailability(), p.getPrice());
        }
    }

    private void registerVideoGame() {
        System.out.println("\n--- REGISTRO DE VIDEOJUEGO ---");
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Título: ");
        String title = scanner.nextLine().trim();
        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Stock inicial: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Plataforma: ");
        String platform = scanner.nextLine().trim();
        System.out.print("Género: ");
        String genre = scanner.nextLine().trim();
        System.out.print("Clasificación de Edad: ");
        String rating = scanner.nextLine().trim();

        productService.registerVideoGame(id, title, price, stock, platform, genre, rating);
        System.out.println("¡Videojuego registrado con éxito!");
    }

    private void registerConsole() {
        System.out.println("\n--- REGISTRO DE CONSOLA ---");
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Título: ");
        String title = scanner.nextLine().trim();
        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Stock inicial: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Marca: ");
        String brand = scanner.nextLine().trim();
        System.out.print("Modelo: ");
        String model = scanner.nextLine().trim();
        System.out.print("Generación: ");
        String generation = scanner.nextLine().trim();

        productService.registerConsole(id, title, price, stock, brand, model, generation);
        System.out.println("¡Consola registrada con éxito!");
    }

    private void showPersons() {
        System.out.println("\n--- CLIENTES ---");
        List<Customer> customers = personService.listCustomers();
        if (customers.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            customers.forEach(c ->
                    System.out.printf("[%s] %s - Tel: %s | Rol: %s%n",
                            c.getId(), c.getName(), c.getPhone(), c.getRoleDescription())
            );
        }

        System.out.println("\n--- VENDEDORES ---");
        List<Seller> sellers = personService.listSellers();
        if (sellers.isEmpty()) {
            System.out.println("No hay vendedores registrados.");
        } else {
            sellers.forEach(s ->
                    System.out.printf("[%s] %s - Tel: %s | Rol: %s%n",
                            s.getId(), s.getName(), s.getPhone(), s.getRoleDescription())
            );
        }
    }

    private void processNewSale() {
        System.out.println("\n--- REALIZAR NUEVA VENTA ---");
        System.out.print("Ingrese ID del Cliente: ");
        String customerId = scanner.nextLine().trim();
        Optional<Customer> customerOpt = personService.findCustomerById(customerId);
        if (customerOpt.isEmpty()) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("Ingrese ID del Vendedor: ");
        String sellerId = scanner.nextLine().trim();
        Optional<Seller> sellerOpt = personService.findSellerById(sellerId);
        if (sellerOpt.isEmpty()) {
            System.out.println("Vendedor no encontrado.");
            return;
        }

        String saleId = "V-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        Sale sale = new Sale(saleId, customerOpt.get(), sellerOpt.get());

        boolean addingProducts = true;
        while (addingProducts) {
            System.out.print("Ingrese ID del producto a vender (o 'FIN' para terminar): ");
            String prodId = scanner.nextLine().trim();
            if (prodId.equalsIgnoreCase("FIN")) {
                break;
            }

            Product product = productService.findById(prodId);
            if (product == null) {
                System.out.println("Producto no encontrado.");
                continue;
            }

            System.out.printf("Producto seleccionado: %s | Disponible: %d%n", product.getTitle(), product.getAvailability());
            System.out.print("Cantidad: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            try {
                sale.addLineItem(product, qty);
                System.out.println("Producto agregado a la venta.");
            } catch (Exception e) {
                System.out.println("Error al agregar: " + e.getMessage());
            }

            System.out.print("¿Desea agregar otro producto? (s/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("s")) {
                addingProducts = false;
            }
        }

        saleService.processSale(sale);
        System.out.println("\n¡Venta registrada exitosamente!");
        System.out.println(sale);
    }

    private void showSalesHistory() {
        System.out.println("\n--- HISTORIAL DE VENTAS REGISTRADAS ---");
        List<Sale> sales = saleService.getAllSales();
        if (sales.isEmpty()) {
            // Fallback if personService wasn't linked inside SaleService
            List<Customer> customers = personService.listCustomers();
            List<Seller> sellers = personService.listSellers();
            List<Product> products = productService.listAllProducts();
            sales = saleService.listAllSales(customers, sellers, products);
        }

        if (sales.isEmpty()) {
            System.out.println("No se han registrado ventas todavía.");
            return;
        }
        for (Sale s : sales) {
            System.out.println(s);
        }
    }

    /**
     * Submenu for product return management operations.
     */
    private void handleReturnsMenu() {
        if (returnService == null) {
            System.out.println("El módulo de devoluciones no está configurado.");
            return;
        }

        int subOption = -1;
        do {
            System.out.println("\n=========================================");
            System.out.println("       GESTIÓN DE DEVOLUCIONES           ");
            System.out.println("=========================================");
            System.out.println("1. Registrar una nueva devolución");
            System.out.println("2. Consultar todas las devoluciones registradas");
            System.out.println("3. Consultar devoluciones por cliente");
            System.out.println("4. Consultar devoluciones por venta");
            System.out.println("5. Consultar el balance mensual");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                subOption = Integer.parseInt(scanner.nextLine().trim());
                switch (subOption) {
                    case 1 -> processNewReturn();
                    case 2 -> showAllReturns();
                    case 3 -> showReturnsByCustomer();
                    case 4 -> showReturnsBySale();
                    case 5 -> showMonthlyBalance();
                    case 0 -> System.out.println("Regresando al menú principal...");
                    default -> System.out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (subOption != 0);
    }

    /**
     * Guides the user through registering a new return transaction.
     */
    private void processNewReturn() {
        System.out.println("\n--- REGISTRAR NUEVA DEVOLUCIÓN ---");
        System.out.print("Ingrese el ID de la venta original: ");
        String saleId = scanner.nextLine().trim();

        Sale sale = saleService.findSaleById(saleId);
        if (sale == null) {
            System.out.println("Error: No se encontró ninguna venta con el ID '" + saleId + "'.");
            return;
        }

        if (!sale.canBeReturned()) {
            System.out.println("Error: La venta indicada supera los 30 días calendario permitidos para devoluciones.");
            return;
        }

        System.out.println("\nVenta encontrada:");
        System.out.printf("Fecha de venta: %s | Cliente: %s (ID: %s)%n",
                sale.getDate().toLocalDate(), sale.getCustomer().getName(), sale.getCustomer().getId());
        System.out.println("Productos pertenecientes a esta venta:");
        for (SalesLineItem item : sale.getItems()) {
            System.out.printf("  - [%s] %s | Cantidad comprada: %d | Precio U.: $%.2f%n",
                    item.getProduct().getId(), item.getProduct().getTitle(), item.getQuantity(), item.getUnitPrice());
        }

        List<String> productIdsToReturn = new ArrayList<>();
        boolean selecting = true;
        while (selecting) {
            System.out.print("\nIngrese el ID del producto a devolver (o 'FIN' para finalizar la selección): ");
            String pId = scanner.nextLine().trim();
            if (pId.equalsIgnoreCase("FIN")) {
                break;
            }

            boolean belongs = sale.getItems().stream()
                    .anyMatch(item -> item.getProduct().getId().equalsIgnoreCase(pId));
            if (!belongs) {
                System.out.println("Error: El producto indicado no pertenece a esta venta.");
                continue;
            }

            productIdsToReturn.add(pId);
            System.out.println("Producto añadido a la lista de devolución.");

            System.out.print("¿Desea devolver otro producto? (s/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("s")) {
                selecting = false;
            }
        }

        if (productIdsToReturn.isEmpty()) {
            System.out.println("Operación cancelada: No se seleccionó ningún producto para devolver.");
            return;
        }

        System.out.print("Ingrese el motivo de la devolución: ");
        String reason = scanner.nextLine().trim();
        if (reason.isEmpty()) {
            System.out.println("Error: El motivo de devolución no puede estar vacío.");
            return;
        }

        try {
            Return processedReturn = returnService.registerReturn(saleId, productIdsToReturn, reason);
            System.out.println("\n¡Devolución registrada exitosamente!");
            System.out.println(processedReturn.generateReturnReceipt());
        } catch (IllegalArgumentException e) {
            System.out.println("Error al procesar la devolución: " + e.getMessage());
        }
    }

    /**
     * Displays all return records in the system.
     */
    private void showAllReturns() {
        System.out.println("\n--- LISTADO DE TODAS LAS DEVOLUCIONES ---");
        List<Return> returns = returnService.viewAllReturns();
        if (returns.isEmpty()) {
            System.out.println("No hay devoluciones registradas en el sistema.");
            return;
        }
        for (Return r : returns) {
            System.out.println(r.generateReturnReceipt());
            System.out.println();
        }
    }

    /**
     * Prompts for a customer ID and displays associated returns.
     */
    private void showReturnsByCustomer() {
        System.out.println("\n--- CONSULTAR DEVOLUCIONES POR CLIENTE ---");
        System.out.print("Ingrese el ID del cliente: ");
        String customerId = scanner.nextLine().trim();

        List<Return> returns = returnService.viewReturnsByCustomer(customerId);
        if (returns.isEmpty()) {
            System.out.println("No se encontraron devoluciones asociadas al cliente con ID '" + customerId + "'.");
            return;
        }
        System.out.printf("Se encontraron %d devolución(es) para el cliente:%n", returns.size());
        for (Return r : returns) {
            System.out.println(r.generateReturnReceipt());
            System.out.println();
        }
    }

    /**
     * Prompts for a sale ID and displays associated returns.
     */
    private void showReturnsBySale() {
        System.out.println("\n--- CONSULTAR DEVOLUCIONES POR VENTA ---");
        System.out.print("Ingrese el ID de la venta: ");
        String saleId = scanner.nextLine().trim();

        List<Return> returns = returnService.viewReturnsBySale(saleId);
        if (returns.isEmpty()) {
            System.out.println("No se encontraron devoluciones asociadas a la venta con ID '" + saleId + "'.");
            return;
        }
        System.out.printf("Se encontraron %d devolución(es) para la venta:%n", returns.size());
        for (Return r : returns) {
            System.out.println(r.generateReturnReceipt());
            System.out.println();
        }
    }

    /**
     * Prompts for month and year, then displays total sales, total returns, and net monthly balance.
     */
    private void showMonthlyBalance() {
        System.out.println("\n--- CONSULTA DE BALANCE MENSUAL ---");
        try {
            System.out.print("Ingrese el mes (1-12): ");
            int month = Integer.parseInt(scanner.nextLine().trim());
            if (month < 1 || month > 12) {
                System.out.println("Error: El mes debe estar entre 1 y 12.");
                return;
            }

            System.out.print("Ingrese el año : ");
            int year = Integer.parseInt(scanner.nextLine().trim());

            double netBalance = returnService.generateMonthlyBalance(month, year);

            // Calculate components for detailed display
            double totalSales = saleService.getAllSales().stream()
                    .filter(s -> s.getDate().getYear() == year && s.getDate().getMonthValue() == month)
                    .mapToDouble(Sale::calculateTotal)
                    .sum();

            double totalReturns = returnService.viewAllReturns().stream()
                    .filter(r -> r.getReturnDate().getYear() == year && r.getReturnDate().getMonthValue() == month)
                    .mapToDouble(Return::getRefundAmount)
                    .sum();

            
            System.out.printf("     Balance Financiero" );
            
            System.out.printf("  (+) Total Ventas del Mes:       ", totalSales);
            System.out.printf("  (-) Total Devoluciones del Mes: ", totalReturns);
            System.out.println("-----------------------------------------");
            System.out.printf("  (=) BALANCE NETO:               ", netBalance);
            System.out.println("");
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese valores numéricos válidos para mes y año.");
        } catch (Exception e) {
            System.out.println("Error al calcular el balance: " + e.getMessage());
        }
    }
}