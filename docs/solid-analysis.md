# SOLID Principles Analysis - Team Guiding Questions

**Project:** GameZone Unicesar  
**Repository Branch:** `feature/solid-audit`  
**Base Commit (develop):** `802f8c6`  

---

## Question 1: Evaluation Criteria and Required Evidence

> **Prompt:** What criteria did the team use to decide whether a code fragment constitutes a SOLID violation, whether the principle is correctly applied, or whether the principle does not apply? What minimum evidence must accompany each verdict to be accepted in the consolidation?

### 1.1 Decision Criteria per Principle

The team evaluated the codebase strictly against Robert C. Martin's five SOLID principles across all architectural layers (`ui -> service -> persistence -> model`):

1. **Single Responsibility Principle (SRP):**
   * **Violation:** Assigned when a class contains multiple distinct reasons to change or serves multiple separate actors (e.g., domain models formatting terminal ASCII receipts, or presentation classes parsing inputs, enforcing business validations, and performing financial balance sums).
   * **Correct:** The class has a single, cohesive responsibility (e.g., `SalesLineItem` strictly holds quantity and price calculation for an item).
   * **Not Applicable:** Only applied if an artifact is an empty marker or configuration structure without behavioral methods.

2. **Open/Closed Principle (OCP):**
   * **Violation:** Adding new features, entities, or rules requires editing existing service methods via conditional branching (`instanceof`, `switch`) rather than extending abstractions (e.g., `SaleService.registerSale` checking `instanceof Console` or `instanceof Accessory`).
   * **Correct:** New variants can be introduced purely by implementing an interface or subclassing an abstract base class without editing client code.
   * **Not Applicable:** Classes that represent closed, immutable data structures where no polymorphism is expected.

3. **Liskov Substitution Principle (LSP):**
   * **Violation:** A subclass alters the expected preconditions, postconditions, invariants, or throws unexpected exceptions when substituted for its base type.
   * **Correct:** All subclasses can be substituted transparently for their base abstractions without altering program correctness.
   * **Not Applicable:** Concrete leaf classes that neither inherit from nor serve as base abstractions.

4. **Interface Segregation Principle (ISP):**
   * **Violation:** A "fat" interface contains methods that certain implementing classes do not support or do not need, forcing dummy or exception-throwing implementations.
   * **Correct:** Interfaces are granular, focused, and client-specific.
   * **Not Applicable:** Modules where interactions rely on concrete classes or standard Java collections without custom interfaces.

5. **Dependency Inversion Principle (DIP):**
   * **Violation:** High-level modules (business services) depend directly on low-level concretions (flat-file repositories or concrete collaborator classes) rather than abstractions.
   * **Correct:** High-level services declare constructor dependencies on abstract interfaces, and low-level repositories implement those interfaces.
   * **Not Applicable:** Data-transfer objects or pure domain entities that do not coordinate dependencies.

### 1.2 Minimum Required Evidence
Every admitted finding in the consolidated audit must include:
* **Traceable Location:** Exact package, class, method name, and line numbers in `develop`.
* **Verbatim Code Extract:** The exact lines illustrating the violation.
* **Architectural Rationale:** Precise explanation linking the code to the specific SOLID principle.
* **Technical Risk & Consequence:** Maintainability cost, testing bottleneck, or regression hazard.
* **Actionable Refactoring Solution:** Concrete interface or extraction strategy.
* **Ownership Tag:** Explicit assignment to the class owner according to `TEAM.md`.

---

## Question 2: Boundary Findings and Cross-Member Responsibility

> **Prompt:** Some violations manifest on the boundary between the code of two team members, for example when one member's class improperly depends on another member's class. What findings of this type did the team identify? How were they recorded in the consolidation and to whom was their correction assigned?

### 2.1 Identified Boundary Findings

The team identified three prominent cross-member boundary violations:

#### Boundary Finding 1: UI High Fan-Out Coupling to Concrete Services
* **Boundary:** Technical Leader (`ConsoleMenu.java`) $\leftrightarrow$ Developer 1 (`ProductService`) & Developer 2 (`PersonService`, `ReturnService`, etc.).
* **Finding Details (L-S05):** `ConsoleMenu` directly injects and holds 7 concrete service implementations in its constructor. Any alteration in a service constructor signature cascades directly into the presentation entry point.
* **Principle Violated:** Dependency Inversion Principle (DIP) and Single Responsibility Principle (SRP).
* **Consolidation & Assignment:**
  * Recorded in the Technical Leader's audit (`L-S05`).
  * **Assignment:** The Technical Leader coordinates service interface abstractions with Developers 1 and 2, allowing the UI to depend solely on service interfaces or an application coordinator.

#### Boundary Finding 2: `SaleService` OCP Violation via `instanceof Console`
* **Boundary:** Technical Leader (`SaleService.java`) $\leftrightarrow$ Developer 1 (`Product.java`, `Console.java`).
* **Finding Details (L-S03):** Inside `SaleService.registerSale()`, hardcoded checks (`product instanceof com.gamezone.model.Console`) determine warranty assignment.
* **Principle Violated:** Open/Closed Principle (OCP).
* **Consolidation & Assignment:**
  * **Developer 1:** Introduces a polymorphic method on `Product` (e.g., `isWarrantyEligible()`) or provides an extensible capability.
  * **Technical Leader:** Refactors `SaleService` to query the polymorphic contract, eliminating type checking.

