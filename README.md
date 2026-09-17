# GameZoneUnicesar
Sistema de información en Java para la gestión de la tienda de videojuegos GameZone Unicesar. Proyecto implementado bajo una arquitectura de cuatro capas utilizando Maven.

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