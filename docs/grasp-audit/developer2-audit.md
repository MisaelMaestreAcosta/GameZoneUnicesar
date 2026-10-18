# GRASP Pattern Audit - Developer 2

**Author:** <Rafael De Jesús Vega Oñate>
**Role:** Developer 2

---

## 1. Audit Scope

| Module / Requirement | Class / Artifact | Specific Methods & Scope Audited |
| :--- | :--- | :--- |
| **Persons** (Workshop 1) | `com.gamezone.model.Person`, `Customer`, `Seller` | Attributes, constructors, getters/setters and `getRoleDescription()`. |
| | `com.gamezone.persistence.PersonRepository` | `saveCustomers`, `loadCustomers`, `saveSellers`, `loadSellers`, `writeToFile`, `readAllLines`, `initializeDefaultSellers`. |
| | `com.gamezone.service.PersonService` | Constructor, `registerCustomer`, `listCustomers`, `findCustomerById`, `registerSeller`, `listSellers`, `findSellerById`. |
| **Accessories** (Requirement 1) | `AccessoryRepository` | `saveAll`, `loadAll`, `toTextLine`, `fromTextLine`. |
| | `AccessoryService` | Constructor, `registerController`, `registerCable`, `registerMemory`, `listAllAccessories`, `listAccessoriesByType`, `findAccessoriesCompatibleWith`, `findById`, `updateStock`. |
| **Promotions** (Requirement 2) | `PromotionRepository` | `saveAll`, `loadAll`, `toTextLine`, `fromTextLine`. |
| | `PromotionService` | All methods except `registerCategoryDiscount`, which belongs to Developer 1. |
| **Returns** (Requirement 3) | `ReturnRepository` | Constructor, `saveAll`, `loadAll`, `toTextLine`, `fromTextLine`, `findSaleById`. |
| | `ReturnService` | Constructor, `registerReturn`, `viewAllReturns`, `viewReturnsByCustomer`, `viewReturnsBySale`, `generateMonthlyBalance`, `findSaleById`, `findProductInSale`. |
| **Warranties** (Requirement 4) | `WarrantyRepository` | Constructor, `saveAll`, `loadAll`, `toTextLine`, `fromTextLine`. |
| | `WarrantyService` | Constructor, `assignBasicWarranty`, `assignExtendedWarranty`, `findWarrantyByProduct`, `listAllWarranties`, `listActiveWarranties`, `listWarrantiesExpiringSoon`. |
| **Exceptions and validations** (Requirement 6) | Services and repositories above | Use of exceptions and validators. |

---

## 2. Evaluation of GRASP Patterns

### 2.1 Information Expert

* **Verdict:** **Violation**

#### Well-Applied Evidence

* `Person` owns `name`, `id` and `phone`. `Customer` and `Seller` implement `getRoleDescription()` using their own data (`email`, `employeeCode`, `shift`).
* `PersonService.findCustomerById()` and `findSellerById()` search the collections held by the service itself.
* `AccessoryService.findAccessoriesCompatibleWith()` filters using `Accessory.getCompatibleConsoleIds()`, data owned by the accessory itself.
* `PromotionService.findBestPromotionFor()` delegates the discount computation to each `Promotion.calculateDiscount(Sale)` and `Promotion.isActive()`, which hold the rules, and only compares the results.
* `WarrantyService.listActiveWarranties()` and `listWarrantiesExpiringSoon()` ask `Warranty.isActive()` and `Warranty.getEndDate()`, so the validity rules stay in `Warranty`.

#### Identified Violations

| Field | Finding D2-V12 |
| :--- | :--- |
| **ID** | `D2-V12` |
| **Pattern** | Information Expert |
| **Module** | Returns (Requirement 3) |
| **Evidence** | `ReturnService.registerReturn()` computes `ChronoUnit.DAYS.between(sale.getDate().toLocalDate(), LocalDate.now())` and compares it with `RETURN_WINDOW_DAYS`. |
| **Explanation** | The return-eligibility rule is computed from data owned by `Sale` (its date), but it lives in the service. The class documentation states the check is done here instead of in `Sale`. |
| **Consequence** | The rule can exist in two places (`Sale` and `ReturnService`) and diverge. |
| **Proposed Solution** | Have `ReturnService` ask `Sale.canBeReturned()` and let `Sale` own the rule, keeping the 30-day window and the same message. |
| **Other Member's Code** | Yes. `Sale.canBeReturned()` belongs to Developer 1; coordinate through the Technical Leader. |

