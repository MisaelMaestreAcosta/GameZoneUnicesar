# Exception Handling Analysis

## 1. GameZoneException: Exception (checked) vs RuntimeException (unchecked)

*Question: Should the root class GameZoneException be declared as Exception or RuntimeException? Which is more suitable for this project and why? What consequence does this have on the method signatures of services and repositories?*

(The `GameZoneException` should be declared as a `RuntimeException` (unchecked exception). In modern Java application design, unchecked exceptions are preferred for business logic validations and system errors because they prevent the need to propagate `throws` clauses across the entire call stack. The main consequence of this choice is that method signatures in the Service and Repository layers remain clean, as they are not forced to explicitly declare `throws GameZoneException` (or its subclasses), reducing boilerplate code and tight coupling between layers.)

## 2. Custom Hierarchy vs Generic IllegalArgumentException

*Question: Why is it preferable to have a custom exception hierarchy with a common root class instead of throwing a generic IllegalArgumentException in all cases? What design principles are applied?*

(Using a custom exception hierarchy with a common root class is preferable because it enables granular and context-specific error handling. A generic `IllegalArgumentException` only indicates that a parameter is wrong, but fails to communicate the exact nature of the error (e.g., a missing file, a business rule violation, or a missing database record). A custom hierarchy allows the UI layer to catch specific error types and display tailored, user-friendly messages. This approach applies the **Single Responsibility Principle (SRP)**, as each exception class represents one exact type of failure, and the **Open/Closed Principle (OCP)**, since the system can be extended with new exception types in the future without breaking existing code.)

## 3. Validation Layer Architecture

*Question: The validators form a new layer (com.gamezone.validation). At what point in the flow are they invoked, and why is this location consistent with the layered architecture? Can validators access the persistence package? Justify.*

(Answer here)

## 4. Preservation of External Behavior

*Question: The refactor must preserve the external behavior of the system. What strategy does the team follow to ensure the 10 menu operations continue to work identically? What evidence will be shown at the end to prove nothing was broken?*

(Answer here)

## 5. ConsoleMenu Exception Catching Strategy

*Question: ConsoleMenu used to catch a single generic exception. Now it must catch four distinct types. Should this be done with a single try block with multiple catches, cascading catches, or a general catch(GameZoneException)? Justify the choice considering code clarity and differentiated user messages.*

(Answer here)
