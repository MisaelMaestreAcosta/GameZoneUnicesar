# Bitácora de Uso de Inteligencia Artificial

**Rol:** Líder Técnico  
**Archivo:** `docs/ai-usage/leader-ai-log.md`  
**Herramienta IA:** Gemini Pro  
**Fase del Proyecto:** Integración final y documentación  

---

## 1. Gestión de Ramas y Prevención de Archivos Basura en Pull Requests

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de integración final - Rama: `fix/complete-modules-integration` |
| **Objetivo** | Crear un nuevo pull request en una rama limpia para resolver conflictos de integración, asegurando que no se subieran archivos compilados o temporales al repositorio. |
| **Consulta** | "Que hago para hacer un nuevo pull request con una nueva rama para resolver todos los errores" y "COMO HAGO PARA HACER EL PULL REQUEST Y NO APAREZCA NADA RARO". |
| **Respuesta** | Se recomendó un flujo de comandos estructurado: aislar el trabajo en una nueva rama, descartar archivos como `sources.txt` mediante `git restore`, y validar a través de `git status` que el `.gitignore` estuviera bloqueando la carpeta `target/` y los `.class`. |
| **Decisión** | Se aceptó la metodología de Git Flow creando la rama. Se decidió excluir manualmente cualquier archivo ajeno al código fuente `.java` o datos `.csv` antes del commit. |
| **Commit relacionado** | `feat(integration): finalize all modules and resolve service dependencies` |

<br>

## 2. Diagnóstico de Clases Faltantes vs. Binarios Huérfanos

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de integración final - Rama: `fix/complete-modules-integration` |
| **Objetivo** | Entender por qué `PromotionService` no aparecía en `git status` para ser commiteada, a pesar de que el IDE mostraba el archivo compilado en el árbol del proyecto. |
| **Consulta** | "No veo que estan clases se integran al commit [Imagen de NetBeans]" y "Antigravity me dice que es normal, esas clases se crearan automaticamente al hacer clean-build en netbeans". |
| **Respuesta** | Se diagnosticó usando `git ls-files` y `Test-Path`. La IA desmintió a la herramienta Antigravity; demostró que `PromotionService.java` había desaparecido del disco físico y que un `Clean and Build` destruiría el único binario restante `.class`, causando un `BUILD FAILURE`. |
| **Decisión** | Se descartó el consejo de Antigravity por ser incorrecto. Se aceptó la directriz de Gemini y se decidió recrear manualmente el archivo `PromotionService.java` con su lógica antes de compilar. |
| **Commit relacionado** | `feat(promotion): implement PromotionService and complete system integration` |

<br>

## 3. Creación de Archivos Markdown y Cmdlets de PowerShell

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de documentación - Rama: `docs/update-diagrams-and-analysis` |
| **Objetivo** | Nombrar correctamente la rama para modificar los diagramas y resolver un error al intentar crear un archivo Markdown desde la consola PowerShell. |
| **Consulta** | "como puedo nombrar la rama?" seguido de una captura de pantalla del error: `New-Item docs/full-class-diagram.md` bloqueando la terminal. |
| **Respuesta** | Se ofrecieron alternativas de nombres de rama basados en convenciones. Sobre el error, se identificó que faltaba especificar el tipo de elemento, recomendando usar `-ItemType File`. |
| **Decisión** | Se aceptó el nombre de rama sugerido para mantener la consistencia con Git Flow. Se modificó el comando en la terminal aplicando la corrección paramétrica para crear el documento. |
| **Commit relacionado** | `docs: add full-class-diagram` |

<br>

## 4. Resolución de Errores de Seguimiento Remoto (Upstream) en Git Push

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de documentación - Rama: `docs/update-diagrams-and-analysis` |
| **Objetivo** | Resolver errores de sincronización con GitHub al intentar subir la nueva rama de documentación por primera vez. |
| **Consulta** | Capturas de pantalla mostrando los errores `fatal: The current branch has no upstream branch` y `src refspec docs/full-class-diagram.md does not match any` al hacer push. |
| **Respuesta** | Se explicó que las ramas nuevas requieren establecer un enlace de seguimiento en su primera publicación usando `--set-upstream`. Se corrigió el error conceptual explicando que `git push` se hace hacia la rama completa, no hacia un archivo individual. |
| **Decisión** | Se aceptó la corrección de sintaxis y se ejecutó el comando `git push -u origin docs/update-diagrams-and-analysis`, descartando el intento de subir el archivo por separado. |
| **Commit relacionado** | Empuje (push) exitoso del commit `docs: add full-class-diagram` hacia el repositorio remoto. |