### 2.2 Creator

* **Verdict:** **Correct**

#### Well-Applied Evidence

* `PersonRepository.loadCustomers()` and `loadSellers()` create `Customer` and `Seller` instances because they hold the file data needed to build them.
* `AccessoryRepository.fromTextLine()` creates `Controller`, `Cable` and `Memory` from file data. `AccessoryService.registerController/Cable/Memory()` create the accessories and manage the collection.
* `PromotionRepository.fromTextLine()` creates the promotion subclasses from file data. `PromotionService.registerPercentageDiscount()` and `registerBulkPurchaseDiscount()` create the promotions and manage the collection.
* `ReturnService.registerReturn()` creates the `Return` because it has the sale, products and reason. `ReturnRepository.fromTextLine()` creates `Return` objects from file data.
* `WarrantyService.assignBasicWarranty()` and `assignExtendedWarranty()` create the warranties and manage the collection. `WarrantyRepository.fromTextLine()` creates them from file data.

### 2.3 Controller

* **Verdict:** **Correct**

#### Well-Applied Evidence

* `PersonService`, `AccessoryService`, `PromotionService`, `ReturnService` and `WarrantyService` coordinate their use cases (register, list, find, query) and delegate persistence to their repositories. The model classes do not receive system events.

### 2.4 Low Coupling

* **Verdict:** **Violation**

#### Well-Applied Evidence

* `Person`, `Customer` and `Seller` perform no file I/O and depend only on the `Person` base class.
* `PersonService`, `AccessoryService` and `PromotionService` receive their repositories through the constructor.

#### Identified Violations

| Field | Finding D2-V10 |
| :--- | :--- |
| **ID** | `D2-V10` |
| **Pattern** | Low Coupling |
| **Module** | Returns (Requirement 3) |
| **Evidence** | `ReturnRepository` (persistence layer) imports and receives `SaleService`, `ProductService` and `PersonService` through its constructor, and `loadAll()` calls `saleService.listAllSales(...)` and `productService.findById()`. |
| **Explanation** | The persistence layer depends on the service layer, against the dependency direction ui → service → persistence → model. It also creates a cycle: `ReturnService` → `ReturnRepository` → `SaleService`/`ProductService`/`PersonService`. |
| **Consequence** | `ReturnRepository` cannot be built or tested without three services, and any change in their APIs breaks persistence. |
| **Proposed Solution** | Make `ReturnRepository` read and write only identifiers, with no service dependency, and have `ReturnService` resolve the sale and products when loading. File content and behavior stay identical. |
| **Other Member's Code** | No. The correction only touches Developer 2's `ReturnRepository` and `ReturnService`. |

| Field | Finding D2-V11 |
| :--- | :--- |
| **ID** | `D2-V11` |
| **Pattern** | Low Coupling (cross-reference: Indirection) |
| **Module** | Returns (Requirement 3) |
| **Evidence** | `ReturnService.findSaleById()` and `generateMonthlyBalance()` both call `saleService.listAllSales(personService.listCustomers(), personService.listSellers(), productService.listAllProducts())`. |
| **Explanation** | `ReturnService` depends on `PersonService` and `ProductService` only to feed arguments to `SaleService`, and rebuilds every sale for each lookup. |
| **Consequence** | A change in how sales are reconstructed forces changes in `ReturnService`, and each lookup does unnecessary work. |
| **Proposed Solution** | Have `ReturnService` depend only on `SaleService`, through a lookup that resolves its own collaborators (Indirection). Results stay identical. |
| **Other Member's Code** | Yes. `SaleService` belongs to the Technical Leader; this is a boundary finding. |

| Field | Finding D2-V15 |
| :--- | :--- |
| **ID** | `D2-V15` |
| **Pattern** | Low Coupling |
| **Module** | Warranties (Requirement 4) |
| **Evidence** | `WarrantyRepository` (persistence layer) imports and receives `SaleService` and `ProductService` through its constructor, and `fromTextLine()` calls `productService.findById()` and `saleService.findById()`. |
| **Explanation** | The persistence layer depends on the service layer, against the dependency direction ui → service → persistence → model. It is the same structure as `D2-V10`. |
| **Consequence** | `WarrantyRepository` cannot be built or tested without two services, and API changes in them break persistence. |
| **Proposed Solution** | Make `WarrantyRepository` read and write only identifiers, and have `WarrantyService` resolve the product and sale when loading. File content and behavior stay identical. |
| **Other Member's Code** | No. The correction only touches Developer 2's classes. |

