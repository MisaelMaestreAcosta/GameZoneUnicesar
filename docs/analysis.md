# Domain Analysis - GameZone Unicesar

## 1. What attributes are common to all people interacting with the store, and which are specific to each type of person? How is this distinction reflected in a class hierarchy? 

**Answer:**
Common attributes: id, name, and phoneNumber.
Specific attributes: Customer has email and purchaseHistory; Seller has employeeCode and workShift.
Class hierarchy: An abstract Person base class stores the common fields, and Customer and Seller extend it to reuse code and represent the is-a relationship.

## 2. Should there be a class representing a "generic person" without specifying their role? Why or why not? What implication does this decision have on the possibility of instantiating said class? 
**Answer:**
Yes, a generic Person class is needed to centralize shared attributes and avoid code duplication. However, it must be declared abstract so it cannot be instantiated, because everyone in the store must have a specific role (Customer or Seller).

## 3. What characteristics do all products sold by the store have in common, regardless of their type? What characteristics are specific to each type of product?
**Answer:**
Common attributes: id, name, price, and stock.
Specific attributes: VideoGame has genre, platform, and developer; Console has brand and storageCapacity.
Implementation: An abstract Product class holds shared data, while VideoGame and Console inherit from it and add their own fields.

## 4. Each type of product must be able to present a description that integrates its specific characteristics. How should this behavior be declared in the base class to ensure that all subclasses implement it in their own way? What object-oriented programming mechanism allows this?
**Answer:**

Declaration: Declare an abstract method getDescription() in the Product class without a body.
OOP mechanism: Polymorphism and method overriding (@Override), which forces subclasses like VideoGame and Console to implement their own custom description logic.

## 5. [Copia aquí la pregunta 1 del documento PDF]
**Answer:** [Redacta tu respuesta en inglés aquí]

## 6. [Copia aquí la pregunta 2 del documento PDF]
**Answer:** [Redacta tu respuesta en inglés aquí]

## 7. [Copia aquí la pregunta 1 del documento PDF]
**Answer:** [Redacta tu respuesta en inglés aquí]

## 8. [Copia aquí la pregunta 2 del documento PDF]
**Answer:** [Redacta tu respuesta en inglés aquí]

## 9. [Copia aquí la pregunta 1 del documento PDF]
**Answer:** [Redacta tu respuesta en inglés aquí]

## 10. [Copia aquí la pregunta 2 del documento PDF]
**Answer:** [Redacta tu respuesta en inglés aquí]

## 11. [Copia aquí la pregunta 1 del documento PDF]
**Answer:** [Redacta tu respuesta en inglés aquí]
