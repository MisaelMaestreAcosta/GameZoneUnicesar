# Integration Analysis

This document describes the adjustments made to integrate the Accessory, Promotion, Warranty, and Return modules into a unified GameZone Unicesar system.

## A1 - Accessory category discount
**Cause:** The Promotion module (Requirement 2) initially limited `CategoryDiscount` strictly to "VIDEOGAME" and "CONSOLE" target categories. With the Accessory module integrated, the store needed the ability to launch promotions for accessories as well.
**Applied Solution:** Modified `CategoryDiscount` to accept "ACCESSORY" as a target category and implemented logic in `calculateDiscount` to recognize `Accessory` instances. Updated `PromotionService.registerCategoryDiscount` to validate the new category, included the option in `ConsoleMenu`, and added a preloaded promotion in `data/promotions.txt`.

## A2 - Circular dependency in warranty module
**Cause:** Requirement 4's specifications caused a cyclic dependency during object instantiation: `SaleService` -> `WarrantyService` -> `WarrantyRepository` -> `SaleService`. This occurred because `WarrantyRepository` attempted to resolve full object references during data loading, preventing constructor injection in `Main`.
**Applied Solution:** *(Pending implementation by another role)* The cycle currently remains in the code.

## A3 - Unified sale registration flow
**Cause:** Requirements 1, 2, and 4 each modified `SaleService.registerSale` independently. When integrated, the execution order of these operations (such as applying discounts vs. calculating warranty costs) directly impacted the final results.
**Applied Solution:** Reorganized the `registerSale` method into a unified flow: 1) Validate items. 2) Verify stock availability. 3) Calculate subtotal. 4) Apply best promotion based strictly on subtotal. 5) Assign basic and extended warranties. 6) Update inventory via respective services. 7) Persist sale (with `SaleRepository` updated to save promotion name, discount, and warranty cost so history and returns work correctly).

## A4 - Accessory return
**Cause:** The Return module (Requirement 3) originally only restored stock by invoking `ProductService.restoreStock`. Since `ProductService` does not manage `Accessory` inventory, returning an accessory did not restore its stock.
**Applied Solution:** *(Pending implementation by another role)* `ReturnService` currently only restores stock for `Product` instances.

## A5 - Discounted refund
**Cause:** The `Return.calculateRefundAmount` method simply summed the list prices of returned items. If the original sale included a promotion, the system would incorrectly refund more money than the customer actually paid.
**Applied Solution:** Modified `calculateRefundAmount` to calculate refunds proportionally based on the discount applied to the original sale using the formula: `price * (1 - (discount / subtotal))`. This works reliably even after reloading sales thanks to the persistence updates in A3.

## A6 - Monthly balance report
**Cause:** `generateMonthlyBalance` only calculated the net balance. However, Requirement 3 mandated displaying total sales, total returns, and the net balance. Additionally, the integrated system needed these calculations to account for promotion discounts and extended warranty costs.
**Applied Solution:** *(Pending implementation by another role)* `ConsoleMenu` currently calculates these totals independently in the UI layer.

## A7 - Warranty cancellation on console return
**Cause:** The original requirements did not define what should happen to an active warranty when its associated console is returned. A returned console should not retain an active warranty.
**Applied Solution:** *(Pending implementation by another role)* Warranties are not currently canceled upon return.