#### Boundary Finding 3: `PersonService` Concretion in `SaleService`
* **Boundary:** Technical Leader (`SaleService.java`) $\leftrightarrow$ Developer 2 (`PersonService.java`, `PersonRepository.java`).
* **Finding Details (L-S04):** `SaleService` injects concrete `PersonService` and concrete `SaleRepository`.
* **Principle Violated:** Dependency Inversion Principle (DIP).
* **Consolidation & Assignment:**
  * **Developer 2:** Extracted `IPersonRepository` (Finding `V-02`).
  * **Technical Leader:** Introduces `ISaleRepository` for `SaleService`, decoupling sales persistence from file I/O details.

---

## Question 3: Relationship Between SOLID and GRASP Audit Findings

> **Prompt:** How do the SOLID audit findings relate to the GRASP audit conducted in Requirement 7? In what ways do the two perspectives align, where did GRASP leave gaps that SOLID exposed, and how do their solutions complement each other?

### 3.1 Direct Alignment and Reinforcement
* **High Cohesion $\leftrightarrow$ Single Responsibility Principle (SRP):**
  * The GRASP audit identified that `ConsoleMenu` and `Sale.generateReceipt()` suffered from low cohesion by mixing presentation formatting with domain or orchestration responsibilities (`L-V07`). SOLID formalizes this as an SRP violation (`L-S01`, `L-S02`): classes have more than one actor and reason to change.
* **Polymorphism & Protected Variations $\leftrightarrow$ Open/Closed Principle (OCP):**
  * The GRASP audit flagged type-checking conditionals (`instanceof Console`, `instanceof Accessory`) under Polymorphism (`L-V09`). SOLID evaluates the architectural consequence under OCP (`L-S03`): the sales checkout workflow is not closed against catalog extensions.
* **Low Coupling $\leftrightarrow$ Dependency Inversion Principle (DIP):**
  * GRASP Low Coupling discouraged direct reliance between layers. SOLID DIP goes further by requiring that dependency arrows point toward abstractions rather than concrete classes.

### 3.2 Gaps Exposed by SOLID after GRASP Refactoring
While Requirement 7 (GRASP) successfully resolved responsibility assignments (such as encapsulating `canBeReturned()` in `Sale` and factory instantiation in `createSale()`), it did not introduce interfaces for repositories or services.
* **Concrete Class Coupling:** `PersonService` had high cohesion under GRASP, but still depended directly on `PersonRepository` (violating DIP).
* **Repository Rigidity:** Repositories had parameterized file paths (GRASP Protected Variations `D2-V03`, `D2-V06`), but lacked interface contracts (`IPersonRepository`, `ISaleRepository`), making unit testing with mocks impossible without refactoring under SOLID.

---

## Question 4: Preservation of Existing Behavior & Non-Regression Strategy

> **Prompt:** What strategy guarantees that refactoring to adhere to SOLID principles will preserve the exact functional behavior of the system? What concrete evidence is collected?

### 4.1 Non-Regression Protocol
To guarantee 100% preservation of behavior during Phase 2 refactoring:
1. **Compilation Baseline:**
   * Automated verification using `mvn clean compile` ensuring 0 compilation errors across all 37 source files.
2. **Deterministic Console Flow Execution:**
   * Executing the complete suite of use cases using predefined benchmark data:
     * Product & Accessory catalog listings.
     * Customer & Seller queries and default seller initialization.
     * Sale processing with promotional discounts and warranty assignment.
     * Sales history queries and receipt generation.
     * Returns processing with 30-day window validation and stock restoration.
     * Monthly financial balance calculation.
3. **Data File Verification:**
   * Validating that output files in `data/*.txt` (`customers.txt`, `sellers.txt`, `products.txt`, `sales.txt`, `returns.txt`, `warranties.txt`) preserve exact delimiters, line formatting, and record ordering.

### 4.2 Evidence Artifact
* All before/after comparisons and terminal test execution logs are systematically recorded in `docs/solid-refactor-log.md`.

---

## Question 5: Correction Order and Git Coordination

> **Prompt:** In what order are violations addressed, and why? When addressing a violation involves code from more than one team member, how is the work coordinated to avoid conflicts within the team?

### 5.1 Sequential Execution Strategy
Violations are corrected in strict architectural order from innermost abstractions to outer presentation:
1. **Persistence Abstractions (DIP):** Introduce repository interfaces (`IPersonRepository`, `ISaleRepository`, `IProductRepository`) before touching services.
2. **Repository Implementation Alignment:** Update concrete repositories to implement their respective interfaces while retaining constructor path injection from GRASP.
3. **Service Layer Refactoring (DIP & OCP):** Update services (`PersonService`, `SaleService`) to accept repository and collaborator interfaces through constructor injection.
4. **Domain Model Extensions (OCP & SRP):** Extract receipt formatters and introduce polymorphic query methods on the domain hierarchy.
5. **Presentation Layer Integration (SRP):** Decompose `ConsoleMenu` submenus and wire services via abstractions in `main.java`.

### 5.2 Git Branching and Conflict Prevention
* **Dedicated Refactoring Branch:** All refactoring work is developed on `feature/solid-refactor`.
* **Atomic Commits with Violation IDs:** Each change is committed with a single focused responsibility using Conventional Commits and referencing the finding ID (e.g., `refactor: introduce ISaleRepository interface (L-S04)`).
* **Cross-Member Coordination:** Shared files (`main.java`, `ConsoleMenu.java`) are modified sequentially. Pull Requests are reviewed and integrated by the Technical Leader to prevent concurrent merge conflicts.
