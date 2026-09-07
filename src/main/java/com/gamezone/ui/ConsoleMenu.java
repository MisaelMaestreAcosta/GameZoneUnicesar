package com.gamezone.ui;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleMenu {

    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;
    private final Scanner scanner;

    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
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
}