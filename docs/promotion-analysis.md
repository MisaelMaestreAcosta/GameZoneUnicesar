## 1. The three promotions have different calculation rules but share common attributes and behaviors. How is this situation reflected in the class hierarchy design? What object-oriented programming mechanism allows each promotion type to calculate its discount differently without the rest of the system needing to know the concrete types?
**Answer:**
## This situation is reflected by creating an abstract base class called Promotion that contains the common attributes such as id, name, startDate, and endDate, along with the concrete method isActive that checks whether the promotion is active on a given date, and by declaring an abstract method calculateDiscount(Sale sale) that each subclass must mandatorily implement, so that the subclasses PercentageDiscount, CategoryDiscount, and BulkPurchaseDiscount inherit from Promotion and each one provides its own discount calculation logic according to its specific rules, and the object-oriented programming mechanism that allows this is polymorphism together with abstract methods, because when the system invokes calculateDiscount on a Promotion reference without knowing whether it is a percentage, category, or bulk discount, Java resolves at runtime which implementation to execute based on the actual type of the object, which allows the rest of the system to work with the Promotion abstraction without needing to know the concrete types or use conditionals to distinguish them.


## 2.   The base class Promotion cannot implement the discount calculation method because each type has a different logic. How is this method declared in the base class and what does this declaration guarantee regarding the subclasses?
**Answer:**
## The method is declared in the Promotion base class as an abstract method with the signature public abstract double calculateDiscount(Sale sale), without a body or implementation, which guarantees that all concrete subclasses that inherit from Promotion are obligated to provide their own implementation of this method, because if a subclass does not implement it the Java compiler generates an error and does not allow the class to compile, and this declaration also guarantees that the system can invoke calculateDiscount polymorphically on any Promotion reference ensuring that an implementation is always available at runtime, in addition to establishing a clear contract that forces all promotions in the system without exception to know how to calculate their discount on a sale.

## 3.
**Answer:**
## 

## 4.
**Answer:**
##

## 5. Active promotions are determined by comparing the current date with the start and end dates of each promotion. Where is this validation performed (in the `Promotion` class, in the `PromotionService`, or in both)? Justify your answer.
**Answer**
## Validity validation must be handled across both classes, which have complementary responsibilities: the `Promotion` class implements the atomic domain rule via the `isActive(LocalDate date)` method—since it owns the start and end date data—while `PromotionService` manages orchestration at the service layer by invoking that method with the current date when filtering promotions for operations such as `listActivePromotions()` or `findBestPromotionFor(sale)`. This distribution is valid because it adheres to the Information Expert principle (GRASP) by keeping the evaluation logic within the entity that holds the dates, and it maintains a separation of concerns by preventing the model class from depending on the system clock or list management, delegating that business flow to the service instead.