### 2.5 High Cohesion

* **Verdict:** **Violation**

#### Identified Violations

| Field | Finding D2-V01 |
| :--- | :--- |
| **ID** | `D2-V01` |
| **Pattern** | High Cohesion (cross-reference: Indirection) |
| **Module** | Persons (Workshop 1) / Exceptions and validation (Requirement 6) |
| **Evidence** | `PersonService.registerCustomer()` and `registerSeller()` check `getId() == null \|\| getId().isBlank()` and duplicate IDs inline, throwing `IllegalArgumentException` and `IllegalStateException`. |
| **Explanation** | The service mixes use-case coordination with data-validation rules that belong in the validation layer introduced in Requirement 6. The same absence of validators applies to `AccessoryService.register*()`, `PromotionService.register*()` and `WarrantyService.assign*()`. |
| **Consequence** | A validation rule change must be applied in several places, and it is unclear which one is authoritative. |
| **Proposed Solution** | Delegate these checks to the validation layer, keeping the same messages and the same exception types so the observable behavior does not change. |
| **Other Member's Code** | The validators belong to Developer 1; coordinate through the Technical Leader if they must be modified. |

| Field | Finding D2-V02 |
| :--- | :--- |
| **ID** | `D2-V02` |
| **Pattern** | High Cohesion |
| **Module** | Persons (Workshop 1) |
| **Evidence** | `PersonRepository.initializeDefaultSellers()` creates three hard-coded `Seller` objects, and `loadSellers()` calls it when `sellers.txt` does not exist. |
| **Explanation** | The persistence class also owns initial business data, a second responsibility beyond reading and writing files. |
| **Consequence** | Changing the default sellers requires editing the persistence class. |
| **Proposed Solution** | Move the seed data to a dedicated class used by `PersonService` (Pure Fabrication), keeping the same three sellers and the same file content. |
| **Other Member's Code** | No other member's code is required. |

| Field | Finding D2-V09 |
| :--- | :--- |
| **ID** | `D2-V09` |
| **Pattern** | High Cohesion |
| **Module** | Promotions (Requirement 2) |
| **Evidence** | `PromotionService.findBestPromotionFor()` embeds the loop that selects the promotion with the highest discount, inside a service that also registers and lists promotions. |
| **Explanation** | The best-promotion selection rule is a second responsibility inside a registration and listing service. |
| **Consequence** | The selection rule cannot change or be reused without editing the service. |
| **Proposed Solution** | Extract the selection rule into a dedicated class (Pure Fabrication) used by the service, keeping the same result, including ties and the no-promotion case. |
| **Other Member's Code** | No other member's code is required. |

| Field | Finding D2-V13 |
| :--- | :--- |
| **ID** | `D2-V13` |
| **Pattern** | High Cohesion |
| **Module** | Returns (Requirement 3) |
| **Evidence** | `ReturnService` contains `registerReturn()` (validation, stock restoration, persistence), the query methods (`viewAllReturns`, `viewReturnsByCustomer`, `viewReturnsBySale`) and `generateMonthlyBalance()`, which sums sales and refunds. |
| **Explanation** | Monthly balance reporting is a financial calculation over sales, unrelated to registering and querying returns. |
| **Consequence** | Changing the balance rules means editing the return-registration service, which also forces it to depend on `PersonService` and `ProductService`. |
| **Proposed Solution** | Move `generateMonthlyBalance()` to a dedicated class (Pure Fabrication), keeping the same formula and results. |
| **Other Member's Code** | No other member's code is required for the extraction itself. |

### 2.6 Polymorphism

* **Verdict:** **Violation**

#### Well-Applied Evidence

* `Person.getRoleDescription()` is abstract and overridden in `Customer` and `Seller`, so no type checks are needed to describe a person.
* The returns module has no type variants, so no conditionals on type appear in `ReturnService` or `ReturnRepository`.

#### Identified Violations

| Field | Finding D2-V04 |
| :--- | :--- |
| **ID** | `D2-V04` |
| **Pattern** | Polymorphism |
| **Module** | Accessories (Requirement 1) |
| **Evidence** | `AccessoryRepository.toTextLine()` uses an `instanceof` chain over `Controller`, `Cable` and `Memory`. `AccessoryRepository.fromTextLine()` uses a `switch` over the strings `"CONTROLLER"`, `"CABLE"` and `"MEMORY"`. |
| **Explanation** | Serialization and deserialization depend on the concrete accessory type through conditionals instead of letting each type provide its own variation. |
| **Consequence** | Adding a new accessory type requires editing two existing methods in the repository, and a missed branch fails only at runtime (`IllegalArgumentException`). |
| **Proposed Solution** | Introduce one serializer per accessory type behind a common interface (Polymorphism) and look it up by type key in the repository. File format, field order and file content stay identical. |
| **Other Member's Code** | No code of `Accessory`, `Controller`, `Cable` or `Memory` (Developer 1) needs changes, because serializers are separate classes. |

