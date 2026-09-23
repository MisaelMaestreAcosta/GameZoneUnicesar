## 1. What attributes are common to all people interacting with the store, and which are specific to each type of person? How is this distinction reflected in a class hierarchy?
**Answer:**
Common attributes: id, name, and phoneNumber. Specific attributes: Customer has email and purchaseHistory; Seller has employeeCode and workShift. Class hierarchy: An abstract Person base class stores the common fields, and Customer and Seller extend it to reuse code and represent the is-a relationship.

## 2. Should there be a class representing a "generic person" without specifying their role? Why or why not? What implication does this decision have on the possibility of instantiating said class?
**Answer:**
Yes, a generic Person class is needed to centralize shared attributes and avoid code duplication. However, it must be declared abstract so it cannot be instantiated, because everyone in the store must have a specific role (Customer or Seller).

## 3. What characteristics do all products sold by the store have in common, regardless of their type? What characteristics are specific to each type of product?
**Answer:**
Common attributes: id, name, price, and stock. Specific attributes: VideoGame has genre, platform, and developer; Console has brand and storageCapacity. Implementation: An abstract Product class holds shared data, while VideoGame and Console inherit from it and add their own fields.

## 4. Each type of product must be able to present a description that integrates its specific characteristics. How should this behavior be declared in the base class to ensure that all subclasses implement it in their own way? What object-oriented programming mechanism allows this?
**Answer:**
Declaration: Declare an abstract method getDescription() in the Product class without a body. OOP mechanism: Polymorphism and method overriding (@Override), which forces subclasses like VideoGame and Console to implement their own custom description logic.

## 5. A sale involves a customer, a salesperson, and one or more products. What types of relationships exist between the class representing the sale and the other classes in the system? Are these relationships inheritance, association, composition, or another type? Justify.
**Answer:**
The relationships between the Sale class and Customer or Salesperson are direct associations, as both customers and salespeople exist independently of any individual sale and maintain their own distinct lifecycles. In contrast, the relationship between Sale and its line items (SalesLineItem) is composition, because line items cannot exist without an overarching sale and are strictly bound to its lifecycle; destroying a sale destroys its line items. Finally, the relationship between each line item and a Product is an association, where line items reference products while products exist independently in the system catalog. None of these are inheritance relationships because a sale is not a specialized type of customer, seller, or product, but rather a transactional entity that links these domain concepts together.

## 6. Should the sale be responsible for calculating its own total, or should this responsibility belong to another class? Argue your decision.
**Answer:**
The Sale class should be responsible for calculating its own total, adhering directly to the Information Expert principle in Object-Oriented Design. Because Sale encapsulates the collection of SalesLineItem instances—each holding specific quantities, unit prices, and line discounts—it possesses or has direct access to all the necessary data required to compute the aggregate total. Under this design, high cohesion and encapsulation are preserved: the Sale delegates subtotal calculations to individual line items and sums the results, preventing external classes from needing to reach into the internal structure of the sale to perform calculations.

## 7. How is it ensured in the design that a sale cannot be registered without at least one product? At what point in the system should this rule be validated?
**Answer:**
This constraint is enforced through class invariants and behavioral encapsulation within the domain model, ensuring that a Sale cannot transition into a "Registered" or "Completed" state while its collection of line items is empty. At the implementation level, this rule must be validated inside the domain layer—specifically within the Sale class itself (e.g., inside a constructor, factory method, or the completeSale() domain method) before state changes are committed. Validating this rule directly inside the domain entity ensures that core business invariants are protected regardless of which external application service, controller, or UI workflow triggers the operation.

## 8. How is the automatic inventory update reflected in the design when a sale is registered? What classes are involved in this operation?
**Answer:**
The automatic inventory update is typically modeled using either an application orchestration flow or an Observer/Domain Event pattern to maintain clean separation between the sales and inventory domains. The primary classes involved are Sale, SalesLineItem, Inventory (or StockItem), and an InventoryService (or SaleRegisteredEventHandler). When Sale.register() executes, it raises a SaleRegisteredEvent containing the purchased products and quantities, which an InventoryService receives to look up the relevant Inventory aggregates and invoke stock deduction methods (e.g., inventory.deductStock(productId, quantity)), keeping the sales aggregate decoupled from stock management logic.

## 9. The system must be organized into four layers: model, persistence, services, and user interface. What types of classes belong to each layer? What criterion determines which layer a class should be placed in?
**Answer:**
Model: Here are the classes representing the system's main elements, such as Console, Product, Person, videogame, etc. Persistence: Here you will find the classes responsible for saving and retrieving information. Service: This is where the system's logic resides—that is, the classes that execute the program's operations and rules. To determine where a class belongs, I primarily look at its responsibility. The class should be placed in the layer that corresponds to the function it performs.

