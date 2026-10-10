# GRASP Pattern Audit - Technical Leader

**Author:** Misael Andrés Maestre Acosta (Technical Leader)  
**Assigned Scope:** Sales Module, Console UI, System Startup, and Cross-Module Integration  
**Repository Branch:** `feature/grasp-audit`  
**Base Commit (develop):** `1c94b3f29fab36b46a814a4b8859881ac2e94a21`  

---

## 1. Audit Scope

In accordance with Section 4 of the Requirement 7 specification (*Responsabilidad sobre el código*), the Technical Leader is responsible for auditing the following classes and methods:

| Module / Requirement | Class / Artifact | Specific Methods & Scope Audited |
| :--- | :--- | :--- |
| **Sales, Interface & Startup** (Workshop 1) | `com.gamezone.model.Sale` | Complete class lifecycle, constructor, `addLineItem`, `calculateSubtotal`, `calculateTotal`, `validateSale`, `generateReceipt`. |
| | `com.gamezone.model.SalesLineItem` | Constructor, `calculateSubtotal`, getters, `toString`. |
| | `com.gamezone.persistence.SaleRepository` | Constructor, `ensureFileExists`, `save`, `loadAll`. |
| | `com.gamezone.service.SaleService` | Core constructors, `findById` (stub), `listAllSales`. |
| | `com.gamezone.ui.ConsoleMenu` | Core loop `start()`, `showProductCatalog`, `registerVideoGame`, `registerConsole`, `showPersons`, `registerCustomer`, `processNewSale`, `showSalesHistory`. |
| | `com.gamezone.main` | System bootstrap `main(String[] args)`, repository and service dependency wiring. |
| **Accessories Integration** (Requirement 1) | `com.gamezone.service.SaleService` | Integration of `AccessoryService` in `registerSale` (stock validation and decrement). |
| | `com.gamezone.ui.ConsoleMenu` | `showAccessoryMenu()`, accessory registration and query delegators. |
| **Promotions Integration** (Requirement 2) | `com.gamezone.service.SaleService` | Promotion application logic inside `registerSale` (`findBestPromotionFor`, discount calculation). |
| | `com.gamezone.model.Sale` | Applied discount fields (`appliedPromotionName`, `discountAmount`) and breakdown in `generateReceipt`. |
| | `com.gamezone.ui.ConsoleMenu` | `registerCategoryPromotion()`. |
| **Returns Integration** (Requirement 3) | `com.gamezone.service.ProductService` | Method `restoreStock(String, int)` (developed by Leader during integration). |
| | `com.gamezone.ui.ConsoleMenu` | `handleReturnsMenu()`, `processNewReturn()`, `showAllReturns()`, `showReturnsByCustomer()`, `showReturnsBySale()`, `showMonthlyBalance()`. |
| **Warranties Integration** (Requirement 4) | `com.gamezone.service.SaleService` | Basic and extended warranty assignment and cost calculation inside `registerSale`. |
| | `com.gamezone.ui.ConsoleMenu` | `showWarrantyMenu()` and warranty certificate queries. |
| **Exceptions & Validations** (Requirement 6) | `com.gamezone.ui.ConsoleMenu` | Differentiated exception handling across input options and submenus. |