## 5. Resolución de Conflictos Multi-Módulo

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de integración final - Rama: `fix/complete-modules-integration` |
| **Objetivo** | Resolver conflictos de fusión complejos en archivos compartidos (`ConsoleMenu.java` y `SaleService.java`) causados por la integración simultánea de todos los módulos. |
| **Consulta** | "¿Cómo resuelvo los conflictos de merge si ConsoleMenu.java tiene código nuevo de la rama promotion y de accessory al mismo tiempo en las mismas líneas?" |
| **Respuesta** | Se explicó cómo identificar las marcas de conflicto de Git (`<<<<<<< HEAD`, `=======`, `>>>>>>>`) y se sugirió usar la herramienta visual de resolución de NetBeans, o hacerlo manualmente, para conservar los *imports* y métodos de ambos submódulos sin sobreescribir el trabajo de los demás. |
| **Decisión** | Se decidió resolver los conflictos manualmente en el editor, unificando las opciones de menú para Accesorios, Promociones, Devoluciones y Garantías en un solo bloque estructurado antes de marcar el conflicto como resuelto. |
| **Commit relacionado** | `fix(core): resolve merge conflicts in ConsoleMenu and SaleService` |

<br>

## 6. Sintaxis de Diagramas Mermaid y Arquitectura

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de documentación - Rama: `docs/update-diagrams-and-analysis` |
| **Objetivo** | Representar correctamente las relaciones del modelo de dominio unificado (agregación, composición y herencia) en el nuevo archivo de diagramas del proyecto. |
| **Consulta** | "Necesito actualizar el diagrama de clases completo en Mermaid, ¿cómo represento que la clase Sale tiene composición con las promociones pero agregación con los accesorios?" |
| **Respuesta** | Se proporcionó la sintaxis específica de Mermaid para relaciones estructurales: `Sale *-- Promotion` para composición (dependencia estricta) y `Sale o-- Accessory` para agregación (independencia de ciclo de vida), incluyendo un esquema base de prueba. |
| **Decisión** | Se aceptó la estructura sintáctica sugerida, adaptándola con los nombres de atributos y métodos exactos del código Java actual para completar el archivo `full-class-diagram.md`. |
| **Commit relacionado** | `docs(diagram): update entity relationships in full class diagram` |

<br>

## 7. Análisis de Dominio y Preguntas Técnicas en Inglés

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de documentación - Rama: `docs/update-diagrams-and-analysis` |
| **Objetivo** | Redactar de forma profesional en inglés técnico las justificaciones de las decisiones de diseño arquitectónico exigidas en el cuestionario de análisis del proyecto. |
| **Consulta** | "Ayúdame a redactar en inglés la respuesta a por qué usamos el patrón Repository para leer los CSV, justificando la arquitectura." |
| **Respuesta** | Se estructuró un párrafo explicando el principio de separación de responsabilidades (*Separation of Concerns*), destacando que el patrón Repository aísla la persistencia de datos (archivos CSV) de la lógica de negocio central (servicios y modelos). |
| **Decisión** | Se modificó ligeramente la redacción sugerida para adaptarla al vocabulario académico visto en clase, conservando los conceptos técnicos clave (desacoplamiento y mantenibilidad). |
| **Commit relacionado** | `docs(analysis): add architectural design answers in english` |

<br>

## 8. Sincronización y Limpieza del Árbol de Trabajo

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Mantenimiento post-integración - Rama: `develop` |
| **Objetivo** | Limpiar el entorno local de Git después de haber fusionado todos los Pull Requests y eliminado las ramas remotas en GitHub. |
| **Consulta** | "Ya hice merge de los pull request en GitHub, pero las ramas viejas como feature/promotion-module siguen apareciendo en mi terminal local, ¿cómo las borro todas de una vez?" |
| **Respuesta** | Se indicaron dos comandos fundamentales de limpieza: `git fetch --prune` para sincronizar las referencias eliminadas en el servidor, y `git branch -d <nombre-rama>` para eliminar de forma segura las ramas locales que ya fueron fusionadas. |
| **Decisión** | Se ejecutaron ambos comandos para depurar el espacio de trabajo local, dejando únicamente la rama `develop` limpia y actualizada con el trabajo final de todo el equipo. |
| **Commit relacionado** | *No aplica (operaciones de limpieza local).* |


