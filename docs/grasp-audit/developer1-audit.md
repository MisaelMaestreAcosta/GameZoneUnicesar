# GRASP Pattern Audit - Developer 1

**Author:** Juan Esteban Vera Mendoza  
**Role:** Developer 1  

---

## 1. Audit Scope

| Module / Requirement | Class / Artifact | Specific Methods & Scope Audited |
| :--- | :--- | :--- |
| **Products** (Workshop 1) | `com.gamezone.model.Product` | Common product state, constructor, stock and description behavior. |
| | `com.gamezone.model.Videogame` | Constructor, videogame-specific attributes and `getDescription()`. |
| | `com.gamezone.model.Console` | Constructor, console-specific attributes and `getDescription()`. |
| | `com.gamezone.persistence.ProductRepository` | File initialization, `loadAll`, `saveAll`, product serialization and deserialization. |
| | `com.gamezone.service.ProductService` | Constructor, product registration, listing, `updateStock`, `findById`, and common-field validation. `restoreStock` is excluded because it was added by the Technical Leader during integration. |
| **Accessories** (Requirement 1) | `Accessory`, `Controller`, `Cable`, `Memory` | Inheritance, constructors, accessory-specific state, compatibility data and `getDescription()`. |
| **Promotions** (Requirement 2) | `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount` | Active-period rule and each `calculateDiscount(Sale)` implementation. |
| | `PromotionService.registerCategoryDiscount` | Category-target validation contributed by Developer 1; the rest of `PromotionService` belongs to Developer 2. |
| **Returns** (Requirement 3) | `com.gamezone.model.Return` | Constructor, return data, `calculateRefundAmount()` and `generateReturnReceipt()`. |
| | `com.gamezone.model.Sale.canBeReturned()` | Method-level audit, as assigned in Requirement 7. |
| **Warranties** (Requirement 4) | `Warranty`, `BasicWarranty`, `ExtendedWarranty` | Constructor, duration/type/cost behavior, `isActive()` and certificate generation. |
| **Exceptions and validations** (Requirement 6) | `GameZoneException`, `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException` | Exception hierarchy, constructors and error categorization. |
| | `PersonValidator`, `ProductValidator`, `SaleValidator` | Data, existence, uniqueness, stock and pre-sale validation responsibilities. |

---

## 2. Evaluation of GRASP Patterns

### 2.1 Information Expert

* **Verdict:** **Correct**

#### Well-Applied Evidence

* `Product` owns the common product attributes and behavior. `Videogame`, `Console`, and the accessory subclasses provide descriptions using their own specific data.
* Each promotion subclass implements `calculateDiscount(Sale)` according to its own rule. `Return.calculateRefundAmount()` uses the original sale and returned products to compute the refund.
* `Warranty` owns its validity dates and associated product. `BasicWarranty` and `ExtendedWarranty` provide their own duration and cost behavior.
* The validators inspect the data they need to validate. For example, `ProductValidator.validateStockAvailability()` checks the product's availability and the requested quantity.

The type checks in `CategoryDiscount` are recorded under Polymorphism, where the issue is how product variants are selected, rather than a lack of access to the information needed for the calculation.

### 2.2 Creator

* **Verdict:** **Correct**

#### Well-Applied Evidence

* `ProductService.registerVideoGame()` creates a `Videogame`, and `ProductService.registerConsole()` creates a `Console`. `ProductService` manages the product collection and persists it, so it has a reasonable creation responsibility for these entities.
* Validators create the corresponding typed exceptions when validation fails. This creation is directly tied to the validation decision they make.

### 2.3 Controller

* **Verdict:** **Correct**

#### Well-Applied Evidence

* `ProductService` receives and coordinates product use-case operations such as registration, listing and stock updates. The console UI delegates product operations to the service instead of implementing persistence itself.
* Validators and exception classes do not receive system events; they support validation and error reporting rather than acting as controllers.

### 2.4 Low Coupling

* **Verdict:** **Correct**

#### Well-Applied Evidence

* Product and promotion domain classes do not perform file I/O. `ProductRepository` encapsulates file operations, and `ProductService` receives the repository through its constructor.
* `ProductService` depends on one repository collaborator. The current evidence does not show excessive coupling in this product-module relationship.
* Validators depend on the domain objects and exception types needed to perform their checks. The exception subclasses depend on the common `GameZoneException` abstraction.

### 2.5 High Cohesion

* **Verdict:** **Violation**

#### Identified Violations

