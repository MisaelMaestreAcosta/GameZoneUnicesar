\# GRASP Pattern Audit - Developer 2



\*\*Author:\*\* <your name>

\*\*Role:\*\* Developer 2



\---



\## 1. Audit Scope



| Module / Requirement | Class / Artifact | Specific Methods \& Scope Audited |

| :--- | :--- | :--- |

| \*\*Persons\*\* (Workshop 1) | `com.gamezone.model.Person`, `Customer`, `Seller` | Attributes, constructors, getters/setters and `getRoleDescription()`. |

| | `com.gamezone.persistence.PersonRepository` | `saveCustomers`, `loadCustomers`, `saveSellers`, `loadSellers`, `writeToFile`, `readAllLines`, `initializeDefaultSellers`. |

| | `com.gamezone.service.PersonService` | Constructor, `registerCustomer`, `listCustomers`, `findCustomerById`, `registerSeller`, `listSellers`, `findSellerById`. |

| \*\*Accessories\*\* (Requirement 1) | `AccessoryRepository`, `AccessoryService` | All methods. |

| \*\*Promotions\*\* (Requirement 2) | `PromotionRepository`, `PromotionService` | All methods except `registerCategoryDiscount`, which belongs to Developer 1. |

| \*\*Returns\*\* (Requirement 3) | `ReturnRepository`, `ReturnService` | All methods, including the monthly balance. |

| \*\*Warranties\*\* (Requirement 4) | `WarrantyRepository`, `WarrantyService` | All methods. |

| \*\*Exceptions and validations\*\* (Requirement 6) | Services and repositories | Use of exceptions and validators. |



\---



\## 2. Evaluation of GRASP Patterns



\### 2.1 Information Expert



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence



\* `Person` owns `name`, `id` and `phone`. `Customer` and `Seller` implement `getRoleDescription()` using their own data (`email`, `employeeCode`, `shift`).

\* `PersonService.findCustomerById()` and `findSellerById()` search the collections held by the service itself.



\### 2.2 Creator



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence



\* `PersonRepository.loadCustomers()` and `loadSellers()` create `Customer` and `Seller` instances because they hold the file data needed to build them.



\### 2.3 Controller



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence



\* `PersonService` coordinates the person use cases (register, list, find) and delegates persistence to `PersonRepository`. The model classes do not receive system events.



\### 2.4 Low Coupling



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence



\* `Person`, `Customer` and `Seller` perform no file I/O and depend only on the `Person` base class.

\* `PersonService` receives `PersonRepository` through its constructor.



\### 2.5 High Cohesion



\* \*\*Verdict:\*\* \*\*Violation\*\*



\#### Identified Violations



| Field | Finding D2-V01 |

| :--- | :--- |

| \*\*ID\*\* | `D2-V01` |

| \*\*Pattern\*\* | High Cohesion (cross-reference: Indirection) |

| \*\*Module\*\* | Persons (Workshop 1) / Exceptions and validation (Requirement 6) |

| \*\*Evidence\*\* | `PersonService.registerCustomer()` and `registerSeller()` check `getId() == null \\|\\| getId().isBlank()` and duplicate IDs inline, throwing `IllegalArgumentException` and `IllegalStateException`. |

| \*\*Explanation\*\* | The service mixes use-case coordination with data-validation rules that belong in the validation layer introduced in Requirement 6. |

| \*\*Consequence\*\* | A validation rule change must be applied in several places, and it is unclear which one is authoritative. |

| \*\*Proposed Solution\*\* | Delegate these checks to `PersonValidator`, keeping the same messages and the same exception types so the observable behavior does not change. |

| \*\*Other Member's Code\*\* | `PersonValidator` belongs to Developer 1; coordinate if it must be modified. |



| Field | Finding D2-V02 |

| :--- | :--- |

| \*\*ID\*\* | `D2-V02` |

| \*\*Pattern\*\* | High Cohesion |

| \*\*Module\*\* | Persons (Workshop 1) |

| \*\*Evidence\*\* | `PersonRepository.initializeDefaultSellers()` creates three hard-coded `Seller` objects, and `loadSellers()` calls it when `sellers.txt` does not exist. |

| \*\*Explanation\*\* | The persistence class also owns initial business data, a second responsibility beyond reading and writing files. |

| \*\*Consequence\*\* | Changing the default sellers requires editing the persistence class. |

| \*\*Proposed Solution\*\* | Move the seed data to a dedicated class used by `PersonService` (Pure Fabrication), keeping the same three sellers and the same file content. |

| \*\*Other Member's Code\*\* | No other member's code is required. |



\### 2.6 Polymorphism



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence



\* `Person.getRoleDescription()` is abstract and overridden in `Customer` and `Seller`, so no type checks are needed to describe a person.



\### 2.7 Pure Fabrication



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence



\* `PersonRepository` encapsulates flat-file persistence so the domain classes stay free of I/O.



\### 2.8 Indirection



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence



\* `PersonRepository` mediates between `PersonService` and the data files. The validation overlap is recorded once, in `D2-V01`.



\### 2.9 Protected Variations



\* \*\*Verdict:\*\* \*\*Violation\*\*



\#### Identified Violations



| Field | Finding D2-V03 |

| :--- | :--- |

| \*\*ID\*\* | `D2-V03` |

| \*\*Pattern\*\* | Protected Variations |

| \*\*Module\*\* | Persons (Workshop 1) |

| \*\*Evidence\*\* | `PersonRepository` fixes `CUSTOMERS\_PATH`, `SELLERS\_PATH` and `DELIMITER` as `static final` constants, and `PersonService` depends on the concrete class. |

| \*\*Explanation\*\* | The storage location and format cannot vary without editing the class. |

| \*\*Consequence\*\* | Changing the file format or location, or testing with another path, requires modifying production code. |

| \*\*Proposed Solution\*\* | Inject the paths through the constructor, using the current values as defaults. |

| \*\*Other Member's Code\*\* | No other member's code is required. |



\---



\## 3. Functional Observation Outside the GRASP Findings



\* A name or email containing a comma corrupts the CSV line, because `split(",")` assumes values have no commas.

\* `loadCustomers()` and `loadSellers()` silently ignore lines with the wrong number of fields.

\* Sellers are seeded by default but customers are not.

\* ID lookup ignores case, but saved IDs keep their original case.



\## 4. Summary of Findings



| Finding ID | Pattern | Status | Assigned Module | Boundary Finding? |

| :--- | :--- | :--- | :--- | :--- |

| `D2-V01` | High Cohesion; Indirection cross-reference | \*\*Violation\*\* | Persons / validation | Possible, with Developer 1 |

| `D2-V02` | High Cohesion | \*\*Violation\*\* | Persons persistence | No |

| `D2-V03` | Protected Variations | \*\*Violation\*\* | Persons persistence | No |

