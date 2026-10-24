\# SOLID Refactor Log



\## Phase 2: Refactoring Log



| Class | Change Made | Violation ID | Principle Applied | Commit / PR | Responsible |

| :--- | :--- | :--- | :--- | :--- | :--- |

| `PersonService.java`, `PersonRepository.java` | Introduced `IPersonRepository` interface and updated `PersonService` to depend on the abstraction instead of the concrete class. | V-02 | Dependency Inversion Principle (DIP) | `refactor: decouple PersonService by injecting IPersonRepository interface (V-02)` | Developer 2 |