| Field | Finding D2-V07 |
| :--- | :--- |
| **ID** | `D2-V07` |
| **Pattern** | Polymorphism |
| **Module** | Promotions (Requirement 2) |
| **Evidence** | `PromotionRepository.toTextLine()` uses an `instanceof` chain over `PercentageDiscount`, `CategoryDiscount` and `BulkPurchaseDiscount`. `fromTextLine()` uses a `switch` over the strings `"PERCENTAGE"`, `"CATEGORY"` and `"BULK"`. |
| **Explanation** | Serialization and deserialization depend on the concrete promotion type through conditionals, the same structure recorded in `D2-V04` for accessories. |
| **Consequence** | Adding a promotion type requires editing two existing repository methods, and a missed branch only fails at runtime with `IllegalArgumentException`. |
| **Proposed Solution** | One serializer per promotion type behind a common interface, looked up by type key. File format, field order and content stay identical. |
| **Other Member's Code** | No code of the promotion hierarchy (Developer 1) needs changes, because the serializers are separate classes. |

| Field | Finding D2-V16 |
| :--- | :--- |
| **ID** | `D2-V16` |
| **Pattern** | Polymorphism |
| **Module** | Warranties (Requirement 4) |
| **Evidence** | `WarrantyRepository.toTextLine()` uses `warranty instanceof BasicWarranty ? "BASIC" : "EXTENDED"`, and `fromTextLine()` compares the type string with `"BASIC"` and `"EXTENDED"`. `WarrantyService` has near-duplicate `assignBasicWarranty()` and `assignExtendedWarranty()`. |
| **Explanation** | Serialization depends on concrete types through conditionals, as in `D2-V04` and `D2-V07`. The ternary also classifies any unknown subtype as `"EXTENDED"`. |
| **Consequence** | A new warranty type would be saved with the wrong discriminator without any error. |
| **Proposed Solution** | One serializer per warranty type behind a common interface, looked up by type key. File format and content stay identical. |
| **Other Member's Code** | No code of the warranty hierarchy (Developer 1) needs changes. |

### 2.7 Pure Fabrication

* **Verdict:** **Correct**

#### Well-Applied Evidence

* `PersonRepository`, `AccessoryRepository`, `PromotionRepository`, `ReturnRepository` and `WarrantyRepository` encapsulate flat-file persistence so the domain classes stay free of I/O.

### 2.8 Indirection

* **Verdict:** **Correct**

#### Well-Applied Evidence

* Each repository mediates between its service and its data file (`data/accessories.txt`, `data/promotions.txt`, `data/returns.txt`, `data/warranties.txt`, and the customer and seller files).
* `ReturnService` uses `ProductService.restoreStock()` instead of touching product stock directly.
* `ReturnService.registerReturn()` reports failures with a message per rule (missing sale, expired window, product not in sale), which keeps each error identifiable.
* The coupling overlap is recorded once, in `D2-V10`, `D2-V11` and `D2-V15`.

### 2.9 Protected Variations

* **Verdict:** **Violation**

#### Identified Violations

| Field | Finding D2-V03 |
| :--- | :--- |
| **ID** | `D2-V03` |
| **Pattern** | Protected Variations |
| **Module** | Persons (Workshop 1) |
| **Evidence** | `PersonRepository` fixes `CUSTOMERS_PATH`, `SELLERS_PATH` and `DELIMITER` as `static final` constants, and `PersonService` depends on the concrete class. |
| **Explanation** | The storage location and format cannot vary without editing the class. |
| **Consequence** | Changing the file format or location, or testing with another path, requires modifying production code. |
| **Proposed Solution** | Inject the paths through the constructor, using the current values as defaults. |
| **Other Member's Code** | No other member's code is required. |

