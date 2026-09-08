# AI Usage Log - Technical Leader

**Student:** Misael Andres Maestre Acosta  
**Role:** Technical Leader  
**Project:** GameZoneUnicesar

---

### Entry 1: 
* **Date:** 2026-09-02 / 2026-09-03
* **Tool:** Gemini Pro
* **Query / Context:**
    * *"¿Cómo organizo las ramas según Git Flow y protejo main/develop?"*
    * *"Tengo problemas de sincronización con Git por tener el proyecto en OneDrive."*
* **AI Output Summary:** Suggested configuring Branch Protection Rules on GitHub to require Pull Requests. Recommended moving the local NetBeans workspace out of OneDrive to prevent file locking and `.git` corruption.
* **Critical Evaluation & Adaptations:** I manually relocated the repository to local storage and verified the workspace path. Configured GitHub protection rules to ensure no developer (including myself) can bypass the review process, aligning perfectly with the team collaboration constraints of the workshop.

---

### Entry 2: 
* **Date:** 2026-09-03
* **Tool:** Gemini Pro
* **Query / Context:**
    * *"Podemos seguir con subir los otros diagramas (Jerarquías y Capas)?"*
    * *"Cual era el comando para crear el archivo md en la raiz de PowerShell?"*
* **AI Output Summary:** Provided the PowerShell `New-Item` commands to create markdown files. Generated the Mermaid syntax for `hierarchy-diagram.md` (abstract classes `Person`, `Product`) and `layers-diagram.md` (`ui`, `service`, `persistence`, `model`).
* **Critical Evaluation & Adaptations:** I reviewed the Mermaid diagrams to ensure they strictly followed the 4-layer architecture requested by the professor. I verified the unidirectional flow (`UI -> Service -> Persistence -> Model`) and executed the atomic commits step-by-step to maintain a clean history.

---

### Entry 3: 
* **Date:** 2026-09-03
* **Tool:** Gemini Pro
* **Query / Context:**
    * *"Hice push y fue rechazado (error: failed to push some refs)."*
    * *"Antes de seguir, acepté un pull request del desarrollador 2. ¿Cómo actualizo?"*
    * *"Me tocó salirme de PowerShell en medio de un merge (editor Vim), ¿cómo verifico si se ejecutó el comando?"*
* **AI Output Summary:** Explained the cause of non-fast-forward rejections and provided the `git pull origin feature/analysis-design --rebase` command. Later, guided the process of pulling from `develop` after accepting a PR, and explained how to save/exit the Vim editor (`:wq`) or recover a pending merge.
* **Critical Evaluation & Adaptations:** Instead of forcing the push (`--force`) which would erase remote history, I applied the rebase and merge techniques. I successfully integrated the `Person` module created by Developer 2 into my local branch without losing the documentation progress.

---

### Entry 4: 
* **Date:** 2026-09-03
* **Tool:** Gemini Pro
* **Query / Context:**
    * *"Abramos la rama de configuracion maven. ¿Esto lo pide el taller?"*
    * *"Yo lo creé con maven en NetBeans, no es necesario hacerlo exactamente como lo hizo el profesor con Claude."*
* **AI Output Summary:** Clarified that the workshop requires a specific 4-layer structure and file persistence, but does not strictly mandate replicating the professor's exact Maven commits if the project is already properly initialized in NetBeans.
* **Critical Evaluation & Adaptations:** I decided to adapt the AI's suggestion. Since my NetBeans project was already initialized as a Maven project, I focused on ensuring the architectural folders (`persistence/data`) and the `Main.java` entry point were correct, prioritizing the workshop's actual technical requirements.

---

### Entry 5: 
* **Date:** 2026-09-07
* **Tool:** Gemini Pro
* **Query / Context:**
    * *"Como cierro el pull reques me equivoque de base"*
* **AI Output Summary:** Explained that it is not necessary to close the Pull Request and create a new one. Provided step-by-step instructions on how to use the "Edit" button in the GitHub UI to change the base branch from `main` to `develop`.
* **Critical Evaluation & Adaptations:** I followed the instructions to change the base branch directly in GitHub. This allowed me to fix the routing mistake instantly without losing the commit history or complicating the branch tree, ensuring strict adherence to the Git Flow model.

---

### Entry 6: 
* **Date:** 2026-09-07
* **Tool:** Gemini Pro
* **Query / Context:**
    * *"Muéstrame la implementación de SalesLineItem.java y Sale.java cumpliendo con el principio de Experto en Información."*
* **AI Output Summary:** Generated the model classes demonstrating the Information Expert pattern. `SalesLineItem` was assigned the responsibility of calculating its own subtotal, and `Sale` was given the responsibility of aggregating the total. 
* **Critical Evaluation & Adaptations:** I analyzed the implementation to ensure all attributes remained private and that no file access logic was present in the model layer. The distribution of mathematical responsibilities perfectly answered the analysis question regarding who should calculate the total of a sale.

---

### Entry 7: 
* **Date:** 2026-09-07
* **Tool:** Gemini Pro
* **Query / Context:**
    * *"Este es el codigo de product... en la clase SalesLineItem en toString product deberia llamar a getTitle verdad?"* (Regarding ProductService logic).
* **AI Output Summary:** Confirmed the correct usage of getters. Analyzed the provided `ProductService` and suggested reusing the existing `updateStock(String productId, int quantity)` method by passing a negative quantity during checkout.
* **Critical Evaluation & Adaptations:** I implemented this approach because it strictly respects the layered architecture and encapsulation. By making `SaleService` communicate with `ProductService` for inventory deductions, I avoided coupling the sales module directly with the product persistence layer.

---

### Entry 8: 
* **Date:** 2026-09-07 / 2026-09-08
* **Tool:** Gemini Pro
* **Query / Context:**
    * *(Se consultó el manejo de `Optional<Customer>` y si había otra manera de integrar archivos sin Pull Request).*
* **AI Output Summary:** Adapted the `ConsoleMenu.java` logic to safely handle `Optional` returns using `.isEmpty()` to prevent `NullPointerException`. Later, strongly warned against bypassing GitHub Pull Requests for merging files.
* **Critical Evaluation & Adaptations:** I integrated the `Optional` handling in the UI, making the console menu robust against invalid user inputs. Crucially, I discarded the idea of merging the AI logs locally via terminal and enforced the Pull Request rule, ensuring we met the minimum required PR count for the final grade.tions:** I decided to adapt the AI's suggestion. Since my NetBeans project was already initialized as a Maven project, I focused on ensuring the architectural folders (`persistence/data`) and the `Main.java` entry point were correct, prioritizing the workshop's actual technical requirements over blindly copying the reference history.