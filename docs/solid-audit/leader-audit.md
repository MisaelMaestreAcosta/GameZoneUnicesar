# SOLID Principles Audit - Technical Leader

**Author:** Misael Andrés Maestre Acosta (Technical Leader)  
**Role:** Technical Leader  
**Assigned Scope:** Sales Module, Console UI, System Startup, and Cross-Module Integration  
**Repository Branch:** `feature/solid-audit`  

---

## 1. Audit Scope

In accordance with Section 4 of the project specification and `TEAM.md`, the Technical Leader is responsible for auditing the following classes and architectural artifacts under the SOLID design principles:

| Module / Layer | Class / Artifact | Specific Scope & Methods Audited |
| :--- | :--- | :--- |
| **Sales Model** | `com.gamezone.model.Sale` | Lifecycle, line item management, price aggregation (`calculateSubtotal`, `calculateTotal`), return policy (`canBeReturned`), discount/warranty state, and receipt generation (`generateReceipt`). |
| | `com.gamezone.model.SalesLineItem` | Line item aggregation, quantity/product state, `calculateSubtotal()`. |
| **Sales Persistence** | `com.gamezone.persistence.SaleRepository` | Flat-file I/O, serialization (`save`), deserialization with collaborator reconstruction (`loadAll`). |
| **Sales Service** | `com.gamezone.service.SaleService` | Use-case orchestration (`registerSale`), entity creation (`createSale`), query methods (`listAllSales`, `findById`), collaborator wiring. |
| **Presentation (UI)** | `com.gamezone.ui.ConsoleMenu` | Console interactive loop (`start()`), input reading and parsing, menu routing, cross-module workflow coordination. |
| **System Bootstrap** | `com.gamezone.main` | Repository instantiation, dependency injection wiring, presentation startup. |
| **Integration** | Service & UI Collaborators | Cross-cutting interaction with `ProductService`, `PersonService`, `AccessoryService`, `PromotionService`, `ReturnService`, and `WarrantyService`. |

---

## 2. Principle Evaluation

### 2.1 Single Responsibility Principle (SRP)

> *"A class should have one, and only one, reason to change."*

* **Verdict:** **Violation**

#### Well-Applied Evidence
* `com.gamezone.model.SalesLineItem`: Strictly holds the association between a `Product` and an integer quantity, calculating solely its own line item subtotal (`product.getPrice() * quantity`). It has a single reason to change: modifications to line item data structure.

#### Identified Violations

| Field | Finding L-S01 |
| :--- | :--- |
| **ID** | `L-S01` |
| **Principle** | Single Responsibility Principle (SRP) |
| **Module** | Presentation Layer (`com.gamezone.ui.ConsoleMenu`) |
| **Evidence** | `ConsoleMenu.java`, lines 72–818: <br>The class exceeds 800 lines of code and contains terminal menu rendering (`start`), console input reading/parsing, business validation (ranges, prompts), cross-module transaction orchestration (sales, returns, warranties, accessories, promotions), receipt printing, and financial balance reporting. |
| **Explanation** | `ConsoleMenu` violates SRP by having multiple distinct actors and reasons to change: UI formatting changes, scanner parsing logic modifications, business workflow alterations, and financial summary reporting adjustments all force changes to this single class. |
| **Consequence** | High maintenance friction and elevated regression risk. Altering presentation styles for one submenu can inadvertently break unrelated operational workflows. |
| **Proposed Solution** | Decompose `ConsoleMenu` into specialized submenu handlers (e.g., `SalesConsoleView`, `ReturnsConsoleView`, `CatalogConsoleView`) or extract console input handling into a reusable `InputReader` utility. |
| **Other Member's Code** | Touches presentation delegation to services owned by Developer 1 and Developer 2. |

