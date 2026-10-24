SOLID Principles Analysis \& Guiding Questions



1\. Evaluation Criteria and Required Evidence



Violation Criteria: A code snippet is marked as a violation if it demonstrates clear structural issues such as tight coupling, mixed responsibilities (e.g., combining domain business logic with low-level file I/O operations), or breaking established inheritance contracts.



Compliance Criteria: A principle is fulfilled when responsibilities are cleanly isolated, code extends functionality via abstractions without modifying existing code, and subclasses strictly honor base class expectations.



Not Applicable Criteria: A principle is marked "not applicable" when the analyzed class lacks the architectural mechanisms targeted by that principle (e.g., no interfaces or client abstraction layers present).



Minimum Evidence Required: Every audit finding must explicitly cite the class name, method signature, line numbers, and exact code snippet from the current state of the develop branch.



2\. Boundary Findings (Cross-Member Dependencies)



Boundary Identification: Boundary issues occur when a class owned by one team member depends inappropriately on concrete implementations or low-level details of a class owned by another member (e.g., ConsoleMenu directly handling repository instantiation or SaleService depending on concrete PersonRepository methods).



Consolidation \& Assignment: Boundary findings are reported to the Technical Leader and logged once in the consolidated audit. Refactoring tasks are assigned based on ownership rules established in TEAM.md: the class owner modifies their respective layer to expose or accept cleaner abstractions.



3\. Relationship Between SOLID and GRASP Audit Findings



Overlap \& Alignment: The GRASP refactoring performed in Requirement 7 laid a solid foundation by improving responsibility assignment (e.g., High Cohesion, Information Expert).



SOLID Violations in GRASP-Refactored Code: Some classes previously refactored under GRASP still exhibited SOLID violations, such as PersonService depending directly on concrete PersonRepository (violating DIP) despite having good GRASP cohesion.



GRASP Corrections Resolving SOLID Issues: Delegating total calculations directly to Sale during the GRASP refactor naturally resolved an OCP violation by eliminating conditional type checks (if/else) in service methods.



4\. Preservation of Existing Behavior \& Non-Regression Strategy



Verification Strategy: To guarantee zero regression during Phase 2 refactoring, the team defines an end-to-end test protocol covering all console menu options, input validations, and data file persistence structures (customers.txt, sellers.txt).



Evidence: Terminal output execution logs and matching dataset file states comparing pre-refactor (develop baseline) against post-refactor execution across all user operations.



5\. Correction Order and Conflict Resolution



Execution Strategy: Violations are addressed in order of architectural dependency: foundational model and persistence layer violations are corrected first, followed by service layer abstractions, and finally UI/Console Menu integrations.



Branch Coordination: All members work on dedicated feature branches (feature/solid-refactor), issuing atomic commits with explicit violation IDs (refactor: ... (V-XX)) and coordinating Pull Requests sequentially to avoid merge conflicts on shared files.