| Field | Finding D1-V03 |
| :--- | :--- |
| **ID** | `D1-V03` |
| **Pattern** | High Cohesion |
| **Module** | Products and validation (Workshop 1 / Requirement 6) |
| **Evidence** | `ProductService.registerVideoGame()` and `registerConsole()` call `validateCommonFields()`, which checks common product fields. `ProductValidator.validateProductData()` independently checks the identifier, title, price and stock, but `ProductService` does not delegate these checks to it. |
| **Explanation** | Common product-field validation is maintained in both `ProductService` and `ProductValidator`. The validator is presented as the central location for those rules, but the product-registration flow keeps a separate implementation. |
| **Consequence** | A future rule change could be applied in one location and missed in the other. It is also unclear which validation path is intended to be authoritative. |
| **Proposed Solution** | Have `ProductService` delegate common field checks to `ProductValidator`. Keep the unique-identifier rule in one clearly assigned location, either by adding it to the validator with the existing products as input or by documenting it as a separate service-level rule. Preserve the current registration behavior and error messages. |
| **Other Member's Code** | No other member's code is required for the product-validation correction. |

The return model keeps return data, refund calculation and receipt generation together. Since the system also generates a sales receipt from `Sale`, this audit does not classify receipt formatting alone as a confirmed cohesion violation.

### 2.6 Polymorphism

* **Verdict:** **Violation**

#### Identified Violations

| Field | Finding D1-V01 |
| :--- | :--- |
| **ID** | `D1-V01` |
| **Pattern** | Polymorphism |
| **Module** | Promotions and product hierarchy (Requirement 2) |
| **Evidence** | `CategoryDiscount.calculateDiscount()` compares `targetCategory` with string values and uses `instanceof` to distinguish `Videogame`, `Console` and `Accessory`. `PromotionService.registerCategoryDiscount()` also validates categories through string comparisons. |
| **Explanation** | Category-discount behavior depends on concrete product types and category names embedded in conditional logic. Adding another product category requires changing the existing discount logic. |
| **Consequence** | The promotion hierarchy does not fully use the product hierarchy to handle category variations, increasing the risk of inconsistent category behavior. |
| **Proposed Solution** | Give products a shared category contract, such as a category enum or a polymorphic category method. Have `CategoryDiscount` compare the product's category rather than inspect concrete classes. Use the same category definition for registration validation. |
| **Other Member's Code** | Coordinate with Developer 2 if the change modifies the signature of `PromotionService.registerCategoryDiscount()` or other parts of `PromotionService`. |

The category validation added to `PromotionService` rejects unsupported strings, but it does not remove the concrete-type checks from `CategoryDiscount`.

### 2.7 Pure Fabrication

* **Verdict:** **Correct**

#### Well-Applied Evidence

* `ProductRepository` is a fabricated class that encapsulates flat-file persistence instead of placing file operations in `Product` or its subclasses.
* `PersonValidator`, `ProductValidator` and `SaleValidator` centralize validation work that does not need to be implemented by the domain entities.
* The exception hierarchy provides distinct error types without adding error-reporting infrastructure to domain classes.

### 2.8 Indirection

* **Verdict:** **Violation**

#### Identified Violations

* **Related finding:** `D1-V03`.
* `ProductValidator` could serve as the indirection between product registration and common field-validation rules. However, `ProductService` performs its own `validateCommonFields()` checks rather than using `ProductValidator`.
* This is recorded as part of `D1-V03` rather than as a second finding, because both observations arise from the same split validation responsibility. The proposed correction is to route product registration through the shared validator while keeping identifier uniqueness in one defined location.

### 2.9 Protected Variations

* **Verdict:** **Violation**

#### Identified Violations

| Field | Finding D1-V02 |
| :--- | :--- |
| **ID** | `D1-V02` |
| **Pattern** | Protected Variations |
| **Module** | Warranty hierarchy (Requirement 4) |
| **Evidence** | The `Warranty` constructor calculates `endDate` by invoking the abstract `getDurationInMonths()` method. `BasicWarranty` and `ExtendedWarranty` override that method. |
| **Explanation** | The base-class constructor invokes behavior supplied by a subclass before that subclass has completed initialization. A future subclass implementation could depend on fields that are not initialized yet. |
| **Consequence** | Adding or changing a warranty subtype could make object construction or end-date calculation fragile. |
| **Proposed Solution** | Pass the duration into the `Warranty` constructor or calculate the end date after the subclass has been constructed. Preserve the existing six-month and twelve-month durations. |
| **Other Member's Code** | No other member's code is required for the warranty-hierarchy correction. |

The category strings and type checks in `CategoryDiscount` also expose a category variation point; this is cross-referenced to `D1-V01` rather than counted again.

---

## 3. Functional Observation Outside the GRASP Findings

`Sale.canBeReturned()` currently returns `true` unconditionally. This is recorded as a functional observation rather than a GRASP violation. The return period is also checked in `ReturnService`, so the team should agree on the authoritative location for the rule before making a behavior change.

---

## 4. Summary of Findings

| Finding ID | Pattern | Status | Assigned Module | Boundary Finding? |
| :--- | :--- | :--- | :--- | :--- |
| `D1-V01` | Polymorphism | **Violation** | Promotions and product hierarchy | Potential coordination with Developer 2 for `PromotionService` API changes |
| `D1-V02` | Protected Variations | **Violation** | Warranty hierarchy | No |
| `D1-V03` | High Cohesion; Indirection cross-reference | **Violation** | Product validation | No |

