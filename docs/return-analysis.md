## 1. A return is a new system entity that refers to an existing sale. What type of relationship exists between the `Return` class and the `Sale` class? Is this relationship one of inheritance, association, aggregation, or composition? Justify your answer.
**Answer:**
The relationship between these classes would be one of composition, since the `Return` class would not exist without the `Sale` class;
without `Sale`, there is no `Return`.
## 2. A return may contain only some of the products from the original sale, not necessarily all of them. How is this situation represented in the attributes of the `Return` class? What is stored in the `returned products` attribute?
**Answer:**
Se representa con un atributo tipo List donde se almacena el motivo de la devolucion (string) y el monto de
la devolucion tipo (double).