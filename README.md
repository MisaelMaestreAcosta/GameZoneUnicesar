# GameZoneUnicesar
Sistema de información en Java para la gestión de la tienda de videojuegos GameZone Unicesar. Proyecto implementado bajo una arquitectura de cuatro capas utilizando Maven.
* **Return Management:** Allows processing partial or full returns of products from a specific previous sale, calculating the exact refund amount.
* **Strict Validations:** Enforces a 30-day return window policy and verifies that the returned items actually belong to the original transaction.
* **Automated Inventory:** Automatically restores the stock quantity of returned products in the system.
* **Monthly Balance Report:** Generates a financial summary for a specific month and year, displaying total sales, total returns, and the net balance.
* **Return Queries:** Supports querying the return history globally, by specific customer, or by the original sale ID.