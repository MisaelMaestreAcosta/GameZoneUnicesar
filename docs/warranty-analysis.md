## 1. The two types of warranty have common attributes (dates, associated product) but also different attributes and behaviors (duration, coverage, cost). How is this situation reflected in the class hierarchy design? What object-oriented programming mechanism allows each warranty type to have its own duration without duplicating code?  

**Answer:**

## This situation is reflected by creating an abstract base class called Warranty that contains the common attributes such as id, product, sale, startDate, and endDate, along with concrete methods like isActive and generateWarrantyCertificate, and by declaring abstract methods getDurationInMonths, getWarrantyType, and getAdditionalCost that each subclass must implement. The subclasses BasicWarranty and ExtendedWarranty inherit from Warranty and each one provides its own duration and cost logic according to its specific rules. The mechanism that allows this is polymorphism together with abstract methods, because when the constructor of Warranty invokes getDurationInMonths to calculate the endDate, Java resolves at runtime which implementation to execute based on the actual type of the object, allowing each warranty type to define its own duration without duplicating code

## 2. The business rule establishes that only consoles generate automatic basic warranty, not video games. In which layer of the system is this decision located and what Java mechanism is used to verify the real type of a product? Justify

**Answer:**

## This decision is located in the service layer, specifically in the SaleService.registerSale method, because it is a business rule that orchestrates the sale registration process and must determine which products require automatic warranty generation. The Java mechanism used to verify the real type of a product is the instanceof operator, which allows checking whether an object is an instance of a particular class, so when iterating over the products of the sale the system evaluates if product instanceof Console and only in that case invokes WarrantyService.assignBasicWarranty to generate the basic warranty, while video games are skipped. This belongs to the service layer because it is a business rule that coordinates actions between entities and services, not a responsibility of the Product class itself


## 3.  The duration of each warranty type is different (6 months or 12 months). How is the expiration date calculated in each subclass? Should this calculation be done in the warranty constructor or in a separate method? Justify.
**Answer:**

## Each Warranty subclass supplies its own duration (6 or 12 months) through an abstract method such as getDurationInMonths(), and the parent class computes the expiration date as startDate.plusMonths(getDurationInMonths()). This should live in a separate method like getExpirationDate(), not in the constructor, because calling an overridable method from a constructor is risky (the subclass may not be initialized yet), and a separate method keeps the constructor limited to assigning state and is easier to test.

## 4. The extended warranty adds a cost of 10% of the product price to the sale total. At what point in the sale registration flow is this additional cost calculated and applied? What modifications are needed in the SaleService.registerSale method?

**Answer:**

## The cost is calculated inside SaleService.registerSale, after the product subtotal is known and before promotions are applied and the sale is persisted, since the service layer is the only place that has both the product price and the chosen warranty. A method such as getCost(productPrice) in the warranty hierarchy returns 0 for the standard warranty and 10% of the price for the extended one, so registerSale just adds that value to the total (ideally as its own field so the receipt can show it) without checking types. Existing behavior is preserved because sales without a warranty, or with a standard one, add zero.


## 5. The query for "guarantees nearing expiration" requires iterating over all guarantees and filtering for those with an end date within the next 30 days. In which class should this method be placed, and what dependencies does it require? Why? Is this placement consistent with a layered architecture? 
**Answer:**
##
## 5. The query for "guarantees nearing expiration" requires iterating over all guarantees and filtering for those with an end date within the next 30 days. In which class should this method be placed, and what dependencies does it require? Why? Is this placement consistent with a layered architecture?

**Answer**

## This method resides in the `WarrantyService` class and relies on `WarrantyRepository` to retrieve the list of warranties. It aligns with the layered architecture because the querying, filtering, and date-range calculations constitute application or business logic, rather than presentation or atomic model logic