## 10. Why shouldn't the logic for saving and retrieving data from files be placed within domain classes? What problems arise when these responsibilities are mixed?
**Answer:**
- Because domain classes should be responsible for representing the system's objects and rules, not for knowing how data is stored in files.
- If we mix these responsibilities, the code becomes harder to understand and maintain. Furthermore, if the data storage method changes later, the domain classes would also have to be modified.

## 11. Which dependencies are permitted between the layers, and which are prohibited? Justify the rationale behind the permitted dependencies.
**Answer:**
Permitted dependencies must follow the layering order. The user interface can use the services, the services can use the model and the persistence layer, and the persistence layer can work with the model. This ensures that each layer has its own responsibility and makes the system easier to modify and maintain. Thus, if I change the interface or the way data is saved, I do not have to change the entire program.

## 12. Should accessories be integrated into the existing product hierarchy (extending Product) or should they form an independent hierarchy? Justify your decision considering code reuse and model coherence.
**Answer:**
## Accessories should form an independent hierarchy that inherits from an abstract Accessory class, because although they share attributes with Product such as id, title, price, and stockQuantity, an accessory is not a sellable product of the same category as a video game or a console and has its own features such as console compatibility that do not apply to base products, and if more accessory types are added in the future or their rules change the modifications remain isolated in their own hierarchy without affecting existing products, achieving reuse through composition or by duplicating the minimal common attributes in the Accessory base class, so an independent Accessory hierarchy is created with the subclasses Controller, Cable, and Memory while keeping the Product hierarchy intact.

## 13.  What attributes are common to the three types of accessories and which are specific to each type? How is this distinction reflected in the module's class hierarchy?
**Answer:**
## The common attributes to the three types of accessories are id, title, price, stockQuantity, and compatibleConsoles which are declared in the abstract base class Accessory, while the specific attributes are connectionType for Controller, lengthInMeters and connectorType for Cable, and capacityInGB and memoryType for Memory, and this distinction is reflected by creating an abstract class Accessory containing the common attributes and three concrete subclasses Controller, Cable, and Memory that inherit from it and add their own attributes, which allows code reuse and correctly models each type of accessory.


## 14.  
**Answer:**
## 

## 15. 
**Answer:**
##

## 16. In which layer of the system architecture should the new classes of the accessories module be placed? Justify your decision based on the responsibilities of each layer. 
**Answer**
## The new classes accessory, controller, Cable, and Memory belong to the Model Layer. They represent domain entities and encapsulate business data without handling operations.

## 17. The three promotions have different calculation rules but share common attributes and behaviors. How is this situation reflected in the class hierarchy design? What object-oriented programming mechanism allows each promotion type to calculate its discount differently without the rest of the system needing to know the concrete types?
**Answer:**
## This situation is reflected by creating an abstract base class called Promotion that contains the common attributes such as id, name, startDate, and endDate, along with the concrete method isActive that checks whether the promotion is active on a given date, and by declaring an abstract method calculateDiscount(Sale sale) that each subclass must mandatorily implement, so that the subclasses PercentageDiscount, CategoryDiscount, and BulkPurchaseDiscount inherit from Promotion and each one provides its own discount calculation logic according to its specific rules, and the object-oriented programming mechanism that allows this is polymorphism together with abstract methods, because when the system invokes calculateDiscount on a Promotion reference without knowing whether it is a percentage, category, or bulk discount, Java resolves at runtime which implementation to execute based on the actual type of the object, which allows the rest of the system to work with the Promotion abstraction without needing to know the concrete types or use conditionals to distinguish them.


## 18.   The base class Promotion cannot implement the discount calculation method because each type has a different logic. How is this method declared in the base class and what does this declaration guarantee regarding the subclasses?
**Answer:**
## The method is declared in the Promotion base class as an abstract method with the signature public abstract double calculateDiscount(Sale sale), without a body or implementation, which guarantees that all concrete subclasses that inherit from Promotion are obligated to provide their own implementation of this method, because if a subclass does not implement it the Java compiler generates an error and does not allow the class to compile, and this declaration also guarantees that the system can invoke calculateDiscount polymorphically on any Promotion reference ensuring that an implementation is always available at runtime, in addition to establishing a clear contract that forces all promotions in the system without exception to know how to calculate their discount on a sale.

## 19.
**Answer:**
## 

## 20.
**Answer:**
##

