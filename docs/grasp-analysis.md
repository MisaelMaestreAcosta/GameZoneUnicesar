# GRASP Analysis - Team Guiding Questions

**Project:** GameZone Unicesar  
**Repository Branch:** `feature/grasp-audit`  
**Base Commit (develop):** `1c94b3f29fab36b46a814a4b8859881ac2e94a21`  

---

## Question 1: Verdict Criteria and Minimum Evidence

> **Prompt:** What criteria did the team use to decide whether a code fragment constitutes a GRASP pattern violation, whether the pattern is correctly applied, or whether the pattern does not apply? What minimum evidence must accompany each verdict to be accepted in the consolidation?

### 1.1 Decision Criteria

The team established uniform evaluation criteria based on the fundamental questions of Craig Larman's nine GRASP patterns, evaluated strictly against the architecture in `develop` (`ui -> service -> persistence -> model`):

1. **Violation (`violation`):**
   * Assigned when a class assumes responsibilities that contradict the core design question of the pattern.
   * *Information Expert:* The class executing a calculation or enforcing a business validation rule does not own the necessary state, or presentation layers perform domain computations.
   * *Creator:* A class instantiates objects without possessing the initializing data, aggregation relationship, or containment role.
   * *Controller:* UI components receive system events and directly orchestrate business processes across multiple services, or when a service fails to coordinate use case execution.
   * *Low Coupling:* Excessive fan-out dependencies exist between presentation and multiple services, or across layer boundaries, causing high ripple-effect risks.
   * *High Cohesion:* A class exceeds a single focused purpose (e.g., UI classes managing persistence lookups, formatting, and validation) or contains duplicated methods.
   * *Polymorphism:* Behavior is branched using conditional statements (`instanceof`, `switch`, type flags) instead of polymorphic method dispatch.
   * *Pure Fabrication:* Technical duties (such as file I/O or multi-entity coordination) are mixed into domain entities instead of being delegated to fabricated classes.
   * *Indirection:* An intermediate coordinator fails to abstract underlying collaborators, forcing clients to mediate between third-party components.
   * *Protected Variations:* Classes depend directly on volatile, concrete implementations rather than stable interfaces or polymorphic contracts.

2. **Correct Application (`correct`):**
   * Assigned when an implementation demonstrably upholds the pattern’s intent without incurring undesirable trade-offs. The report must cite the specific class and method proving how responsibility assignment enhances cohesion or decoupling.

3. **Not Applicable (`not applicable`):**
   * Assigned only when the design context governed by the pattern does not exist within the audited class hierarchy (e.g., a pure data-holding class does not coordinate system events, so Controller does not apply). Every `not applicable` verdict must contain an explicit technical justification.

### 1.2 Minimum Evidence Required for Consolidation

To ensure objectivity and audit validity, every finding admitted into the consolidated report must contain:

* **Traceable Location:** Exact class, method name, and line numbers matching the baseline commit of `develop` (`1c94b3f`).
* **Code Extract:** Verbatim snippet of the audited source code illustrating the violation or positive application.
* **Architectural Rationale:** Precise explanation linking the code snippet to the violated GRASP principle.
* **Technical Consequence:** Tangible current bug, maintainability bottleneck, or predictable change risk.
* **Actionable Solution:** Proposed refactoring strategy and design pattern to be implemented during Phase 2.
* **Ownership Mapping:** Identification of whether the solution requires modifying another member's code.

---

## Question 2: Boundary Findings and Cross-Member Responsibility

> **Prompt:** Some violations manifest on the boundary between the code of two team members, for example when one member's class improperly depends on another member's class. What findings of this type did the team identify? How were they recorded in the consolidation and to whom was their correction assigned? Present at least one concrete case or, if there were none, explain how you verified that none existed.

### 2.1 Identified Boundary Findings

The team identified three prominent boundary violations where responsibilities collided across member modules:

#### Concrete Case 1: Return Window Validation vs. `Sale.canBeReturned()`

* **Boundary:** Technical Leader (`ConsoleMenu.java`) $\leftrightarrow$ Developer 1 (`Sale.java`).
* **Finding Details:** In `ConsoleMenu.processNewReturn()` (lines 614–618), the UI calculates whether a transaction is within the 30-day return policy using `ChronoUnit.DAYS.between(sale.getDate().toLocalDate(), LocalDate.now()) > 30`. This occurred because `Sale.canBeReturned()` (line 87) remained an incomplete stub in Developer 1's code (`return true;`).
* **Pattern Violated:** Information Expert.
* **Consolidation & Assignment:** Recorded as a single boundary violation in the consolidated audit. Correction is split:
  * **Developer 1:** Implements the calendar validation logic inside `Sale.canBeReturned()` (Model layer).
  * **Technical Leader:** Refactors `ConsoleMenu.java` and `ReturnService` integration to invoke `sale.canBeReturned()` polymorphically, stripping date arithmetic from the UI.

