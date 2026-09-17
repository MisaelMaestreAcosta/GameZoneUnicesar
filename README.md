# GameZoneUnicesar
Sistema de información en Java para la gestión de la tienda de videojuegos GameZone Unicesar. Proyecto implementado bajo una arquitectura de cuatro capas utilizando Maven.


### 🎮 Accessory Module (Módulo de Accesorios)
* **Accessory Management:** Supports the registration and inventory tracking of three new specific item types: Controllers, Cables, and Memories.
* **Console Compatibility:** Allows querying which accessories are compatible with a specific console before making a sale.
* **Unified Sales Integration:** Accessories can be sold alongside video games and consoles in a single transaction, with automated stock validation and deduction.
* **Categorized Inventory:** Users can view the complete inventory of accessories or filter them by their specific category.

* **Return Management:** Allows processing partial or full returns of products from a specific previous sale, calculating the exact refund amount.
* **Strict Validations:** Enforces a 30-day return window policy and verifies that the returned items actually belong to the original transaction.
* **Automated Inventory:** Automatically restores the stock quantity of returned products in the system.
* **Monthly Balance Report:** Generates a financial summary for a specific month and year, displaying total sales, total returns, and the net balance.
* **Return Queries:** Supports querying the return history globally, by specific customer, or by the original sale ID.

