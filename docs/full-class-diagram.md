
```mermaid
classDiagram
    %% ============================================
    %% MODEL LAYER - PERSON HIERARCHY
    %% ============================================
    
    class Person {
        <<abstract>>
        - String id
        - String name
        - String phoneNumber
        --
        + Person(id, name, phoneNumber)
        + String getId()
        + String getName()
        + String getPhoneNumber()
        + String getContactInfo()
        + String getRoleDescription()*
    }
    
    class Customer {
        - String email
        - List~Sale~ purchaseHistory
        --
        + Customer(id, name, phoneNumber, email, purchaseHistory)
        + String getEmail()
        + List~Sale~ getPurchaseHistory()
        + void addPurchaseToHistory(Sale)
        + String getRoleDescription()
    }
    
    class Seller {
        - String employeeCode
        - String workShift
        --
        + Seller(id, name, phoneNumber, employeeCode, workShift)
        + String getEmployeeCode()
        + String getWorkShift()
        + String getRoleDescription()
    }
    
    %% ============================================
    %% MODEL LAYER - PRODUCT HIERARCHY
    %% ============================================
    
    class Product {
        <<abstract>>
        - String id
        - String title
        - double price
        - int stockQuantity
        --
        + Product(id, title, price, stockQuantity)
        + String getId()
        + String getTitle()
        + double getPrice()
        + int getStockQuantity()
        + void setStockQuantity(int)
        + void reduceStock(int)
        + String getDescription()*
    }
    
    class VideoGame {
        - String platform
        - String genre
        - String ageRating
        --
        + VideoGame(id, title, price, stockQuantity, platform, genre, ageRating)
        + String getPlatform()
        + String getGenre()
        + String getAgeRating()
        + String getDescription()
    }
    
    class Console {
        - String brand
        - String model
        - String generation
        --
        + Console(id, title, price, stockQuantity, brand, model, generation)
        + String getBrand()
        + String getModel()
        + String getGeneration()
        + String getDescription()
    }
    
    %% ============================================
    %% MODEL LAYER - ACCESSORY HIERARCHY (REQ 1)
    %% ============================================
    
    class Accessory {
        <<abstract>>
        - String id
        - String title
        - double price
        - int stockQuantity
        - List~Console~ compatibleConsoles
        --
        + Accessory(id, title, price, stockQuantity)
        + String getId()
        + String getTitle()
        + double getPrice()
        + int getStockQuantity()
        + void setStockQuantity(int)
        + void reduceStock(int)
        + List~Console~ getCompatibleConsoles()
        + void addCompatibleConsole(Console)
        + boolean isCompatibleWith(Console)
        + String getDescription()*
    }
    
    class Controller {
        - String connectionType
        --
        + Controller(id, title, price, stockQuantity, connectionType)
        + String getConnectionType()
        + String getDescription()
    }
    
    class Cable {
        - double lengthInMeters
        - String connectorType
        --
        + Cable(id, title, price, stockQuantity, lengthInMeters, connectorType)
        + double getLengthInMeters()
        + String getConnectorType()
        + String getDescription()
    }
    
    class Memory {
        - int capacityInGB
        - String memoryType
        --
        + Memory(id, title, price, stockQuantity, capacityInGB, memoryType)
        + int getCapacityInGB()
        + String getMemoryType()
        + String getDescription()
    }
    
    %% ============================================
    %% MODEL LAYER - PROMOTION HIERARCHY (REQ 2)
    %% ============================================
    
    class Promotion {
        <<abstract>>
        - String id
        - String name
        - LocalDate startDate
        - LocalDate endDate
        --
        + Promotion(id, name, startDate, endDate)
        + String getId()
        + String getName()
        + LocalDate getStartDate()
        + LocalDate getEndDate()
        + boolean isActive(LocalDate date)
        + double calculateDiscount(Sale sale)*
    }
    
    class PercentageDiscount {
        - double percentage
        --
        + PercentageDiscount(id, name, startDate, endDate, percentage)
        + double getPercentage()
        + void setPercentage(double)
        + double calculateDiscount(Sale sale)
    }
    
    class CategoryDiscount {
        - double percentage
        - String targetCategory
        --
        + CategoryDiscount(id, name, startDate, endDate, percentage, targetCategory)
        + double getPercentage()
        + String getTargetCategory()
        + double calculateDiscount(Sale sale)
    }
    
    class BulkPurchaseDiscount {
        - int minimumQuantity
        - double percentage
        --
        + BulkPurchaseDiscount(id, name, startDate, endDate, minimumQuantity, percentage)
        + int getMinimumQuantity()
        + double getPercentage()
        + double calculateDiscount(Sale sale)
    }
    
    %% ============================================
    %% MODEL LAYER - WARRANTY HIERARCHY (REQ 4)
    %% ============================================
    
    class Warranty {
        <<abstract>>
        - String id
        - Product product
        - Sale sale
        - LocalDate startDate
        - LocalDate endDate
        --
        + Warranty(id, product, sale, startDate)
        + String getId()
        + Product getProduct()
        + Sale getSale()
        + LocalDate getStartDate()
        + LocalDate getEndDate()
        + int getDurationInMonths()*
        + String getWarrantyType()*
        + double getAdditionalCost()*
        + boolean isActive(LocalDate date)
        + String generateWarrantyCertificate()
    }
    
    class BasicWarranty {
        --
        + BasicWarranty(id, product, sale, startDate)
        + int getDurationInMonths()
        + String getWarrantyType()
        + double getAdditionalCost()
    }
    
    class ExtendedWarranty {
        --
        + ExtendedWarranty(id, product, sale, startDate)
        + int getDurationInMonths()
        + String getWarrantyType()
        + double getAdditionalCost()
    }
    
    %% ============================================
    %% MODEL LAYER - SALE CLASS
    %% ============================================
    
    class Sale {
        - String id
        - LocalDateTime date
        - Customer customer
        - Seller seller
        - List~Product~ products
        - List~Accessory~ accessories
        - String appliedPromotionName
        - double discountAmount
        --
        + Sale(id, date, customer, seller, products, accessories)
        + String getId()
        + LocalDateTime getDate()
        + Customer getCustomer()
        + Seller getSeller()
        + List~Product~ getProducts()
        + List~Accessory~ getAccessories()
        + String getAppliedPromotionName()
        + void setAppliedPromotionName(String)
        + double getDiscountAmount()
        + void setDiscountAmount(double)
        + double calculateSubtotal()
        + double calculateTotal()
        + void addProduct(Product)
        + void addAccessory(Accessory)
        + int getItemCount()
        + boolean canBeReturned()
        + String generateReceipt()
    }
    
    %% ============================================
    %% MODEL LAYER - RETURN CLASS (REQ 3)
    %% ============================================
    
    class Return {
        - String id
        - LocalDate date
        - Sale originalSale
        - List~Product~ returnedProducts
        - String reason
        - double refundAmount
        --
        + Return(id, date, originalSale, returnedProducts, reason)
        + String getId()
        + LocalDate getDate()
        + Sale getOriginalSale()
        + List~Product~ getReturnedProducts()
        + String getReason()
        + double getRefundAmount()
        + double calculateRefundAmount()
        + String generateReturnReceipt()
    }
    
    %% ============================================
    %% PERSISTENCE LAYER
    %% ============================================
    
    class PersonRepository {
        - String CUSTOMER_FILE
        - String SELLER_FILE
        --
        + PersonRepository()
        + void saveCustomers(List~Customer~)
        + List~Customer~ loadCustomers()
        + void saveSellers(List~Seller~)
        + List~Seller~ loadSellers()
    }
    
    class ProductRepository {
        - String VIDEOGAME_FILE
        - String CONSOLE_FILE
        --
        + ProductRepository()
        + void saveVideoGames(List~VideoGame~)
        + List~VideoGame~ loadVideoGames()
        + void saveConsoles(List~Console~)
        + List~Console~ loadConsoles()
    }
    
    class AccessoryRepository {
        - String ACCESSORY_FILE
        --
        + AccessoryRepository()
        + void saveAll(List~Accessory~)
        + List~Accessory~ loadAll()
        + void saveControllers(List~Controller~)
        + List~Controller~ loadControllers()
        + void saveCables(List~Cable~)
        + List~Cable~ loadCables()
        + void saveMemories(List~Memory~)
        + List~Memory~ loadMemories()
    }
    
    class PromotionRepository {
        - String PROMOTION_FILE
        --
        + PromotionRepository()
        + void saveAll(List~Promotion~)
        + List~Promotion~ loadAll()
    }
    
    class WarrantyRepository {
        - String WARRANTY_FILE
        --
        + WarrantyRepository()
        + void saveAll(List~Warranty~)
        + List~Warranty~ loadAll(List~Sale~ sales, List~Product~ products)
    }
    
    class SaleRepository {
        - String SALE_FILE
        --
        + SaleRepository()
        + void saveAll(List~Sale~)
        + List~Sale~ loadAll()
        + void saveSale(Sale)
        + void deleteSale(String)
    }
    
    class ReturnRepository {
        - String RETURN_FILE
        --
        + ReturnRepository()
        + void saveAll(List~Return~)
        + List~Return~ loadAll(List~Sale~ sales, List~Product~ products)
    }
    
    %% ============================================
    %% SERVICE LAYER
    %% ============================================
    
    class PersonService {
        - PersonRepository repository
        - List~Customer~ customers
        - List~Seller~ sellers
        --
        + PersonService()
        + void registerCustomer(Customer)
        + List~Customer~ getAllCustomers()
        + List~Seller~ getAllSellers()
        + Customer findCustomerById(String)
        + Seller findSellerById(String)
        + void loadData()
        + void saveData()
    }
    
    class ProductService {
        - ProductRepository repository
        - List~Product~ products
        --
        + ProductService()
        + void registerVideoGame(VideoGame)
        + void registerConsole(Console)
        + List~Product~ getAllProducts()
        + Product findProductById(String)
        + void updateStock(String, int)
        + boolean isStockAvailable(String, int)
        + void restoreStock(String, int)
        + void loadData()
        + void saveData()
    }
    
    class AccessoryService {
        - AccessoryRepository repository
        - List~Accessory~ accessories
        - ProductService productService
        --
        + AccessoryService(ProductService)
        + void registerController(Controller)
        + void registerCable(Cable)
        + void registerMemory(Memory)
        + List~Accessory~ getAllAccessories()
        + List~Accessory~ getAccessoriesByType(String)
        + List~Accessory~ getCompatibleAccessories(String consoleId)
        + Accessory findAccessoryById(String)
        + void updateStock(String, int)
        + boolean isStockAvailable(String, int)
        + void loadData()
        + void saveData()
    }
    
    class PromotionService {
        - PromotionRepository repository
        - List~Promotion~ promotions
        --
        + PromotionService()
        + void registerPercentageDiscount(...)
        + void registerCategoryDiscount(...)
        + void registerBulkPurchaseDiscount(...)
        + List~Promotion~ listAllPromotions()
        + List~Promotion~ listActivePromotions()
        + Promotion findBestPromotionFor(Sale sale)
        + Promotion findById(String id)
        + void loadData()
        + void saveData()
    }
    
    class WarrantyService {
        - WarrantyRepository repository
        - List~Warranty~ warranties
        --
        + WarrantyService()
        + BasicWarranty assignBasicWarranty(Product, Sale, LocalDate)
        + ExtendedWarranty assignExtendedWarranty(Product, Sale, LocalDate)
        + Warranty findWarrantyByProduct(String, String)
        + List~Warranty~ listAllWarranties()
        + List~Warranty~ listActiveWarranties()
        + List~Warranty~ listWarrantiesExpiringSoon(int)
        + void loadData(List~Sale~ sales, List~Product~ products)
        + void saveData()
    }
    
    class SaleService {
        - SaleRepository repository
        - PersonService personService
        - ProductService productService
        - AccessoryService accessoryService
        - PromotionService promotionService
        - WarrantyService warrantyService
        - List~Sale~ sales
        --
        + SaleService(PersonService, ProductService, AccessoryService, PromotionService, WarrantyService)
        + void registerSale(Sale, List~String~ extendedWarrantyProductIds)
        + List~Sale~ getAllSales()
        + List~Sale~ getSalesByCustomer(String)
        + List~Sale~ getSalesBySeller(String)
        + Sale findSaleById(String)
        + double calculateTotal(Sale)
        + void loadData()
        + void saveData()
    }
    
    class ReturnService {
        - ReturnRepository repository
        - SaleService saleService
        - ProductService productService
        - List~Return~ returns
        --
        + ReturnService(ReturnRepository, SaleService, ProductService)
        + Return registerReturn(String saleId, List~String~ productIds, String reason)
        + List~Return~ viewAllReturns()
        + List~Return~ viewReturnsByCustomer(String customerId)
        + List~Return~ viewReturnsBySale(String saleId)
        + double generateMonthlyBalance(int month, int year)
        + void loadData(List~Sale~ sales, List~Product~ products)
        + void saveData()
    }
    
    %% ============================================
    %% UI LAYER
    %% ============================================
    
    class ConsoleMenu {
        - PersonService personService
        - ProductService productService
        - AccessoryService accessoryService
        - PromotionService promotionService
        - WarrantyService warrantyService
        - SaleService saleService
        - ReturnService returnService
        - Scanner scanner
        --
        + ConsoleMenu(...)
        + void start()
        - void showMainMenu()
        - void handleProductMenu()
        - void handlePersonMenu()
        - void handleAccessoryMenu()
        - void handlePromotionMenu()
        - void handleWarrantyMenu()
        - void handleSaleMenu()
        - void handleReturnMenu()
        - void handleReportsMenu()
        - void registerVideoGame()
        - void registerConsole()
        - void listAllProducts()
        - void registerCustomer()
        - void listAllCustomers()
        - void listAllSellers()
        - void registerController()
        - void registerCable()
        - void registerMemory()
        - void listAllAccessories()
        - void listAccessoriesByType()
        - void listCompatibleAccessories()
        - void registerPercentageDiscount()
        - void registerCategoryDiscount()
        - void registerBulkPurchaseDiscount()
        - void listAllPromotions()
        - void listActivePromotions()
        - void consultWarrantyByProduct()
        - void listAllWarranties()
        - void listActiveWarranties()
        - void listWarrantiesExpiringSoon()
        - void registerSale()
        - void showAllSales()
        - void showCustomerHistory()
        - void showSellerHistory()
        - void registerReturn()
        - void showAllReturns()
        - void showReturnsByCustomer()
        - void showReturnsBySale()
        - void showMonthlyBalance()
    }
    
    %% ============================================
    %% MAIN CLASS
    %% ============================================
    
    class Main {
        + Main()
        + void main(String[] args)
    }
    
    %% ============================================
    %% INHERITANCE RELATIONSHIPS
    %% ============================================
    
    Customer --|> Person
    Seller --|> Person
    VideoGame --|> Product
    Console --|> Product
    Controller --|> Accessory
    Cable --|> Accessory
    Memory --|> Accessory
    PercentageDiscount --|> Promotion
    CategoryDiscount --|> Promotion
    BulkPurchaseDiscount --|> Promotion
    BasicWarranty --|> Warranty
    ExtendedWarranty --|> Warranty
    
    %% ============================================
    %% ASSOCIATION RELATIONSHIPS
    %% ============================================
    
    Sale --> Customer : 1
    Sale --> Seller : 1
    Sale --> Product : 1..*
    Sale --> Accessory : 0..*
    
    Customer --> Sale : 0..*
    Seller --> Sale : 0..*
    
    Accessory --> Console : 0..* compatible
    
    Return --> Sale : 1
    Return --> Product : 1..*
    
    Warranty --> Product : 1
    Warranty --> Sale : 1
    Product --> Warranty : 0..*
    Sale --> Warranty : 0..*
    
    Promotion --> Sale : 0..* applies to
    
    %% ============================================
    %% DEPENDENCY RELATIONSHIPS - SERVICE LAYER
    %% ============================================
    
    PersonService --> PersonRepository
    PersonService --> Person
    PersonService --> Customer
    PersonService --> Seller
    
    ProductService --> ProductRepository
    ProductService --> Product
    ProductService --> VideoGame
    ProductService --> Console
    
    AccessoryService --> AccessoryRepository
    AccessoryService --> Accessory
    AccessoryService --> Controller
    AccessoryService --> Cable
    AccessoryService --> Memory
    AccessoryService --> ProductService
    
    PromotionService --> PromotionRepository
    PromotionService --> Promotion
    PromotionService --> PercentageDiscount
    PromotionService --> CategoryDiscount
    PromotionService --> BulkPurchaseDiscount
    PromotionService --> Sale
    
    WarrantyService --> WarrantyRepository
    WarrantyService --> Warranty
    WarrantyService --> BasicWarranty
    WarrantyService --> ExtendedWarranty
    WarrantyService --> Product
    WarrantyService --> Sale
    
    SaleService --> SaleRepository
    SaleService --> PersonService
    SaleService --> ProductService
    SaleService --> AccessoryService
    SaleService --> PromotionService
    SaleService --> WarrantyService
    SaleService --> Sale
    
    ReturnService --> ReturnRepository
    ReturnService --> SaleService
    ReturnService --> ProductService
    ReturnService --> Return
    
    %% ============================================
    %% DEPENDENCY RELATIONSHIPS - UI LAYER
    %% ============================================
    
    ConsoleMenu --> PersonService
    ConsoleMenu --> ProductService
    ConsoleMenu --> AccessoryService
    ConsoleMenu --> PromotionService
    ConsoleMenu --> WarrantyService
    ConsoleMenu --> SaleService
    ConsoleMenu --> ReturnService
    
    Main --> ConsoleMenu
    Main --> PersonService
    Main --> ProductService
    Main --> AccessoryService
    Main --> PromotionService
    Main --> WarrantyService
    Main --> SaleService
    Main --> ReturnService
```