#### Concrete Case 2: Warranty Eligibility Checking via `instanceof Console`

* **Boundary:** Technical Leader (`SaleService.java`, `ConsoleMenu.java`) $\leftrightarrow$ Developer 1 (`Product.java`, `Console.java`).
* **Finding Details:** In `SaleService.registerSale()` (lines 106–114) and `ConsoleMenu.processNewSale()` (line 261), hardcoded checks (`product instanceof Console`) are performed to offer and attach warranties.
* **Pattern Violated:** Polymorphism and Low Coupling.
* **Consolidation & Assignment:**
  * **Developer 1:** Introduces a polymorphic contract/method in the `Product` hierarchy (e.g., `isWarrantable()` or warranty capability).
  * **Technical Leader:** Eliminates `instanceof Console` branching from `SaleService` and `ConsoleMenu`, querying the polymorphic property uniformly.

#### Concrete Case 3: Monthly Balance Financial Calculations in UI

* **Boundary:** Technical Leader (`ConsoleMenu.java`) $\leftrightarrow$ Developer 2 (`ReturnService.java`).
* **Finding Details:** In `ConsoleMenu.showMonthlyBalance()` (lines 752–765), the UI queries all sales and all returns, streams over them, and calculates monthly total sales and return subtotals directly, bypassing `ReturnService.generateMonthlyBalance()`.
* **Pattern Violated:** Information Expert and High Cohesion.
* **Consolidation & Assignment:**
  * **Developer 2:** Enhances `ReturnService` to produce a complete financial summary object (DTO) containing total sales, total refunds, and net balance for the given month/year.
  * **Technical Leader:** Simplifies `ConsoleMenu.showMonthlyBalance()` to strictly display the values returned by `ReturnService`.

---

## Question 3: Related Patterns and Multi-Pattern Violations

> - **D2-V01:** inline validation in `PersonService` breaks High Cohesion and also Indirection (no validator between the service and the rules). It is recorded once under High Cohesion with an Indirection cross-reference, and High Cohesion guides the fix because it explains the root cause (two responsibilities in one class).
- **D2-V05:** filtering by `getClass().getSimpleName()` breaks Protected Variations and relates to Polymorphism (`D2-V04`). It is recorded once under Protected Variations, and the same type key from the serializers fixes both.
- **D2-V11:** Low Coupling with an Indirection cross-reference, guided by Low Coupling.
- **D2-V18:** Protected Variations with a High Cohesion cross-reference (`D2-V01`).

---

## Question 4: Non-Regression Verification Strategy

> Before the first correction, I run every menu operation on `develop` with fixed input data and save the console output and a copy of the `data/*.txt` files as the reference result. After each correction, and in each Pull Request, I repeat the same operations on the affected modules and compare console output and file content line by line. At the end, I repeat the full run over all menu operations. The evidence is the before/after comparison recorded in `docs/grasp-refactor-log.md`, plus `mvn clean compile` passing.


---

## Question 5: Correction Ordering and Git Coordination

> **Prompt:** In what order are violations addressed, and why? When addressing a violation involves code from more than one team member, how is the work coordinated to avoid conflicts within the team?
**Answer:**The team will first address violations affecting shared interfaces between modules, as changes to these contracts could block or interfere with other fixes. Before implementation, the technical lead will coordinate with the relevant developers to agree upon method signatures and their associated responsibilities. This includes areas such as sales querying and loading, return eligibility, the generation of monthly balance reports, and stock replenishment.
Subsequently, each developer will correct the violations identified within the code of their assigned domain. For issues related to polymorphism, the specific functionality for a given product or accessory must be defined before updating the sales service and the interface to utilize it.
Finally, the team will address issues concerning local cohesion and code duplication that do not rely on shared contracts. Following this, they will verify that menu operations, messaging, and data persistence continue to function as they did previously. Since the team is working on a shared branch dedicated to refactoring (based on principles such as GRASP), the technical lead will coordinate concurrent changes to ensure that only one team member edits a shared file at any given time. Before making any modifications, team members will agree on file ownership, the interfaces to be used, and the sequence of dependent commits. Each fix must be implemented as an atomic commit that includes the identifier of the violation; furthermore, it must be reviewed by another team member before any subsequent tasks that depend on it can proceed. The team will document the change and the corresponding regression tests in both the change log and the relevant pull request.
