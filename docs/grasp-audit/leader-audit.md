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

---

## 2. Evaluation of GRASP Patterns

### 2.1 Information Expert

* **Verdict:** **Violation**

#### Well-Applied Evidence
* `com.gamezone.model.Sale.calculateSubtotal()` (lines 61–67) and `calculateTotal()` (lines 74–76): `Sale` holds the collection of `SalesLineItem`, the discount amount, and warranty costs. It properly aggregates subtotals and delegates item price computation to `SalesLineItem.calculateSubtotal()` (lines 30–32), which possesses the item's unit price and quantity.

#### Identified Violations

| Field | Finding L-V01 | Finding L-V02 |
| :--- | :--- | :--- |
| **ID** | `L-V01` | `L-V02` |
| **Pattern** | Information Expert | Information Expert |
| **Module** | Returns Integration (Requirement 3) | Returns Integration & Interface (Requirement 3) |
| **Evidence** | `ConsoleMenu.java`, method `processNewReturn()`, lines 614–618: <br>`long daysSinceSale = java.time.temporal.ChronoUnit.DAYS.between(sale.getDate().toLocalDate(), java.time.LocalDate.now());`<br>`if (daysSinceSale > 30) { ... }` | `ConsoleMenu.java`, method `showMonthlyBalance()`, lines 752–765: <br>`List<Sale> allSales = saleService.listAllSales(...);`<br>`double totalSales = allSales.stream().filter(s -> s.getDate().getYear() == year && s.getDate().getMonthValue() == month).mapToDouble(Sale::calculateTotal).sum();`<br>`double totalReturns = returnService.viewAllReturns().stream().filter(...).mapToDouble(Return::getRefundAmount).sum();` |
| **Explanation** | The UI class (`ConsoleMenu`) calculates whether a sale is within the 30-day return policy window. The information expert for evaluating whether a sale can be returned is the domain entity `Sale` (via `Sale.canBeReturned()`) or `ReturnService`. | `ConsoleMenu` performs reporting aggregations, streams filtering, and summation of monthly sales and return totals directly in the presentation layer, instead of delegating to `ReturnService` or a dedicated reporting service. |
| **Consequence** | Business rules are leaked into the presentation layer. If the return window policy changes (e.g., from 30 days to 15 or 60 days), UI code must be altered, causing potential inconsistencies if another interface (web, API) is introduced. | Code duplication and presentation bloat. Any modification to financial balance calculation requires editing the console menu. |
| **Proposed Solution** | Delegate return eligibility validation directly to `Sale.canBeReturned()` or to `ReturnService.registerReturn()`, removing date math from `ConsoleMenu`. | Encapsulate the monthly balance breakdown (total sales, total refunds, net balance) in a domain/DTO object returned directly by `ReturnService.generateMonthlyBalanceSummary()`. |
| **Other Member's Code** | Requires Developer 1 to finalize `Sale.canBeReturned()` logic and Developer 2 for `ReturnService`. | Touches `ReturnService` (Developer 2). |

---

### 2.2 Creator

* **Verdict:** **Violation**

#### Well-Applied Evidence
* `com.gamezone.model.Sale.addLineItem(Product, int)` (lines 49–54): `Sale` aggregates, records, and closely uses `SalesLineItem`. Hence, `Sale` directly creates instances of `SalesLineItem`: `this.items.add(new SalesLineItem(product, quantity));`.

#### Identified Violations

| Field | Finding L-V03 |
| :--- | :--- |
| **ID** | `L-V03` |
| **Pattern** | Creator |
| **Module** | Sales, Interface & Startup (Workshop 1) |
| **Evidence** | `ConsoleMenu.java`, method `processNewSale()`, lines 232–234: <br>`String saleId = "V-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();`<br>`Sale sale = new Sale(saleId, customerOpt.get(), sellerOpt.get());` |
| **Explanation** | `ConsoleMenu` directly instantiates the domain entity `Sale` and generates its identifier. According to the Creator pattern, `SaleService` (which records, coordinates, and manages the lifecycle of sales transactions) should create the `Sale` aggregate, or `SaleService` should provide a creation factory method. |
| **Consequence** | The UI layer is tightly bound to the internal instantiation details and ID generation scheme of domain entities. |
| **Proposed Solution** | Provide a method in `SaleService` (such as `createSale(Customer, Seller)`) that handles entity instantiation and identifier generation, shielding the UI from constructor dependencies. |
| **Other Member's Code** | None (under Technical Leader's responsibility). |

