## 1.  
**Answer:**
## 

## 2.  
**Answer:**
##  t

## 3.  
**Answer:**
## 

## 4. 
**Answer:**
##

## 5. The query for "guarantees nearing expiration" requires iterating over all guarantees and filtering for those with an end date within the next 30 days. In which class should this method be placed, and what dependencies does it require? Why? Is this placement consistent with a layered architecture? 
**Answer**
## This method resides in the `WarrantyService` class and relies on `WarrantyRepository` to retrieve the list of warranties. It aligns with the layered architecture because the querying, filtering, and date-range calculations constitute application or business logic, rather than presentation or atomic model logic
