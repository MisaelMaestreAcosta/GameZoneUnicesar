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

import com.gamezone.service.WarrantyService;
import com.gamezone.model.Warranty;


import com.gamezone.model.Accessory;
import com.gamezone.service.AccessoryService;

import java.util.Arrays;


import com.gamezone.model.Accessory;
import com.gamezone.service.AccessoryService;

import java.util.Arrays;


import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Text-based console interface providing interactive menus for catalog, sales,
 * person management, product returns, and financial balance reporting.
 */
public class ConsoleMenu {

    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;

    private AccessoryService accessoryService;

    private final ReturnService returnService;

    private final WarrantyService warrantyService;
    private final Scanner scanner;

    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService, AccessoryService accessoryService, ReturnService returnService, WarrantyService warrantyService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.accessoryService = accessoryService;
        this.returnService = returnService;
        this.warrantyService = warrantyService;
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
            System.out.println("5. Registrar Nuevo Cliente");
            System.out.println("6. Registrar una Venta");
            System.out.println("7. Ver Historial de Ventas");
            System.out.println("8. Gestión de Accesorios");
            System.out.println("9. Gestión de Devoluciones");
            System.out.println("10. Gestión de Garantías");
            System.out.println("11. Consultar Balance Mensual");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine().trim());
                switch (option) {
                    case 1 -> showProductCatalog();
                    case 2 -> registerVideoGame();
                    case 3 -> registerConsole();
                    case 4 -> showPersons();
                    case 5 -> registerCustomer();
                    case 6 -> processNewSale();
                    case 7 -> showSalesHistory();
                    case 8 -> showAccessoryMenu();
                    case 9 -> handleReturnsMenu();
                    case 10 -> showWarrantyMenu();
                    case 11 -> showMonthlyBalance();
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

    private void registerCustomer() {
        System.out.println("\n--- REGISTRO DE CLIENTE ---");
        System.out.print("Nombre completo: ");
        String name = scanner.nextLine().trim();
        System.out.print("Documento / ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Teléfono: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Correo electrónico: ");
        String email = scanner.nextLine().trim();

        Customer customer = new Customer(name, id, phone, email);
        personService.registerCustomer(customer);
        System.out.println("¡Cliente registrado con éxito!");
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

        List<String> extendedWarrantyProducts = new ArrayList<>();
        boolean addingProducts = true;
        while (addingProducts) {
            System.out.print("Ingrese ID del producto a vender (o 'FIN' para terminar): ");
            String prodId = scanner.nextLine().trim();
            if (prodId.equalsIgnoreCase("FIN")) {
                break;
            }

            Product product = productService.findById(prodId);
            if (product == null && accessoryService != null) {
                product = accessoryService.findById(prodId);
            }
            if (product == null) {
                System.out.println("Producto o accesorio no encontrado.");
                continue;
            }

            System.out.printf("Producto seleccionado: %s | Disponible: %d%n", product.getTitle(), product.getAvailability());
            System.out.print("Cantidad: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            try {
                sale.addLineItem(product, qty);
                System.out.println("Producto agregado a la venta.");
                
                if (product instanceof com.gamezone.model.Console) {
                    System.out.print("Este producto es una consola. ¿Desea agregar Garantía Extendida? (s/n): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                        extendedWarrantyProducts.add(product.getId());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al agregar: " + e.getMessage());
            }

            System.out.print("¿Desea agregar otro producto? (s/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("s")) {
                addingProducts = false;
            }
        }

        saleService.registerSale(sale, extendedWarrantyProducts);
        System.out.println("\n¡Venta registrada exitosamente!");
        System.out.println(sale);
    }

    private List<Sale> getAllSalesInternal() {
        List<Customer> customers = personService.listCustomers();
        List<Seller> sellers = personService.listSellers();
        List<Product> products = productService.listAllProducts();
        return saleService.listAllSales(customers, sellers, products);
    }

    private void showSalesHistory() {
        System.out.println("\n--- HISTORIAL DE VENTAS REGISTRADAS ---");

        List<Sale> sales = getAllSalesInternal();
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


    private void showWarrantyMenu() {
        if (warrantyService == null) {
            System.out.println("Servicio de garantías no disponible.");
            return;
        }
        int option = -1;
        do {
            System.out.println("\n--- GESTIÓN DE GARANTÍAS ---");
            System.out.println("1. Consultar garantía por producto y venta");
            System.out.println("2. Listar todas las garantías registradas");
            System.out.println("3. Listar garantías vigentes hoy");
            System.out.println("4. Listar garantías próximas a vencer");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine().trim());
                switch (option) {
                    case 1 -> {
                        System.out.print("Ingrese ID del Producto: ");
                        String prodId = scanner.nextLine().trim();
                        System.out.print("Ingrese ID de la Venta: ");
                        String saleId = scanner.nextLine().trim();
                        Warranty w = warrantyService.findWarrantyByProduct(prodId, saleId);
                        if (w == null) {
                            System.out.println("No se encontró garantía para ese producto en esa venta.");
                        } else {
                            System.out.println(w.generateWarrantyCertificate());
                        }
                    }
                    case 2 -> {
                        List<Warranty> all = warrantyService.listAllWarranties();
                        if (all.isEmpty()) System.out.println("No hay garantías registradas.");
                        else all.forEach(w -> System.out.println(w.generateWarrantyCertificate()));
                    }
                    case 3 -> {
                        List<Warranty> active = warrantyService.listActiveWarranties();
                        if (active.isEmpty()) System.out.println("No hay garantías vigentes hoy.");
                        else active.forEach(w -> System.out.println(w.generateWarrantyCertificate()));
                    }
                    case 4 -> {
                        System.out.print("Ingrese días de anticipación: ");
                        int days = Integer.parseInt(scanner.nextLine().trim());
                        List<Warranty> expiring = warrantyService.listWarrantiesExpiringSoon(days);
                        if (expiring.isEmpty()) System.out.println("No hay garantías próximas a vencer en ese plazo.");
                        else expiring.forEach(w -> System.out.println(w.generateWarrantyCertificate()));
                    }
                    case 0 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (option != 0);
    }

    private void showAccessoryMenu() {
        int option = -1;
        do {
            System.out.println("\n--- GESTIÓN DE ACCESORIOS ---");
            System.out.println("1. Registrar un nuevo control");
            System.out.println("2. Registrar un nuevo cable");
            System.out.println("3. Registrar una nueva memoria");
            System.out.println("4. Listar todos los accesorios");
            System.out.println("5. Listar accesorios por tipo");
            System.out.println("6. Consultar accesorios compatibles con una consola");

            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine().trim());
                switch (option) {

                    case 1 -> registerController();
                    case 2 -> registerCable();
                    case 3 -> registerMemory();
                    case 4 -> listAllAccessories();
                    case 5 -> listAccessoriesByType();
                    case 6 -> listCompatibleAccessories();

                    case 0 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (option != 0);
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

    private void registerController() {
        System.out.println("\n--- REGISTRO DE CONTROL ---");
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Título: ");
        String title = scanner.nextLine().trim();
        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Stock inicial: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("¿Es inalámbrico? (true/false): ");
        boolean isWireless = Boolean.parseBoolean(scanner.nextLine().trim());

        System.out.print("Tipo de conexión (ej. Inalámbrico, USB): ");
        String connectionType = scanner.nextLine().trim();

        System.out.print("Consolas compatibles (IDs separados por coma): ");
        String consoles = scanner.nextLine().trim();
        List<String> compatibleConsoles = Arrays.asList(consoles.split("\\s*,\\s*"));

        if (accessoryService != null) {

            accessoryService.registerController(id, title, price, stock, isWireless ? "Wireless" : "Wired", compatibleConsoles);

            accessoryService.registerController(id, title, price, stock, connectionType, compatibleConsoles);

            System.out.println("¡Control registrado con éxito!");
        } else {
            System.out.println("Servicio de accesorios no disponible.");
        }
    }

    private void registerCable() {
        System.out.println("\n--- REGISTRO DE CABLE ---");
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Título: ");
        String title = scanner.nextLine().trim();
        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Stock inicial: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Longitud en metros (ej. 1.5): ");
        double length = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Tipo de conector (HDMI, USB, etc.): ");
        String connectorType = scanner.nextLine().trim();
        System.out.print("Consolas compatibles (IDs separados por coma): ");
        String consoles = scanner.nextLine().trim();
        List<String> compatibleConsoles = Arrays.asList(consoles.split("\\s*,\\s*"));

        if (accessoryService != null) {
            accessoryService.registerCable(id, title, price, stock, length, connectorType, compatibleConsoles);
            System.out.println("¡Cable registrado con éxito!");
        } else {
            System.out.println("Servicio de accesorios no disponible.");
        }
    }

    private void registerMemory() {
        System.out.println("\n--- REGISTRO DE MEMORIA ---");
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Título: ");
        String title = scanner.nextLine().trim();
        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Stock inicial: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Capacidad (GB): ");
        int capacity = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Tipo (SD, microSD, tarjeta interna): ");
        String type = scanner.nextLine().trim();
        System.out.print("Consolas compatibles (IDs separados por coma): ");
        String consoles = scanner.nextLine().trim();
        List<String> compatibleConsoles = Arrays.asList(consoles.split("\\s*,\\s*"));

        if (accessoryService != null) {
            accessoryService.registerMemory(id, title, price, stock, capacity, type, compatibleConsoles);
            System.out.println("¡Memoria registrada con éxito!");
        } else {
            System.out.println("Servicio de accesorios no disponible.");
        }
    }

    private void listAllAccessories() {
        if (accessoryService == null) {
            System.out.println("Servicio de accesorios no disponible.");
            return;
        }
        System.out.println("\n--- TODOS LOS ACCESORIOS ---");
        List<Accessory> accessories = accessoryService.listAllAccessories();
        if (accessories == null || accessories.isEmpty()) {
            System.out.println("No hay accesorios registrados.");
            return;
        }
        for (Accessory a : accessories) {
            System.out.printf("[%s] %s | Stock: %d | Precio: $%.2f%n",
                    a.getId(), a.getTitle(), a.getAvailability(), a.getPrice());
        }
    }

    private void listAccessoriesByType() {
        if (accessoryService == null) {
            System.out.println("Servicio de accesorios no disponible.");
            return;
        }
        System.out.print("Ingrese el tipo de accesorio (Control, Cable, Memoria): ");
        String type = scanner.nextLine().trim();
        System.out.println("\n--- ACCESORIOS TIPO: " + type.toUpperCase() + " ---");
        List<Accessory> accessories = accessoryService.listAccessoriesByType(type);
        if (accessories == null || accessories.isEmpty()) {
            System.out.println("No hay accesorios de este tipo.");
            return;
        }
        for (Accessory a : accessories) {
            System.out.printf("[%s] %s | Stock: %d | Precio: $%.2f%n",
                    a.getId(), a.getTitle(), a.getAvailability(), a.getPrice());
        }
    }

    private void listCompatibleAccessories() {
        if (accessoryService == null) {
            System.out.println("Servicio de accesorios no disponible.");
            return;
        }
        System.out.print("Ingrese el ID de la consola: ");
        String consoleId = scanner.nextLine().trim();
        System.out.println("\n--- ACCESORIOS COMPATIBLES CON: " + consoleId + " ---");
        List<Accessory> accessories = accessoryService.findAccessoriesCompatibleWith(consoleId);
        if (accessories == null || accessories.isEmpty()) {
            System.out.println("No se encontraron accesorios compatibles.");
            return;
        }
        for (Accessory a : accessories) {
            System.out.printf("[%s] %s | Stock: %d | Precio: $%.2f%n",
                    a.getId(), a.getTitle(), a.getAvailability(), a.getPrice());
        }
    }


    /**
     * Guides the user through registering a new return transaction.
     */
    private void processNewReturn() {
        System.out.println("\n--- REGISTRAR NUEVA DEVOLUCIÓN ---");
        System.out.print("Ingrese el ID de la venta original: ");
        String saleId = scanner.nextLine().trim();


        Sale sale = getAllSalesInternal().stream().filter(s -> s.getId().equals(saleId)).findFirst().orElse(null);

        if (sale == null) {
            System.out.println("Error: No se encontró ninguna venta con el ID '" + saleId + "'.");
            return;
        }


        // TODO: Uncomment when canBeReturned is implemented by Desarrollador 1
        // if (!sale.canBeReturned()) {
        //     System.out.println("Error: La venta indicada supera los 30 días calendario permitidos para devoluciones.");
        //     return;
        // }

        long daysSinceSale = java.time.temporal.ChronoUnit.DAYS.between(sale.getDate().toLocalDate(), java.time.LocalDate.now());
        if (daysSinceSale > 30) {
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

            List<Sale> allSales = saleService.listAllSales(personService.listCustomers(), personService.listSellers(), productService.listAllProducts());
            double totalSales = allSales.stream()



                    .filter(s -> s.getDate().getYear() == year && s.getDate().getMonthValue() == month)
                    .mapToDouble(Sale::calculateTotal)
                    .sum();

            double totalReturns = returnService.viewAllReturns().stream()
                    .filter(r -> r.getReturnDate().getYear() == year && r.getReturnDate().getMonthValue() == month)
                    .mapToDouble(Return::getRefundAmount)
                    .sum();

            System.out.println("     Balance Financiero");

            System.out.printf("  (+) Total Ventas del Mes:       $%.2f%n", totalSales);
            System.out.printf("  (-) Total Devoluciones del Mes: $%.2f%n", totalReturns);
            System.out.println("-----------------------------------------");
            System.out.printf("  (=) BALANCE NETO:               $%.2f%n", netBalance);

            System.out.println("");

            System.out.println();

        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese valores numéricos válidos para mes y año.");
        } catch (Exception e) {
            System.out.println("Error al calcular el balance: " + e.getMessage());

        }
    }
}