| Field | Finding L-S02 |
| :--- | :--- |
| **ID** | `L-S02` |
| **Principle** | Single Responsibility Principle (SRP) |
| **Module** | Sales Model (`com.gamezone.model.Sale`) |
| **Evidence** | `Sale.java`, lines 145–180, method `generateReceipt()`: <br>Constructs formatted textual receipts with ASCII decorative borders (`=========================================`), headers, currency alignment, and breakdown of discounts and warranties. |
| **Explanation** | `Sale` violates SRP by mixing domain entity responsibilities (managing transaction invariants, line items, and financial totals) with presentation formatting concerns (string layout, ASCII decorative borders, and localized labels). |
| **Consequence** | A change in reporting requirements (e.g., generating JSON, HTML, or changing print borders) forces modifications to a core domain model entity. |
| **Proposed Solution** | Extract receipt formatting into a dedicated receipt printer or formatter class (e.g., `SaleReceiptFormatter`), leaving `Sale` strictly focused on business state and calculations. |
| **Other Member's Code** | No. Modifies only the Technical Leader's class `Sale`. |

---

### 2.2 Open/Closed Principle (OCP)

> *"Software entities should be open for extension, but closed for modification."*

* **Verdict:** **Violation**

#### Well-Applied Evidence
* `com.gamezone.model.Sale`: Extensible with respect to line items. Any product subtype inheriting from `Product` can be added via `addLineItem(Product, int)` without altering the internal aggregation logic of `Sale`.

#### Identified Violations

| Field | Finding L-S03 |
| :--- | :--- |
| **ID** | `L-S03` |
| **Principle** | Open/Closed Principle (OCP) |
| **Module** | Sales Service (`com.gamezone.service.SaleService`) |
| **Evidence** | `SaleService.java`, method `registerSale()`, lines 71–87 and 104–115: <br>`if (item.getProduct() instanceof Accessory && accessoryService != null) { ... }`<br>`if (product instanceof com.gamezone.model.Console) { warrantyService.assignBasicWarranty(...); }` |
| **Explanation** | `SaleService` is not closed for modification. Introducing a new product classification with special checkout requirements (such as accessories requiring `AccessoryService` inventory deduction, or consoles requiring warranty certificates) forces direct modifications and conditional branching inside `registerSale()`. |
| **Consequence** | Extending the catalog with new specialized product types requires editing core sales transaction code, risking regression in existing workflows. |
| **Proposed Solution** | Introduce a polymorphic capability or checkout visitor/strategy in the `Product` hierarchy (e.g., `isWarrantyEligible()`) and uniform stock deduction, eliminating concrete type checks in `SaleService`. |
| **Other Member's Code** | Yes. Relies on the `Product` hierarchy owned by Developer 1. |

---

### 2.3 Liskov Substitution Principle (LSP)

> *"Subtypes must be substitutable for their base types without altering program correctness."*

* **Verdict:** **Correct**

#### Well-Applied Evidence
* Within the Technical Leader's scope, `Sale` and `SalesLineItem` are concrete domain entities that do not extend other classes, eliminating inheritance-level contract breaches.
* `SalesLineItem` interacts with the `Product` base abstraction. Both `Videogame` and `Console` (and `Accessory` subclasses) substitute seamlessly wherever `Product` is expected by `SalesLineItem.calculateSubtotal()`, honoring price and availability contracts without throwing unexpected exceptions.

---

### 2.4 Interface Segregation Principle (ISP)

> *"Clients should not be forced to depend upon interfaces that they do not use."*

* **Verdict:** **Not Applicable**

#### Justification
* Within the baseline Sales module (`Sale`, `SalesLineItem`, `SaleRepository`, `SaleService`), all interactions currently rely directly on concrete class methods and standard Java collections (`List`).
* There are no "fat" interfaces with bloated method contracts forcing clients to implement or depend on empty/unsupported operations. The principle will become applicable once repository and service abstractions are introduced during refactoring.

---

### 2.5 Dependency Inversion Principle (DIP)

> *"High-level modules should not depend on low-level modules. Both should depend on abstractions. Abstractions should not depend on details. Details should depend on abstractions."*

