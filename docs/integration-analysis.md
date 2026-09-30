# Integration Analysis

This document describes the adjustments made to integrate the Accessory, Promotion, Warranty, and Return modules into a unified GameZone Unicesar system.

## A1 - Accessory category discount
**Cause:** The Promotion module (Requirement 2) initially limited `CategoryDiscount` strictly to "VIDEOGAME" and "CONSOLE" target categories. With the Accessory module integrated, the store needed the ability to launch promotions for accessories as well.
**Applied Solution:** Modified `CategoryDiscount` to accept "ACCESSORY" as a target category and implemented logic in `calculateDiscount` to recognize `Accessory` instances. Updated `PromotionService.registerCategoryDiscount` to validate the new category, included the option in `ConsoleMenu`, and added a precargada promotion in `data/promotions.csv`.

## A2 - Circular dependency in warranty module
**Cause:** Requirement 4's specifications caused a cyclic dependency during object instantiation: `SaleService` -> `WarrantyService` -> `WarrantyRepository` -> `SaleService`. This occurred because `WarrantyRepository` attempted to resolve full object references during data loading, preventing constructor injection in `Main`.
**Applied Solution:** Changed `WarrantyRepository` to only persist and load `saleId` and `productId`, completely removing its dependency on `SaleService`. `WarrantyService` was updated to resolve these object references using its injected `WarrantyRepository`, `SaleRepository`, and `ProductService`.

## A3 - Unified sale registration flow
**Cause:** Requirements 1, 2, and 4 each modified `SaleService.registerSale` independently. When integrated, the execution order of these operations (such as applying discounts vs. calculating warranty costs) directly impacted the final results.
**Applied Solution:** Reorganized the `registerSale` method into a unified flow: 1) Validate items. 2) Resolve items and validate stock. 3) Calculate subtotal. 4) Apply best promotion based strictly on subtotal. 5) Generate and sum warranties. 6) Calculate final total. 7) Update inventory via respective services. 8) Persist sale and warranties.

## A4 - Accessory return
**Cause:** The Return module (Requirement 3) originally only restored stock by invoking `ProductService.restoreStock`. Since `ProductService` does not manage `Accessory` inventory, returning an accessory did not restore its stock.
**Applied Solution:** Injected `AccessoryService` into `ReturnService`. Added a `restoreStock(String accessoryId, int quantity)` method to `AccessoryService`. Updated `ReturnService` to delegate stock restoration to either `ProductService` or `AccessoryService` depending on the type of the returned item.

## A5 - Discounted refund
**Cause:** The `Return.calculateRefundAmount` method simply summed the list prices of returned items. If the original sale included a promotion, the system would incorrectly refund more money than the customer actually paid.
**Applied Solution:** Modified `calculateRefundAmount` to calculate refunds proportionally based on the discount applied to the original sale using the formula: `price * (1 - (discount / subtotal))`. Updated receipts to reflect this proportional calculation.

## A6 - Monthly balance report
**Cause:** `generateMonthlyBalance` only calculated the net balance. However, Requirement 3 mandated displaying total sales, total returns, and the net balance. Additionally, the integrated system needed these calculations to account for promotion discounts and extended warranty costs.
**Applied Solution:** Added `calculateMonthlySales` and `calculateMonthlyReturns` to `ReturnService`, ensuring total sales used the final total of each sale (including discounts and warranties). `generateMonthlyBalance` now returns the difference between these two new methods, and `ConsoleMenu` was updated to display all three figures.

## A7 - Warranty cancellation on console return
**Cause:** The original requirements did not define what should happen to an active warranty when its associated console is returned. A returned console should not retain an active warranty.
**Applied Solution:** Added `cancelWarranties(String productId, String saleId)` to `WarrantyService` to delete associated warranties and return the refundable cost (zero for basic, full additional cost for extended). `ReturnService.registerReturn` was updated to invoke this for each returned console and add the returned value to the total refund amount.