| Field | Finding D2-V05 |
| :--- | :--- |
| **ID** | `D2-V05` |
| **Pattern** | Protected Variations (cross-reference: Polymorphism) |
| **Module** | Accessories (Requirement 1) |
| **Evidence** | `AccessoryService.listAccessoriesByType()` compares `a.getClass().getSimpleName()` with the received string. The service also has one nearly identical `registerController()`, `registerCable()` and `registerMemory()` method per type. |
| **Explanation** | Filtering depends on the Java class name, so renaming or refactoring a class silently breaks the behavior. Each new accessory type forces a new register method in the service. |
| **Consequence** | A class rename returns empty lists without any compile error, and extending the catalog requires modifying the service. |
| **Proposed Solution** | Filter through a stable type key (for example, the serializer key from `D2-V04`) instead of the class name. Keep the accepted input values and results identical. |
| **Other Member's Code** | Exposing a type key on `Accessory` would touch Developer 1's code; coordinate through the Technical Leader. |

| Field | Finding D2-V06 |
| :--- | :--- |
| **ID** | `D2-V06` |
| **Pattern** | Protected Variations |
| **Module** | Accessories (Requirement 1) |
| **Evidence** | `AccessoryRepository` fixes `FILE_PATH`, `FIELD_DELIMITER` and `LIST_DELIMITER` as `static final` constants, and `AccessoryService` depends on the concrete class. |
| **Explanation** | The storage location and format cannot vary without editing the class, the same situation as `D2-V03`. |
| **Consequence** | Testing with another path or changing the format requires modifying production code. |
| **Proposed Solution** | Inject the path through the constructor, with the current value as default. |
| **Other Member's Code** | No other member's code is required. |

| Field | Finding D2-V08 |
| :--- | :--- |
| **ID** | `D2-V08` |
| **Pattern** | Protected Variations |
| **Module** | Promotions (Requirement 2) |
| **Evidence** | `PromotionRepository` fixes `FILE_PATH` and `FIELD_DELIMITER` as `static final` constants, and `PromotionService` depends on the concrete class. |
| **Explanation** | The storage location and format cannot vary without editing the class, the same situation as `D2-V03` and `D2-V06`. |
| **Consequence** | Testing with another path or changing the format requires modifying production code. |
| **Proposed Solution** | Inject the path through the constructor, with the current value as default. |
| **Other Member's Code** | No other member's code is required. |

| Field | Finding D2-V14 |
| :--- | :--- |
| **ID** | `D2-V14` |
| **Pattern** | Protected Variations |
| **Module** | Returns (Requirement 3) |
| **Evidence** | `ReturnRepository` fixes `FILE_PATH`, `FIELD_DELIMITER` and `LIST_DELIMITER` as `static final` constants; `ReturnService` fixes `RETURN_WINDOW_DAYS = 30` as a private constant. |
| **Explanation** | The storage location and format, and the return window, cannot vary without editing the classes. |
| **Consequence** | A policy change (for example, a different return window) or a testing path requires modifying production code. |
| **Proposed Solution** | Inject the path through the constructor and expose the return window through a single configurable source, with the current values as defaults. |
| **Other Member's Code** | No, unless the window ends up in `Sale` (see `D2-V12`). |

| Field | Finding D2-V17 |
| :--- | :--- |
| **ID** | `D2-V17` |
| **Pattern** | Protected Variations |
| **Module** | Warranties (Requirement 4) |
| **Evidence** | `WarrantyRepository` fixes `FILE_PATH` and `FIELD_DELIMITER` as `static final` constants, and `WarrantyService` depends on the concrete class. |
| **Explanation** | The storage location and format cannot vary without editing the class, the same situation as `D2-V03`, `D2-V06` and `D2-V08`. |
| **Consequence** | Testing with another path or changing the format requires modifying production code. |
| **Proposed Solution** | Inject the path through the constructor, with the current value as default. |
| **Other Member's Code** | No other member's code is required. |

| Field | Finding D2-V18 |
| :--- | :--- |
| **ID** | `D2-V18` |
| **Pattern** | Protected Variations (cross-reference: High Cohesion, `D2-V01`) |
| **Module** | Exceptions and validation (Requirement 6) |
| **Evidence** | None of Developer 2's services or repositories import the `exceptions` or `validation` packages. Services throw `IllegalArgumentException` (`ReturnService`, `AccessoryService.updateStock()`), `IllegalStateException` (`PersonService`) and repositories throw `RuntimeException` (`AccessoryRepository`, `PromotionRepository`, `ReturnRepository`, `WarrantyRepository`). `PersonRepository.writeToFile()` and `readAllLines()` only log the `IOException` and continue. |
| **Explanation** | Errors are not categorized with the typed exceptions of Requirement 6 (not found, business rule, invalid data, persistence). Persistence failures are handled inconsistently: four repositories throw and `PersonRepository` swallows them. |
| **Consequence** | A caller cannot distinguish error categories without parsing messages, and a failed write in `PersonRepository` goes unnoticed while the other repositories report it. |
| **Proposed Solution** | Use the typed exceptions and the validators already available in the system, keeping the same Spanish messages. Treat the `PersonRepository` swallowed errors as a functional observation unless the team agrees to unify it without changing what the user sees. |
| **Other Member's Code** | Yes. The exception and validator classes belong to Developer 1, and the handling in `ConsoleMenu` belongs to the Technical Leader; report both to the Leader. |

