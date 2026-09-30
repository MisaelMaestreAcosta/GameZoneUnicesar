# Layers Diagram

```mermaid
graph TD
    subgraph UI["UI Layer"]
        UI_Classes["ConsoleMenu, Main"]
    end
    
    subgraph Service["Service Layer"]
        Service_Classes["PersonService, ProductService, SaleService, AccessoryService, PromotionService, WarrantyService, ReturnService"]
    end
    
    subgraph Persistence["Persistence Layer"]
        Persistence_Classes["PersonRepository, ProductRepository, SaleRepository, AccessoryRepository, PromotionRepository, WarrantyRepository, ReturnRepository"]
    end
    
    subgraph Model["Model Layer"]
        Model_Classes["Person, Customer, Seller, Product, VideoGame, Console, Accessory, Sale, Promotion, Warranty, Return"]
    end
    
    UI --> Service
    Service --> Persistence
    Service --> Model
    Persistence --> Model
```