---

### 2.3 Controller

* **Verdict:** **Violation**

#### Well-Applied Evidence
* `com.gamezone.service.SaleService.registerSale(Sale, List<String>)` (lines 67–129): Acts as a Use Case Controller / Application Service orchestrating the transaction workflow: validating stock, evaluating promotions, assigning warranties, decrementing inventory, and persisting the record.

#### Identified Violations

| Field | Finding L-V04 |
| :--- | :--- |
| **ID** | `L-V04` |
| **Pattern** | Controller |
| **Module** | Sales, Interface & Startup (Workshop 1) |
| **Evidence** | `SaleService.java`, lines 41–43: <br>`public Sale findById(String id) { return null; // Stub to satisfy compilation. Real implementation requires PersonService. }`<br>Forced workaround in `ConsoleMenu.java`, lines 282–287 and 599–600: <br>`Sale sale = getAllSalesInternal().stream().filter(s -> s.getId().equals(saleId)).findFirst().orElse(null);` |
| **Explanation** | Because `SaleService` fails to fulfill its role as controller for sales queries (stubbed `findById`), `ConsoleMenu` is forced to act as a pseudo-controller: it coordinates retrieval across 3 services, executes in-memory filters, and manages transactional lookup logic. |
| **Consequence** | The presentation layer assumes orchestration responsibilities that belong strictly to the application/service layer. |
| **Proposed Solution** | Properly implement `findById(String id)` inside `SaleService` (by collaborating with `PersonService` and `ProductService`), allowing UI controllers to delegate sale lookups cleanly. |
| **Other Member's Code** | Requires collaboration with `PersonService` (Developer 2) and `ProductService` (Developer 1). |

---

### 2.4 Low Coupling

* **Verdict:** **Violation**

#### Well-Applied Evidence
* Decoupling of domain entities (`Sale`) from persistence mechanisms via `SaleRepository`. `Sale` has zero dependencies on `java.io` or file paths.

#### Identified Violations

| Field | Finding L-V05 | Finding L-V06 |
| :--- | :--- | :--- |
| **ID** | `L-V05` | `L-V06` |
| **Pattern** | Low Coupling | Low Coupling |
| **Module** | Console UI (Workshop 1 & Cross-Module) | Persistence (Workshop 1) |
| **Evidence** | `ConsoleMenu.java`, constructor and attributes (lines 49–70): <br>`private final ProductService productService;`<br>`private final PersonService personService;`<br>`private final SaleService saleService;`<br>`private AccessoryService accessoryService;`<br>`private final ReturnService returnService;`<br>`private final WarrantyService warrantyService;`<br>`private final PromotionService promotionService;` | `SaleRepository.java`, method `loadAll()`, lines 90–91: <br>`public List<Sale> loadAll(List<Customer> customers, List<Seller> sellers, List<Product> products)` |
| **Explanation** | `ConsoleMenu` is directly coupled to 7 different service implementations and several domain models, creating an excessively high coupling fan-out. Any change in any service constructor or method contract directly affects `ConsoleMenu`. | `SaleRepository` cannot deserialize a sale on its own; it forces the caller to load and provide collections of all customers, sellers, and products, tightly coupling repository methods to external domain collections. |
| **Consequence** | Ripple effect of changes: modifying a subsystem requires updating `ConsoleMenu`. `SaleRepository` cannot be used in isolation or tested independently without full preloaded collections. | Maintenance difficulty and high fragility across integration points. |
| **Proposed Solution** | Group related operations or introduce Facade/Controller abstractions to reduce direct dependencies in `ConsoleMenu`. Decouple repository loading or pass repository/lookup helpers directly where appropriate. | Refactor `SaleRepository` and `SaleService` so that entity linking is handled without exposing list parameters across layers. |
| **Other Member's Code** | Cross-cutting (all modules). | Touches `PersonService` (Developer 2) and `ProductService` (Developer 1). |