## 21. Active promotions are determined by comparing the current date with the start and end dates of each promotion. Where is this validation performed (in the `Promotion` class, in the `PromotionService`, or in both)? Justify your answer.
**Answer**
## Validity validation must be handled across both classes, which have complementary responsibilities: the `Promotion` class implements the atomic domain rule via the `isActive(LocalDate date)` method—since it owns the start and end date data—while `PromotionService` manages orchestration at the service layer by invoking that method with the current date when filtering promotions for operations such as `listActivePromotions()` or `findBestPromotionFor(sale)`. This distribution is valid because it adheres to the Information Expert principle (GRASP) by keeping the evaluation logic within the entity that holds the dates, and it maintains a separation of concerns by preventing the model class from depending on the system clock or list management, delegating that business flow to the service instead.

## 22. A return is a new system entity that refers to an existing sale. What type of relationship exists between the `Return` class and the `Sale` class? Is this relationship one of inheritance, association, aggregation, or composition? Justify your answer.
**Answer:**
## The relationship between these classes would be one of composition, since the `Return` class would not exist without the `Sale` class;
## without `Sale`, there is no `Return`.
## 23. A return may contain only some of the products from the original sale, not necessarily all of them. How is this situation represented in the attributes of the `Return` class? What is stored in the `returned products` attribute?
**Answer:**

## It is represented by a `List` attribute that stores the reason for the return (string) and the return amount (double).


## 24. The business rule states that returns can only be registered within 30 days of the sale. In which system layer is this validation located, and why? What Java mechanism is used to calculate the difference between two dates?
**Answer:**
## This is located in the service layer, where the return logic is implemented; the system validates all relevant aspects and the conditions required for the return to proceed. We can use the `LocalDate` class to calculate the difference between dates.
## 25. Returning products increases stock levels. Which existing method from the Workshop 1 system is reused for this operation, and in which class is it invoked from the returns module? Why is it important to reuse existing methods instead of duplicating the stock update logic?
**Answer:**

## When a product is returned and stock is increased in the `productService` class, the `updateStock` method is used to handle that logic. Reusing code is important for optimizing the application.
## 26. The monthly summary report requires consolidating information from two distinct modules (sales and returns). In which type of service is this report located, and why is this placement consistent with the layered architecture? What dependencies does this class require to generate the report?
**Answer**
## It is located in the services package, which is consistent with the layered architecture, as the service layer is responsible for the project's business logic.
## The dependencies required to create the monthly summary involve calculating the total sales for the specified month and year, as well as the total returns for that same period.

## 27. The two types of warranty have common attributes (dates, associated product) but also different attributes and behaviors (duration, coverage, cost). How is this situation reflected in the class hierarchy design? What object-oriented programming mechanism allows each warranty type to have its own duration without duplicating code?  

**Answer:**

## This situation is reflected by creating an abstract base class called Warranty that contains the common attributes such as id, product, sale, startDate, and endDate, along with concrete methods like isActive and generateWarrantyCertificate, and by declaring abstract methods getDurationInMonths, getWarrantyType, and getAdditionalCost that each subclass must implement. The subclasses BasicWarranty and ExtendedWarranty inherit from Warranty and each one provides its own duration and cost logic according to its specific rules. The mechanism that allows this is polymorphism together with abstract methods, because when the constructor of Warranty invokes getDurationInMonths to calculate the endDate, Java resolves at runtime which implementation to execute based on the actual type of the object, allowing each warranty type to define its own duration without duplicating code

## 28. The business rule establishes that only consoles generate automatic basic warranty, not video games. In which layer of the system is this decision located and what Java mechanism is used to verify the real type of a product? Justify

**Answer:**

## This decision is located in the service layer, specifically in the SaleService.registerSale method, because it is a business rule that orchestrates the sale registration process and must determine which products require automatic warranty generation. The Java mechanism used to verify the real type of a product is the instanceof operator, which allows checking whether an object is an instance of a particular class, so when iterating over the products of the sale the system evaluates if product instanceof Console and only in that case invokes WarrantyService.assignBasicWarranty to generate the basic warranty, while video games are skipped. This belongs to the service layer because it is a business rule that coordinates actions between entities and services, not a responsibility of the Product class itself


## 29.  
**Answer:**

## 

## 30. 

**Answer:**

##


## 31. The query for "guarantees nearing expiration" requires iterating over all guarantees and filtering for those with an end date within the next 30 days. In which class should this method be placed, and what dependencies does it require? Why? Is this placement consistent with a layered architecture?

**Answer**

## This method resides in the `WarrantyService` class and relies on `WarrantyRepository` to retrieve the list of warranties. It aligns with the layered architecture because the querying, filtering, and date-range calculations constitute application or business logic, rather than presentation or atomic model logic
