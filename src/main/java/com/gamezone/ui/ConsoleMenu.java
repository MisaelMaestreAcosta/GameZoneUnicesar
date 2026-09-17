package com.gamezone.ui;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.service.PersonService;
import com.gamezone.service.SaleService;
import com.gamezone.model.Promotion;
import com.gamezone.service.PromotionService;

import com.gamezone.model.Accessory;
import com.gamezone.service.AccessoryService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConsoleMenu {

    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;
    private AccessoryService accessoryService;
    private PromotionService promotionService;
    private final Scanner scanner;

    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.scanner = new Scanner(System.in);
    }

    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService, PromotionService promotionService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.promotionService = promotionService;
        this.scanner = new Scanner(System.in);
    }

    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService, AccessoryService accessoryService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.accessoryService = accessoryService;
        this.scanner = new Scanner(System.in);
    }

    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService, AccessoryService accessoryService, PromotionService promotionService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
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
            System.out.println("7. Gestión de Accesorios");
            System.out.println("8. Gestión de Promociones");
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
                    case 7 -> showAccessoryMenu();
                    case 8 -> showPromotionMenu();
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
        List<Customer> customers = personService.listCustomers();
        List<Seller> sellers = personService.listSellers();
        List<Product> products = productService.listAllProducts();

        List<Sale> sales = saleService.listAllSales(customers, sellers, products);
        if (sales.isEmpty()) {
            System.out.println("No se han registrado ventas todavía.");
            return;
        }
        for (Sale s : sales) {
            System.out.println(s);
        }
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
        System.out.print("Consolas compatibles (IDs separados por coma): ");
        String consoles = scanner.nextLine().trim();
        List<String> compatibleConsoles = Arrays.asList(consoles.split("\\s*,\\s*"));

        if (accessoryService != null) {
            accessoryService.registerController(id, title, price, stock, isWireless, compatibleConsoles);
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
        List<Accessory> accessories = accessoryService.listCompatibleAccessories(consoleId);
        if (accessories == null || accessories.isEmpty()) {
            System.out.println("No se encontraron accesorios compatibles.");
            return;
        }
        for (Accessory a : accessories) {
            System.out.printf("[%s] %s | Stock: %d | Precio: $%.2f%n",
                    a.getId(), a.getTitle(), a.getAvailability(), a.getPrice());
        }
    }

    private void showPromotionMenu() {
        if (promotionService == null) {
            System.out.println("Servicio de promociones no disponible.");
            return;
        }
        int option = -1;
        do {
            System.out.println("\n--- GESTIÓN DE PROMOCIONES ---");
            System.out.println("1. Registrar una nueva promoción de tipo porcentaje");
            System.out.println("2. Registrar una nueva promoción de tipo categoría");
            System.out.println("3. Registrar una nueva promoción de tipo volumen");
            System.out.println("4. Listar todas las promociones registradas");
            System.out.println("5. Listar solo las promociones vigentes en la fecha actual");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine().trim());
                switch (option) {
                    case 1 -> registerPercentageDiscount();
                    case 2 -> registerCategoryDiscount();
                    case 3 -> registerBulkPurchaseDiscount();
                    case 4 -> listAllPromotions();
                    case 5 -> listActivePromotions();
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

    private void registerPercentageDiscount() {
        System.out.println("\n--- REGISTRO DE PROMOCIÓN (PORCENTAJE) ---");
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nombre: ");
        String name = scanner.nextLine().trim();
        System.out.print("Fecha inicio (yyyy-MM-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.print("Fecha fin (yyyy-MM-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.print("Porcentaje de descuento (0-100): ");
        double percentage = Double.parseDouble(scanner.nextLine().trim());

        promotionService.registerPercentageDiscount(id, name, startDate, endDate, percentage);
        System.out.println("¡Promoción registrada con éxito!");
    }

    private void registerCategoryDiscount() {
        System.out.println("\n--- REGISTRO DE PROMOCIÓN (CATEGORÍA) ---");
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nombre: ");
        String name = scanner.nextLine().trim();
        System.out.print("Fecha inicio (yyyy-MM-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.print("Fecha fin (yyyy-MM-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.print("Porcentaje de descuento (0-100): ");
        double percentage = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Categoría objetivo (VIDEOGAME/CONSOLE): ");
        String targetCategory = scanner.nextLine().trim().toUpperCase();

        promotionService.registerCategoryDiscount(id, name, startDate, endDate, percentage, targetCategory);
        System.out.println("¡Promoción registrada con éxito!");
    }

    private void registerBulkPurchaseDiscount() {
        System.out.println("\n--- REGISTRO DE PROMOCIÓN (VOLUMEN) ---");
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nombre: ");
        String name = scanner.nextLine().trim();
        System.out.print("Fecha inicio (yyyy-MM-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.print("Fecha fin (yyyy-MM-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.print("Cantidad mínima: ");
        int minQty = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Porcentaje de descuento (0-100): ");
        double percentage = Double.parseDouble(scanner.nextLine().trim());

        promotionService.registerBulkPurchaseDiscount(id, name, startDate, endDate, minQty, percentage);
        System.out.println("¡Promoción registrada con éxito!");
    }

    private void listAllPromotions() {
        System.out.println("\n--- TODAS LAS PROMOCIONES ---");
        List<Promotion> promotions = promotionService.listAllPromotions();
        if (promotions == null || promotions.isEmpty()) {
            System.out.println("No hay promociones registradas.");
            return;
        }
        for (Promotion p : promotions) {
            System.out.printf("[%s] %s | Vigencia: %s a %s%n",
                    p.getId(), p.getName(), p.getStartDate(), p.getEndDate());
        }
    }

    private void listActivePromotions() {
        System.out.println("\n--- PROMOCIONES VIGENTES HOY ---");
        List<Promotion> promotions = promotionService.listActivePromotions();
        if (promotions == null || promotions.isEmpty()) {
            System.out.println("No hay promociones vigentes el día de hoy.");
            return;
        }
        for (Promotion p : promotions) {
            System.out.printf("[%s] %s | Vigencia: %s a %s%n",
                    p.getId(), p.getName(), p.getStartDate(), p.getEndDate());
        }
    }
}