# SOLID Refactor Log

## Phase 2: Refactoring Log

| Class | Change Made | Violation ID | Principle Applied | Commit / PR | Responsible |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `PersonService.java`, `PersonRepository.java` | Introduced `IPersonRepository` interface and updated `PersonService` to depend on the abstraction instead of the concrete class. | V-02 | Dependency Inversion Principle (DIP) | `refactor: decouple PersonService by injecting IPersonRepository interface (V-02)` | Developer 2 |
| `SaleService.java`, `SaleRepository.java`, `ISaleRepository.java` | Introduced `ISaleRepository` interface and updated `SaleService` constructors and field to depend on the abstraction instead of the concrete persistence class. | L-S04 | Dependency Inversion Principle (DIP) | `378dcab` / PR | Technical Leader |
| `Sale.java`, `SaleReceiptFormatter.java` | Extracted textual receipt formatting logic into dedicated `SaleReceiptFormatter`, leaving `Sale` strictly responsible for business calculations. | L-S02 | Single Responsibility Principle (SRP) | `9c49e81` / PR | Technical Leader |

---

## Non-Regression Verification
* Automated compilation check: `mvn clean compile` succeeded with `BUILD SUCCESS` across all 39 source files.
* Transactional output stability: `Sale.generateReceipt()` preserves identical ASCII header layout and currency output formatting via delegation to `SaleReceiptFormatter`.
* Persistence compatibility: `SaleRepository` implements `ISaleRepository` maintaining identical delimited record serialization to `data/sales.txt`.