* **Verdict:** **Violation**

#### Identified Violations

| Field | Finding L-S04 |
| :--- | :--- |
| **ID** | `L-S04` |
| **Principle** | Dependency Inversion Principle (DIP) |
| **Module** | Sales Service & Persistence (`SaleService`, `SaleRepository`) |
| **Evidence** | `SaleService.java`, lines 22–27: <br>`private final SaleRepository saleRepository;`<br>`private final ProductService productService;`<br>`private WarrantyService warrantyService;`<br>`private AccessoryService accessoryService;`<br>`private PromotionService promotionService;`<br>`private PersonService personService;` |
| **Explanation** | The high-level business service (`SaleService`) depends directly on the concrete low-level persistence implementation (`SaleRepository`) and concrete collaborator services, rather than abstract interfaces. |
| **Consequence** | `SaleService` cannot be unit tested in isolation without real flat-file I/O or without instantiating concrete collaborator services. Switching the storage layer to a database requires editing business logic. |
| **Proposed Solution** | Introduce an abstraction `ISaleRepository` (defining `save(Sale)` and `loadAll(...)`) implemented by `SaleRepository`, and inject `ISaleRepository` into `SaleService`. |
| **Other Member's Code** | No. Touches only `SaleService` and `SaleRepository` (Technical Leader). |

| Field | Finding L-S05 |
| :--- | :--- |
| **ID** | `L-S05` |
| **Principle** | Dependency Inversion Principle (DIP) |
| **Module** | Presentation Layer (`ConsoleMenu.java`) & Bootstrap (`main.java`) |
| **Evidence** | `ConsoleMenu.java`, constructor lines 61–70: <br>`public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService, AccessoryService accessoryService, ReturnService returnService, WarrantyService warrantyService, PromotionService promotionService)` |
| **Explanation** | `ConsoleMenu` directly depends on 7 concrete service implementations rather than high-level service interfaces or a unified application facade. |
| **Consequence** | High fan-out coupling in the UI layer. Any refactoring in the concrete constructor signatures of services ripples into the presentation entry point. |
| **Proposed Solution** | Introduce service interfaces for client interactions or wrap service interactions behind an application facade. |
| **Other Member's Code** | Relies on services maintained by Developer 1 and Developer 2. |

---

## 3. Summary of SOLID Audit Findings

| Finding ID | Principle | Status | Assigned Class / Module | Boundary Finding? | Priority |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **L-S01** | Single Responsibility (SRP) | **Violation** | `ConsoleMenu.java` (UI) | Yes (Cross-module) | Medium |
| **L-S02** | Single Responsibility (SRP) | **Violation** | `Sale.java` (Model) | No (Leader code) | Medium |
| **L-S03** | Open/Closed (OCP) | **Violation** | `SaleService.java` (Service) | Yes (with Developer 1) | High |
| **L-S04** | Dependency Inversion (DIP) | **Violation** | `SaleService` & `SaleRepository` | No (Leader code) | High |
| **L-S05** | Dependency Inversion (DIP) | **Violation** | `ConsoleMenu.java` (UI) | Yes (Cross-cutting) | Low |

---

## 4. Alignment with GRASP Audit

The findings identified under SOLID directly reinforce the responsibility allocations evaluated during the GRASP audit (Requirement 7):
* **SRP (`L-S01`, `L-S02`)** directly mirrors the GRASP **High Cohesion** violations (`L-V07`), confirming that mixing receipt formatting or UI orchestration with domain entities harms maintainability.
* **OCP (`L-S03`)** aligns with GRASP **Polymorphism** (`L-V09`) and **Protected Variations** (`L-V12`), where conditional branching on concrete types (`instanceof Console`) causes rigidity.
* **DIP (`L-S04`, `L-S05`)** formalizes the GRASP **Low Coupling** and **Protected Variations** findings, establishing that software layers must interact through abstract contracts rather than concrete implementations.
