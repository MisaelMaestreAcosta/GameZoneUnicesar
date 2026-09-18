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

public class main {

    public static void main(String[] args) {
        // 1. Inicialización de Repositorios (Persistencia)
        ProductRepository productRepo = new ProductRepository();
        PersonRepository personRepo = new PersonRepository();
        SaleRepository saleRepo = new SaleRepository();
        WarrantyRepository warrantyRepo = new WarrantyRepository();

        // 2. Inyección de Dependencias en Servicios (Lógica de Negocio)
        ProductService productService = new ProductService(productRepo);
        PersonService personService = new PersonService(personRepo);
        WarrantyService warrantyService = new WarrantyService(warrantyRepo);
        SaleService saleService = new SaleService(saleRepo, productService, warrantyService);

        // 3. Inicio de la Capa de Presentación (UI)
        ConsoleMenu menu = new ConsoleMenu(productService, personService, saleService, null, warrantyService);
        menu.start();
    }
}