---

## 3. Functional Observation Outside the GRASP Findings

* A name, email, title or reason containing a comma corrupts the line in every repository, because `split(",")` assumes values have no commas.
* `PersonRepository.loadCustomers()` and `loadSellers()` silently ignore lines with the wrong number of fields. The other repositories throw on malformed lines (`ArrayIndexOutOfBoundsException`, `NumberFormatException` or `DateTimeParseException`).
* Sellers are seeded by default but customers are not.
* ID lookup in `PersonService` ignores case, but saved IDs keep their original case.
* `AccessoryService.register*()` perform no validation (duplicate IDs, negative price or quantity).
* `PromotionService.listActivePromotions()` uses `LocalDate.now()` while `findBestPromotionFor()` uses the sale date, so "active" has two meanings.
* `BulkPurchaseDiscount` is written to file as `percentage, minQuantity` but its constructor receives `minQuantity, percentage`; the order is easy to confuse.
* `ReturnService.registerReturn()` calls `ret.calculateRefundAmount()` without using its return value, while the `Return` is created with a refund of `0.0`.
* `registerReturn()` restores stock before `saveAll()`; if saving fails, stock stays restored without a saved return.
* `findProductInSale()` does not check quantity, so passing the same product ID several times can return more units than were sold, and nothing prevents returning the same product twice for the same sale.
* `ReturnRepository.fromTextLine()` can add `null` when `productService.findById()` finds no product.
* `WarrantyRepository.loadAll()` silently drops warranties whose product or sale is not found (`fromTextLine()` returns `null`), so the next `saveAll()` erases them from the file.
* `assignBasicWarranty()` and `assignExtendedWarranty()` do not check that the product belongs to the sale or whether a warranty already exists.
* Several lookups return `null` (`AccessoryService.findById`, `PromotionService.findById`, `WarrantyService.findWarrantyByProduct`) while `PersonService` returns `Optional`; the error contract is inconsistent.

## 4. Summary of Findings

| Finding ID | Pattern | Status | Assigned Module | Boundary Finding? |
| :--- | :--- | :--- | :--- | :--- |
| `D2-V01` | High Cohesion; Indirection cross-reference | **Violation** | Persons / validation | Possible, with Developer 1 |
| `D2-V02` | High Cohesion | **Violation** | Persons persistence | No |
| `D2-V03` | Protected Variations | **Violation** | Persons persistence | No |
| `D2-V04` | Polymorphism | **Violation** | Accessories persistence | No |
| `D2-V05` | Protected Variations; Polymorphism cross-reference | **Violation** | Accessories service | Possible, with Developer 1 |
| `D2-V06` | Protected Variations | **Violation** | Accessories persistence | No |
| `D2-V07` | Polymorphism | **Violation** | Promotions persistence | No |
| `D2-V08` | Protected Variations | **Violation** | Promotions persistence | No |
| `D2-V09` | High Cohesion | **Violation** | Promotions service | No |
| `D2-V10` | Low Coupling | **Violation** | Returns persistence | No |
| `D2-V11` | Low Coupling; Indirection cross-reference | **Violation** | Returns service | Yes, with Technical Leader (`SaleService`) |
| `D2-V12` | Information Expert | **Violation** | Returns service | Yes, with Developer 1 (`Sale.canBeReturned`) |
| `D2-V13` | High Cohesion | **Violation** | Returns service | No |
| `D2-V14` | Protected Variations | **Violation** | Returns persistence and service | No |
| `D2-V15` | Low Coupling | **Violation** | Warranties persistence | No |
| `D2-V16` | Polymorphism | **Violation** | Warranties persistence and service | No |
| `D2-V17` | Protected Variations | **Violation** | Warranties persistence | No |
| `D2-V18` | Protected Variations; High Cohesion cross-reference | **Violation** | Exceptions and validation usage | Yes, with Developer 1 and Technical Leader |
