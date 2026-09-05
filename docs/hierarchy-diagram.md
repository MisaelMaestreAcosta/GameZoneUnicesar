```mermaid
classDiagram
    class Person {
        <<abstract>>
    }
    
    class Product {
        <<abstract>>
    }
    
    class Customer
    class Seller
    class VideoGame
    class Console
    
    Customer --|> Person
    Seller --|> Person
    VideoGame --|> Product
    Console --|> Product
```