## 9. Configuración de Políticas de Protección de Ramas (Branch Protection)

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Configuración del repositorio - Rama: *N/A (GitHub Settings)* |
| **Objetivo** | Aplicar restricciones en el repositorio remoto para obligar a todo el equipo a utilizar el flujo de Pull Requests, impidiendo la subida de código sin revisión a la rama principal. |
| **Consulta** | "¿Cómo configuro mi repositorio en GitHub para que nadie pueda hacer push directo a develop o main, y que todo deba pasar por un Pull Request obligatoriamente?" |
| **Respuesta** | Se explicó el proceso para acceder a `Settings > Branches` en GitHub y crear una regla de protección de rama (*Branch protection rule*), activando la opción "Require a pull request before merging". |
| **Decisión** | Se implementó la regla en las ramas `develop` y `main`, bloqueando los commits directos y asegurando el cumplimiento estricto de la metodología Git Flow por parte de todos los desarrolladores. |
| **Commit relacionado** | *No aplica (Configuración aplicada directamente en la plataforma GitHub).* |

<br>

## 10. Refactorización y Polimorfismo en Lógica de Descuentos

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de integración final - Rama: `fix/complete-modules-integration` |
| **Objetivo** | Optimizar el cálculo de descuentos en el módulo de promociones utilizando conceptos de jerarquía de herencia orientada a objetos para evitar el uso excesivo de condicionales `if-else`. |
| **Consulta** | "Tengo varias clases de descuento (Percentage, Category, Bulk). ¿Cómo puedo calcular el mejor descuento en PromotionService sin hacer tantos if-else para saber qué tipo de descuento es?" |
| **Respuesta** | Se sugirió aprovechar el polimorfismo definiendo un método `calculateDiscount(Sale sale)` en la clase padre `Promotion` y sobreescribiéndolo en cada subclase. Así, el servicio solo itera la lista llamando al mismo método genérico. |
| **Decisión** | Se adaptó la arquitectura de las clases de promoción. Se eliminaron las validaciones de tipo explícitas en el servicio, delegando el cálculo matemático a cada subclase correspondiente. |
| **Commit relacionado** | `refactor(promotion): apply polymorphism for discount calculation logic` |

<br>

## 11. Estandarización de Mensajes con Conventional Commits

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Estandarización del flujo de trabajo - Rama: Todas las ramas |
| **Objetivo** | Establecer un estándar de escritura claro para el historial de versiones del proyecto, facilitando la lectura y auditoría de los cambios por parte del docente y el equipo. |
| **Consulta** | "¿Cuáles son las reglas de Conventional Commits para un proyecto de software en Java y cómo debo estructurar los mensajes?" |
| **Respuesta** | Se proporcionó una guía sobre los prefijos estándar (`feat`, `fix`, `docs`, `refactor`) y la regla de mantener el mensaje en minúsculas, en modo imperativo y en idioma inglés. |
| **Decisión** | Se adoptó el estándar como política obligatoria para el proyecto. Se utilizó activamente durante las fases de desarrollo e integración para clasificar modificaciones de código, correcciones y documentación. |
| **Commit relacionado** | *Múltiples commits estandarizados (ej. `feat(core): ...`, `docs(analysis): ...`).* |

<br>

## 12. Depuración de Buffer en la Interfaz de Consola (Scanner)

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 17 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Pruebas de integración - Rama: `fix/complete-modules-integration` |
| **Objetivo** | Solucionar un error de ejecución en `ConsoleMenu.java` donde el menú interactivo se saltaba la lectura de texto del usuario después de haber ingresado una opción numérica. |
| **Consulta** | "En mi ConsoleMenu de Java, cuando pido un entero con nextInt() y luego pido un String con nextLine(), el programa se salta la lectura del String y sigue de largo. ¿Por qué pasa esto?" |
| **Respuesta** | Se diagnosticó el problema clásico del "salto de línea residual". Se explicó que `nextInt()` no consume el carácter especial de Enter (`\n`), lo que hace que el siguiente `nextLine()` lo lea como una cadena vacía. Se sugirió añadir un `scanner.nextLine()` adicional para limpiar el buffer. |
| **Decisión** | Se incorporó la limpieza del buffer en todos los flujos de captura de datos de la interfaz de usuario, estabilizando la navegación entre los menús de Accesorios, Promociones y Devoluciones. |
| **Commit relacionado** | `fix(ui): resolve scanner buffer issue in ConsoleMenu input handling` |

