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

---

### 2.5 High Cohesion

* **Verdict:** **Violation**

#### Well-Applied Evidence
* `SaleRepository.java` focuses strictly on disk persistence operations (`save` and `loadAll` in `sales.txt`), without mixing business validation or UI presentation.

#### Identified Violations

| Field | Finding L-V07 | Finding L-V08 |
| :--- | :--- | :--- |
| **ID** | `L-V07` | `L-V08` |
| **Pattern** | High Cohesion | High Cohesion |
| **Module** | Console UI (All Requirements) | Returns Integration (Requirement 3) |
| **Evidence** | `ConsoleMenu.java` (826 lines total, handling console parsing, presentation, flow orchestration, return policy checks, and financial summary calculation). | `ProductService.java`, method `restoreStock(String, int)`, lines 92–106: <br>`void restoreStock(String productId, int quantity){ Product product = findById(productId); ... repository.saveAll(products); }` |
| **Explanation** | `ConsoleMenu` suffers from low cohesion ("God Class" symptom in UI). It mixes menu loops, input scanning, domain validation rules, financial metric aggregations, and upsell logic. | `ProductService.restoreStock()` is an exact duplicate of `updateStock(String, int)` (lines 78–91), has package-private visibility, lacks JavaDoc, and splits stock management logic redundantly. |
| **Consequence** | Comprehensibility and maintainability are severely degraded. Bugs in one submenu can break unrelated features. Duplicate stock methods introduce diverging maintenance paths. | Code duplication violates DRY and degrades cohesion of `ProductService`. |
| **Proposed Solution** | Decompose `ConsoleMenu` into cohesive sub-handlers or menu presenters (e.g., `SalesView`, `ReturnView`). Eliminate `restoreStock` by delegating directly to `updateStock(productId, quantity)`. | Refactor `restoreStock` to call `updateStock` directly, or deprecate/remove the duplicate method after aligning with `ReturnService`. |
| **Other Member's Code** | Touches all module entry points. | `ProductService` is maintained by Developer 1; method was added by Leader during Requirement 3. |

---

### 2.6 Polymorphism

* **Verdict:** **Violation**

#### Well-Applied Evidence
* In `Sale.java`, polymorphism is respected when iterating over `SalesLineItem`: `item.calculateSubtotal()` is called uniformly regardless of product type.

#### Identified Violations

| Field | Finding L-V09 | Finding L-V10 |
| :--- | :--- | :--- |
| **ID** | `L-V09` | `L-V10` |
| **Pattern** | Polymorphism | Polymorphism |
| **Module** | Sales Service (Requirements 1 & 4) | Console UI (Requirement 4) |
| **Evidence** | `SaleService.java`, method `registerSale()`, lines 73–77, 106–114, 120–124: <br>`if (item.getProduct() instanceof Accessory && accessoryService != null) { ... } else { ... }`<br>`if (product instanceof com.gamezone.model.Console) { ... }`<br>`if (product instanceof Accessory && accessoryService != null) { ... }` | `ConsoleMenu.java`, method `processNewSale()`, lines 261–266: <br>`if (product instanceof com.gamezone.model.Console) { System.out.print("Este producto es una consola. ¿Desea agregar Garantía Extendida? (s/n): "); ... }` |
| **Explanation** | `SaleService` relies on conditional type checks (`instanceof Accessory`, `instanceof Console`) to handle divergent behavior for stock lookup, warranty applicability, and inventory decrement. This is an explicit violation of the Polymorphism pattern. | `ConsoleMenu` uses `instanceof Console` to conditionally display warranty prompts, hardcoding class hierarchy checks into UI flow rather than querying a polymorphic property. |
| **Consequence** | Adding new product categories or warrantable items requires modifying `SaleService` and `ConsoleMenu` with additional conditional branches, violating the Open/Closed Principle. | The design is rigid and non-extensible for future catalog expansions. |
| **Proposed Solution** | Treat `Accessory` uniformly through a common inventory/catalog contract, or delegate warranty eligibility check to the product hierarchy (e.g., `product.isWarrantable()`). | Polymorphically query product capabilities (e.g., `product.supportsWarranty()`) rather than checking `instanceof Console`. |
| **Other Member's Code** | Requires Developer 1 (`Product`, `Console`) and Developer 2 (`Accessory`). | Requires Developer 1 (`Product`, `Console`). |

---

### 2.7 Pure Fabrication

* **Verdict:** **Correct**

#### Well-Applied Evidence
* `com.gamezone.persistence.SaleRepository`: This class does not represent any real-world entity in the gaming domain. It was fabricated specifically to encapsulate low-level file I/O operations (`BufferedWriter`, `BufferedReader`, `sales.txt`), preventing the domain model (`Sale`) from being polluted with persistence infrastructure.
* `com.gamezone.service.SaleService`: Fabricated as an application service to coordinate use-case workflows and inter-service dependencies without inflating domain entity responsibilities.

