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

* (2.1 Information Expert) `AccessoryService.findAccessoriesCompatibleWith()` filters using `Accessory.getCompatibleConsoleIds()`, data owned by the accessory itself.



\* `Person` owns `name`, `id` and `phone`. `Customer` and `Seller` implement `getRoleDescription()` using their own data (`email`, `employeeCode`, `shift`).

\* `PersonService.findCustomerById()` and `findSellerById()` search the collections held by the service itself.



\### 2.2 Creator



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence

* (2.2 Creator) `AccessoryRepository.fromTextLine()` creates `Controller`, `Cable` and `Memory` because it holds the file data needed to build them. `AccessoryService.registerController/Cable/Memory()` create the accessories and manage the collection.

\* `PersonRepository.loadCustomers()` and `loadSellers()` create `Customer` and `Seller` instances because they hold the file data needed to build them.



\### 2.3 Controller



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence

* (2.3 Controller) `AccessoryService` coordinates register, list, find and `updateStock` and delegates persistence to `AccessoryRepository`.

\* `PersonService` coordinates the person use cases (register, list, find) and delegates persistence to `PersonRepository`. The model classes do not receive system events.



\### 2.4 Low Coupling



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence

* (2.4 Low Coupling) `AccessoryService` receives `AccessoryRepository` through its constructor and the accessory model classes perform no file I/O.																																																		

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



\* \*\*Verdict:\*\* \*\*Violation\*\*



### 2.6 Polymorphism

* **Verdict:** **Violation**

#### Well-Applied Evidence

* `Person.getRoleDescription()` is abstract and overridden in `Customer` and `Seller`, so no type checks are needed to describe a person.

#### Identified Violations

| Field | Finding D2-V04 |
| :--- | :--- |
| **ID** | `D2-V04` |
| **Pattern** | Polymorphism |
| **Module** | Accessories (Requirement 1) |
| **Evidence** | `AccessoryRepository.toTextLine()` uses an `instanceof` chain over `Controller`, `Cable` and `Memory`. `AccessoryRepository.fromTextLine()` uses a `switch` over the strings `"CONTROLLER"`, `"CABLE"` and `"MEMORY"`. |
| **Explanation** | Serialization and deserialization depend on the concrete accessory type through conditionals instead of letting each type provide its own variation. |
| **Consequence** | Adding a new accessory type requires editing two existing methods in the repository, and a missed branch fails only at runtime (`IllegalArgumentException`). |
| **Proposed Solution** | Introduce one serializer per accessory type behind a common interface (Polymorphism) and look it up by type key in the repository. File format, field order and file content stay identical. |
| **Other Member's Code** | No code of `Accessory`, `Controller`, `Cable` or `Memory` (Developer 1) needs changes, because serializers are separate classes. |


\### 2.7 Pure Fabrication



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence

* (2.7 Pure Fabrication) `AccessoryRepository` encapsulates flat-file persistence for accessories.

\* `PersonRepository` encapsulates flat-file persistence so the domain classes stay free of I/O.



\### 2.8 Indirection



\* \*\*Verdict:\*\* \*\*Correct\*\*



\#### Well-Applied Evidence

* (2.8 Indirection) `AccessoryRepository` mediates between `AccessoryService` and `data/accessories.txt`.

\* `PersonRepository` mediates between `PersonService` and the data files. The validation overlap is recorded once, in `D2-V01`.



\### 2.9 Protected Variations



\* \*\*Verdict:\*\* \*\*Violation\*\*



\#### Identified Violations

| Field | Finding D2-V05 |

| :--- | :--- |

| **ID** | `D2-V05` |

| **Pattern** | Protected Variations (cross-reference: Polymorphism) |

| **Module** | Accessories (Requirement 1) |

| **Evidence** | `AccessoryService.listAccessoriesByType()` compares `a.getClass().getSimpleName()` with the received string. The service also has one nearly identical `registerController()`, `registerCable()` and `registerMemory()` method per type. |

| **Explanation** | Filtering depends on the Java class name, so renaming or refactoring a class silently breaks the behavior. Each new accessory type forces a new register method in the service. |

| **Consequence** | A class rename returns empty lists without any compile error, and extending the catalog requires modifying the service. |

| **Proposed Solution** | Filter through a stable type key exposed by the accessory type (or the serializer key from D2-V04) instead of the class name. Keep the accepted input values and results identical. |

| **Other Member's Code** | Exposing a type key on `Accessory` would touch Developer 1's code; coordinate through the Technical Leader. |

| Field | Finding D2-V06 |

| :--- | :--- |

| **ID** | `D2-V06` |

| **Pattern** | Protected Variations |

| **Module** | Accessories (Requirement 1) |

| **Evidence** | `AccessoryRepository` fixes `FILE_PATH`, `FIELD_DELIMITER` and `LIST_DELIMITER` as `static final` constants, and `AccessoryService` depends on the concrete class. |

| **Explanation** | The storage location and format cannot vary without editing the class, the same situation as `D2-V03`. |

| **Consequence** | Testing with another path or changing the format requires modifying production code. |

| **Proposed Solution** | Inject the path through the constructor, with the current value as default. |

| **Other Member's Code** | No other member's code is required. |

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
* `AccessoryRepository.loadAll()` assumes every line has the expected number of fields; a malformed line throws `ArrayIndexOutOfBoundsException` or `NumberFormatException`.
* A title containing a comma corrupts the line, because `split(",")` is used.
* `AccessoryService.register*()` perform no validation (duplicate IDs, negative price or quantity); this is recorded in the exceptions and validation block.
* `AccessoryService.findById()` returns `null` while `updateStock()` throws `IllegalArgumentException`, which is inconsistent with `PersonService`, which returns `Optional`.
* `Cable.getLenght()` is misspelled (Developer 1's code); reported to the Technical Leader.


r
\## 4. Summary of Findings



| Finding ID | Pattern | Status | Assigned Module | Boundary Finding? |
| :--- | :--- | :--- | :--- | :--- |
| `D2-V01` | High Cohesion; Indirection cross-reference | \*\*Violation\*\* | Persons / validation | Possible, with Developer 1 |
| `D2-V02` | High Cohesion | \*\*Violation\*\* | Persons persistence | No |
| `D2-V03` | Protected Variations | \*\*Violation\*\* | Persons persistence | No |
| `D2-V04` | Polymorphism | **Violation** | Accessories persistence | No |
| `D2-V05` | Protected Variations; Polymorphism cross-reference | **Violation** | Accessories service | Possible, with Developer 1 |
| `D2-V06` | Protected Variations | **Violation** | Accessories persistence | No |

