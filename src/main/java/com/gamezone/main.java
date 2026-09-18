package com.gamezone;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;
import com.gamezone.ui.ConsoleMenu;
import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.ReturnService;

public class main {

    public static void main(String[] args) {
        // 1. Inicialización de Repositorios (Persistencia)
        ProductRepository productRepo = new ProductRepository();
        PersonRepository personRepo = new PersonRepository();
        SaleRepository saleRepo = new SaleRepository();
        AccessoryRepository accessoryRepo = new AccessoryRepository();
        // 2. Inyección de Dependencias en Servicios (Lógica de Negocio)
        ProductService productService = new ProductService(productRepo);
        PersonService personService = new PersonService(personRepo);
<<<<<<< HEAD
        
        SaleService saleService = new SaleService(saleRepo, productService, null);
        
        WarrantyRepository warrantyRepo = new WarrantyRepository(saleService, productService);
        WarrantyService warrantyService = new WarrantyService(warrantyRepo);
        
        saleService.setWarrantyService(warrantyService);

        // 3. Inicio de la Capa de Presentación (UI)
        ConsoleMenu menu = new ConsoleMenu(productService, personService, saleService, null, warrantyService);
=======
        SaleService saleService = new SaleService(saleRepo, productService);
        AccessoryService accessoryService = new AccessoryService(accessoryRepo);
        
        ReturnRepository returnRepo = new ReturnRepository(saleService, productService, personService);
        ReturnService returnService = new ReturnService(returnRepo, saleService, productService, personService);

        // 3. Inicio de la Capa de Presentación (UI)
        ConsoleMenu menu = new ConsoleMenu(productService, personService, saleService, accessoryService, returnService);
>>>>>>> origin/develop
        menu.start();
    }
}