---

### 2.8 Indirection

* **Verdict:** **Violation**

#### Well-Applied Evidence
* `SaleService` introduces an indirection layer between UI (`ConsoleMenu`) and persistence (`SaleRepository`) for recording transactions (`registerSale`), keeping the UI isolated from flat-file storage mechanisms.

#### Identified Violations

| Field | Finding L-V11 |
| :--- | :--- |
| **ID** | `L-V11` |
| **Pattern** | Indirection |
| **Module** | Sales & Interface (Workshop 1 & Requirement 3) |
| **Evidence** | `ConsoleMenu.java`, lines 282–287, 292–299, 599–600: <br>`private List<Sale> getAllSalesInternal() { List<Customer> customers = personService.listCustomers(); List<Seller> sellers = personService.listSellers(); List<Product> products = productService.listAllProducts(); return saleService.listAllSales(customers, sellers, products); }` |
| **Explanation** | The indirection provided by `SaleService` is broken for queries. Instead of acting as an intermediary that retrieves sales directly, `SaleService` forces `ConsoleMenu` to mediate between `PersonService`, `ProductService`, and `SaleService`, violating proper indirection. |
| **Consequence** | Unnecessary indirection leakage: every caller wanting to display sales history or lookup a sale must know about and invoke three unrelated services. |
| **Proposed Solution** | Allow `SaleService` to manage its own internal collaborator references to `PersonService` and `ProductService`, so `saleService.listAllSales()` and `saleService.findById()` can be called without intermediary parameter passing by the UI. |
| **Other Member's Code** | Touches integration with `PersonService` (Developer 2) and `ProductService` (Developer 1). |

---

### 2.9 Protected Variations

* **Verdict:** **Violation**

#### Well-Applied Evidence
* `Sale.getItems()` (line 130): Protects the internal state of the `Sale` entity from external mutations by returning an unmodifiable view: `Collections.unmodifiableList(items)`.

#### Identified Violations

| Field | Finding L-V12 |
| :--- | :--- |
| **ID** | `L-V12` |
| **Pattern** | Protected Variations |
| **Module** | Sales Service & Startup (Workshop 1 & Requirements 1, 2, 4) |
| **Evidence** | `SaleService.java`, lines 22–27, 53: <br>`private final SaleRepository saleRepository;`<br>`private final ProductService productService;`<br>`private WarrantyService warrantyService;`<br>`private AccessoryService accessoryService;`<br>`private PromotionService promotionService;` |
| **Explanation** | `SaleService` couples directly to concrete service and repository implementations rather than stable abstractions or interfaces. Any variation or substitution in inventory handling, warranty calculation, or persistence implementation forces direct modifications in `SaleService`. |
| **Consequence** | The system is vulnerable to changes in collaborating modules. Adding new persistence mechanisms (e.g., database) or mocking components for unit testing is difficult. |
| **Proposed Solution** | Introduce interface abstractions for repositories and service collaborators (or at minimum wrap volatile points of variation behind stable method contracts), shielding `SaleService` from implementation changes. |
| **Other Member's Code** | Involves collaborators maintained by Developer 1 and Developer 2. |

---

## 3. Summary of Findings

| Finding ID | Pattern | Status | Assigned Module | Boundary Finding? |
| :--- | :--- | :--- | :--- | :--- |
| `L-V01` | Information Expert | **Violation** | Returns Integration (Req 3) | Yes (with Developer 1 & 2) |
| `L-V02` | Information Expert | **Violation** | Returns / Monthly Balance (Req 3) | Yes (with Developer 2) |
| `L-V03` | Creator | **Violation** | Sales UI & Model (Workshop 1) | No (Leader code) |
| `L-V04` | Controller | **Violation** | Sales Service (Workshop 1) | Yes (with Developer 1 & 2) |
| `L-V05` | Low Coupling | **Violation** | Console UI (Cross-Module) | Yes (Cross-cutting) |
| `L-V06` | Low Coupling | **Violation** | Sale Repository (Workshop 1) | Yes (with Developer 1 & 2) |
| `L-V07` | High Cohesion | **Violation** | Console UI (Cross-Module) | No (Leader code) |
| `L-V08` | High Cohesion | **Violation** | Product Service - restoreStock (Req 3) | Yes (with Developer 1) |
| `L-V09` | Polymorphism | **Violation** | Sale Service (Req 1 & 4) | Yes (with Developer 1 & 2) |
| `L-V10` | Polymorphism | **Violation** | Console UI (Req 4) | Yes (with Developer 1) |
| `L-V11` | Indirection | **Violation** | Sales & Interface (Workshop 1 & Req 3) | Yes (with Developer 1 & 2) |
| `L-V12` | Protected Variations | **Violation** | Sales Service (Workshop 1, 1, 2, 4) | Yes (with Developer 1 & 2) |