```markdown
## 13. Diseño de Jerarquía de Clases con Abstracción

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 10 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de desarrollo - Rama: `feature/promotion-module` |
| **Objetivo** | Diseñar la estructura base orientada a objetos para los diferentes tipos de promociones, asegurando que compartan atributos comunes pero tengan comportamientos matemáticos distintos. |
| **Consulta** | "¿Cómo creo una clase abstracta en Java para una Promoción de la cual hereden los descuentos por porcentaje y por categoría, obligándolas a calcular el descuento a su manera?" |
| **Respuesta** | Se explicó la sintaxis de `public abstract class Promotion`, mostrando cómo definir atributos `protected` (id, name, startDate) y el método abstracto `abstract double calculateDiscount(Sale sale);` para forzar la implementación en las clases hijas. |
| **Decisión** | Se creó la clase abstracta `Promotion` como base del modelo. Luego, se implementaron las subclases `PercentageDiscount`, `CategoryDiscount` y `BulkPurchaseDiscount` heredando de ella y sobreescribiendo el método de cálculo. |
| **Commit relacionado** | `feat(promotion): create abstract Promotion class and discount subclasses` |

<br>

## 14. Parseo de Archivos CSV a Objetos Java (POJOs)

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 14 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de persistencia - Rama: `feature/accessory-module` |
| **Objetivo** | Convertir las cadenas de texto leídas desde los archivos de simulación de base de datos (`data/accessories.csv`) en instancias reales de la clase `Accessory` dentro del repositorio. |
| **Consulta** | "Estoy leyendo un CSV con BufferedReader en mi repositorio. Si una línea viene como '1,Control Xbox,60.0,Inalámbrico', ¿cómo la corto y la convierto en un objeto Accessory en Java?" |
| **Respuesta** | Se indicó cómo utilizar el método `String.split(",")` para separar la línea en un arreglo de Strings. Posteriormente, se enseñó el uso de Wrappers como `Double.parseDouble()` y `Integer.parseInt()` para adaptar los tipos antes de pasarlos al constructor del objeto. |
| **Decisión** | Se integró el método `split(",")` dentro de un bloque `try-catch` en el `AccessoryRepository`. Se mapearon correctamente los índices del arreglo a los atributos de la clase al instanciarla. |
| **Commit relacionado** | `feat(accessory): implement CSV parsing logic in AccessoryRepository load method` |

<br>

## 15. Manejo de Fechas con la API java.time

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 15 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de desarrollo - Rama: `feature/warranty-module` |
| **Objetivo** | Calcular de forma precisa la fecha de vencimiento de las garantías, sumando meses o años a la fecha original de la venta, considerando años bisiestos y la duración exacta de los meses. |
| **Consulta** | "Tengo la fecha de compra de la venta guardada en un objeto LocalDate. ¿Cómo le sumo 6 meses o 1 año dependiendo de si el cliente compró la garantía básica o la extendida?" |
| **Respuesta** | Se sugirió descartar el uso de utilidades antiguas como `java.util.Date` o `Calendar` y aprovechar los métodos inmutables de la API moderna de Java: `purchaseDate.plusMonths(6)` y `purchaseDate.plusYears(1)`. |
| **Decisión** | Se implementó el atributo `expirationDate` de tipo `LocalDate` en la clase `Warranty`. Su valor se calcula dinámicamente en el constructor del servicio de garantías usando `plusMonths()` según el tipo elegido por el usuario. |
| **Commit relacionado** | `feat(warranty): implement precise expiration date calculation using LocalDate` |

<br>

## 16. Prevención de NullPointerException en Relaciones Opcionales

| Campo | Contenido |
| :--- | :--- |
| **Fecha** | 16 de septiembre de 2026 |
| **Herramienta** | Gemini Pro |
| **Fase y rama** | Fase de pruebas - Rama: `fix/complete-modules-integration` |
| **Objetivo** | Evitar un fallo crítico en la ejecución del programa (`NullPointerException`) al intentar imprimir el recibo de una venta que no cumplió con las condiciones de ninguna promoción. |
| **Consulta** | "Al imprimir el ticket de venta en ConsoleMenu, el programa se cierra con NullPointerException en la línea de la promoción. ¿Cómo valido si un objeto tiene un valor asignado o está vacío antes de llamar a sus métodos getter?" |
| **Respuesta** | Se explicó el concepto de validación de nulidad (Guards). Se sugirió envolver la lógica de impresión del descuento en un condicional `if (sale.getPromotion() != null)`, evitando así que la JVM intente acceder a métodos de una referencia inexistente en la memoria. |
| **Decisión** | Se añadieron guardas de validación (`if != null`) en el método `printTicket()` de la clase `Sale`. Si no hay promoción asignada, el ticket omite la línea de descuento e imprime directamente el total regular. |
| **Commit relacionado** | `fix(sale): guard against NullPointerException when printing sales without promos` |

