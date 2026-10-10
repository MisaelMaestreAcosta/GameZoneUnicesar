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

> *(To be completed by Developer 1 / Developer 2)*

---

## Question 4: Non-Regression Verification Strategy

> *(To be completed by Developer 1 / Developer 2)*

---

## Question 5: Correction Ordering and Git Coordination

> *(To be completed by Developer 1 / Developer 2)*
