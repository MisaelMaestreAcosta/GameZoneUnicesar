# GRASP Refactor Log

| Clase | Variación Implementada | ID de Variación | Principio de Diseño | Commit / PR | Responsable |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **PersonRepository** | Paths injected through constructor, defaults kept | D2-V03 | Protected Variations | `75a523e` / PR #34 | Developer 2 |
| **AccessoryRepository** | File path injected through constructor, default path preserved | D2-V06 | Protected Variations | `b8cdb41` / PR #34 | Developer 2 |
| **PromotionRepository** | File path injected through constructor, default path preserved | D2-V08 | Protected Variations | `3d395b3` / PR #34 | Developer 2 |
| **ReturnRepository** | File path injected through constructor, default path preserved | D2-V14 | Protected Variations | `a2fd117` / PR #34 | Developer 2 |
| **WarrantyRepository** | File path injected through constructor, default path preserved | D2-V17 | Protected Variations | `0c8ccc5` / PR #34 | Developer 2 |
| **Sale** & **ConsoleMenu** | Encapsulated 30-day return eligibility rule in `Sale.canBeReturned()`, removing UI calendar arithmetic | L-V01 | Information Expert | `97cb799` / PR #34 | Technical Leader |
| **SaleService** & **ConsoleMenu** | Implemented `createSale(Customer, Seller)` factory in `SaleService`, shielding UI from entity instantiation | L-V03 | Creator | `fc40f42` / PR #34 | Technical Leader |
| **SaleService**, **ConsoleMenu** & **main** | Injected `PersonService` into `SaleService`, provided parameterless `listAllSales()` and implemented `findById(id)` | L-V04, L-V11 | Controller & Indirection | `c52bfc2` / PR #34 | Technical Leader |

---

## Non-regression strategy

To guarantee that the responsibility reassignment and design pattern refactoring do not alter the system's observable behavior, the team established a deterministic non-regression protocol:

1. **Compilation and Build Integrity:**
   * Automated verification using `mvn clean compile` to ensure strict type compliance, binary compatibility, and zero compile-time warnings or errors across all layers (`ui`, `service`, `persistence`, `model`, `exceptions`).

2. **Console Menu End-to-End Test Suite:**
   The Technical Leader executes the complete operational flow using fixed benchmark input data:
   * **Product & Accessory Catalog:** Verifies that all video games, consoles, and accessories load from flat files and render identical attributes and formatted descriptions.
   * **Person Listing:** Verifies customer and seller retrieval and ensures fallback initialization (`initializeDefaultSellers()`) continues working without errors.
   * **Transaction Lifecycle (Sale Registration):** Registers a sale with a known customer, seller, product, and extended warranty. Confirms that stock is decremented accurately and the receipt format is preserved.
   * **Sales History Display:** Confirms reconstructed transactions render identical receipt summaries without UI mediation across multiple services.
   * **Return Processing:** Attempts a return for an existing transaction within the 30-day window and validates boundary rejection for sales older than 30 days. Verifies that inventory restoration and return records are saved to `data/returns.txt`.
   * **Financial Balance Generation:** Calculates the net balance for the target month/year and confirms mathematical equality between total sales, total returns, and net revenue.

3. **Data File Persistence Verification:**
   * Line-by-line verification of output files in the `data/` directory (`customers.txt`, `sellers.txt`, `products.txt`, `accessories.txt`, `promotions.txt`, `sales.txt`, `returns.txt`, `warranties.txt`) against reference snapshots taken from `develop`.

---

## Baseline result (develop, before the first correction)

* **Baseline Commit:** `412e112` (`origin/develop`, post-merge of `feature/grasp-audit`).
* **Initial Test Run Execution:**
  * All 7 console submenus and use cases executed successfully with sample data.
  * Persistence files correctly loaded and stored delimited text records using default paths.
  * Console outputs matched the baseline expectations documented in Workshop 1 and Requirements 1 through 6.
  * Identified structural liabilities: UI was tightly coupled to entity creation and date arithmetic, repository file paths were hardcoded static constants, and sales query lookups required manual three-service orchestration in `ConsoleMenu`.

---

## Out-of-scope findings

During the GRASP audit, several functional limitations and legacy defects were cataloged. Because Requirement 7 focuses strictly on GRASP responsibility assignment and structural refactoring without breaking legacy input contracts, the following items are formally classified as out of scope:

1. **Delimiter Collision in Flat-File Serialization:** Names, descriptions, or addresses containing commas or pipe symbols (`|`) can cause parsing shifts in `split(",")`. Correcting this requires a new CSV escaping protocol, which is deferred to maintain backward compatibility.
2. **Inconsistent Error Handling in Repositories:** `PersonRepository` logs `IOException` and continues, while subsequent repositories throw runtime exceptions. Unification of persistence exception handling is deferred to the SOLID/architecture hardening phase.
3. **Case Sensitivity in ID Lookups:** Searches perform case-insensitive comparisons, whereas file records persist the exact case entered by the user. Preserved as-is.
4. **Promotion Date Semantics:** `listActivePromotions()` checks `LocalDate.now()` whereas `findBestPromotionFor()` checks `Sale.getDate()`. Maintained to preserve the dual use-case contract.

---

## Final verification

* **Target Commit:** Head of `feature/grasp-refactor`.
* **Build Status:** `mvn clean compile` succeeded with `BUILD SUCCESS` (0 errors, 0 failures).
* **Operational Verification Results:**
  * Injected repository constructors (`PersonRepository`, `AccessoryRepository`, `PromotionRepository`, `ReturnRepository`, `WarrantyRepository`) operate transparently using both default and custom paths.
  * `Sale.canBeReturned()` correctly enforces the 30-day policy rule from the domain model, eliminating presentation-layer date math.
  * `SaleService.createSale()` centralizes entity construction and unique identifier generation (`V-XXXXX`), decoupling the UI from domain entity constructors.
  * `SaleService.findById()` and `SaleService.listAllSales()` resolve customer, seller, and product collaborators internally, completely removing three-way orchestration from `ConsoleMenu`.
  * All console receipts, balance summaries, and text files in `data/` match baseline snapshots verbatim.
