# GameZoneUnicesar
Sistema de información en Java para la gestión de la tienda de videojuegos GameZone Unicesar. Proyecto implementado bajo una arquitectura de cuatro capas utilizando Maven.

## Funcionalidades Implementadas

### 🎮 Accessory Module (Módulo de Accesorios)
* **Accessory Management:** Supports the registration and inventory tracking of three new specific item types: Controllers, Cables, and Memories.
* **Console Compatibility:** Allows querying which accessories are compatible with a specific console before making a sale.
* **Unified Sales Integration:** Accessories can be sold alongside video games and consoles in a single transaction, with automated stock validation and deduction.
* **Categorized Inventory:** Users can view the complete inventory of accessories or filter them by their specific category.

### 🏷️ Promotion Module (Módulo de Promociones)
* **Discount Strategies:** Supports multiple types of promotions, including Percentage Discount, Category Discount (e.g., videogames only), and Bulk Purchase Discount.
* **Automatic Best Discount Application:** When a sale is processed, the system automatically evaluates all active promotions and applies the one that grants the highest monetary discount to the customer.
* **Validity Periods:** Promotions have start and end dates and are only applied if the current date falls within this active period.
* **Receipt Breakdown:** The sales receipt clearly displays the subtotal, the name and amount of the applied discount, and the final total to pay.

### 🔄 Return Module (Módulo de Devoluciones)
* **Return Management:** Allows processing partial or full returns of products from a specific previous sale, calculating the exact refund amount.
* **Strict Validations:** Enforces a 30-day return window policy and verifies that the returned items actually belong to the original transaction.
* **Automated Inventory:** Automatically restores the stock quantity of returned products in the system.
* **Monthly Balance Report:** Generates a financial summary for a specific month and year, displaying total sales, total returns, and the net balance.
* **Return Queries:** Supports querying the return history globally, by specific customer, or by the original sale ID.

### 🛡️ Warranty Module (Módulo de Garantías)
* **Warranty Management:** Automatically generates 6-month basic warranties for console purchases.
* **Extended Warranties:** Offers an optional 12-month extended warranty for consoles with an additional 10% cost during sales.
* **Warranty Tracking:** Allows querying active warranties, soon-to-expire warranties, and validating coverage based on product and sale IDs.

### 🔗 System Integration (Integración del Sistema - Requerimiento 5)
* **Unified Sales Flow:** Consolidates sales registration to handle products and accessories, apply the best promotion to the subtotal, compute warranty costs, and update respective inventories in a coordinated flow.
* **Category Discount Integration:** Extends Category Discounts to be applicable specifically to Accessories.
* **Integrated Returns & Refunds:** Accurately recalculates proportional refunds for items bought under a promotion. *(Note: Restoring accessory stock and automatically canceling warranties for returned consoles are pending implementation by other roles)*.
* **Unified Financial Reports:** *(Pending implementation by other roles: Generates an accurate monthly balance that integrates promotional discounts, extended warranty revenues, and valid returns).*

### ⚠️ Exception Handling & Validation Architecture (Módulo de Excepciones - Requerimiento 6)
* **Typed Custom Exception Hierarchy:** Base abstract runtime exception `GameZoneException` specialized into four domain-specific exceptions: `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, and `PersistenceException`.
* **Centralized Validation Layer:** Dedicated validator classes (`PersonValidator`, `ProductValidator`, `SaleValidator`) enforcing structural, existence, and business constraints before service execution.
* **Differentiated User-Friendly Messaging:** Refactored presentation layer (`ConsoleMenu`) catching typed exceptions with targeted, readable feedback for end users without cryptic stack traces.

### 📐 GRASP Patterns Audit & Architectural Refactoring (Requerimiento 7)
* **Comprehensive Design Pattern Audit:** In-depth evaluation of Craig Larman's 9 GRASP principles (Information Expert, Creator, Controller, Low Coupling, High Cohesion, Polymorphism, Pure Fabrication, Indirection, Protected Variations) across all domain, service, persistence, and presentation classes.
* **Consolidated Guiding Analysis:** Cross-module evaluation documenting boundary violations, non-regression verification protocol, and sequential git coordination.
* **Architectural Refactoring:**
  * **Information Expert:** Return eligibility window (30 calendar days) encapsulated in `Sale.canBeReturned()`.
  * **Creator:** Instantiation of sales aggregates and ID generation centralized in `SaleService.createSale()`.
  * **Controller & Indirection:** `SaleService` manages internal resolution of customer, seller, and product collaborators, exposing `listAllSales()` and `findById(id)` without UI mediation.
  * **Protected Variations:** Parameterized persistence file paths injected via constructor across all repositories, maintaining stable default paths.
* **Deterministic Non-Regression Strategy:** Documented in `docs/grasp-refactor-log.md` ensuring identical input/output operational behavior and zero compile-time warnings.
