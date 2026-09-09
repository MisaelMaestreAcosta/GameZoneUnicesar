## 1. A return is a new system entity that refers to an existing sale. What type of relationship exists between the `Return` class and the `Sale` class? Is this relationship one of inheritance, association, aggregation, or composition? Justify your answer.
**Answer:**
The relationship between these classes would be one of composition, since the `Return` class would not exist without the `Sale` class;
without `Sale`, there is no `Return`.
## 2. A return may contain only some of the products from the original sale, not necessarily all of them. How is this situation represented in the attributes of the `Return` class? What is stored in the `returned products` attribute?
**Answer:**
Se representa con un atributo tipo List donde se almacena el motivo de la devolucion (string) y el monto de
la devolucion tipo (double).
## 3. The business rule states that returns can only be registered within 30 days of the sale. In which system layer is this validation located, and why? What Java mechanism is used to calculate the difference between two dates?
**Answer:**
## This is located in the service layer, where the return logic is implemented; the system validates all relevant aspects and the conditions required for the return to proceed. We can use the `LocalDate` class to calculate the difference between dates.
## 4. Returning products increases stock levels. Which existing method from the Workshop 1 system is reused for this operation, and in which class is it invoked from the returns module? Why is it important to reuse existing methods instead of duplicating the stock update logic?
**Answer:**
## When a product is returned and stock is increased in the `productService` class, the `updateStock` method is used to handle that logic. Reusing code is important for optimizing the application.##