# AI Usage Log - Developer 1

**Student:** Juan Esteban Vera Mendoza
**Role:** developer 1
**Project:** GameZoneUnicesar

---

### Entrada 1

| Campo | Contenido |
| --- | --- |
| **Fecha** | 2026-09-02 |
| **Herramienta** | ChatGPT |
| **Fase y rama** | fase de desarrollo \ feature/product-module |
| **Objetivo** | Resolver error de versión no soportada al compilar el proyecto. |
| **Consulta** | `"porque me sale este error (error: release version 26 not supported) al intentar compilar el código"` |
| **Respuesta** | Usar la misma versión del IDE/JDK que están utilizando los demás integrantes del equipo. |
| **Decisión** | Se igualó la versión del JDK a la utilizada por el equipo para mantener consistencia. |


---

### Entrada 2

| Campo | Contenido |
| --- | --- |
| **Fecha** | 2026-09-02 |
| **Herramienta** | Gemini |
| **Fase y rama** | fase de integracion / feature/product-class |
| **Objetivo** | Estandarizar la nomenclatura de mensajes de commit en inglés. |
| **Consulta** | `"como puedo nombrar los commit en ingles"` |
| **Respuesta** | Se recomendó el estándar *Conventional Commits* combinado con prefijos de capa o alcance de clases. Ejemplo: `feat: add Product abstract class`. |
| **Decisión** | Se adoptó el estándar *Conventional Commits* para los mensajes del repositorio. |

---

### Entrada 3

| Campo | Contenido |
| --- | --- |
| **Fecha** | 2026-09-03|
| **Herramienta** | Gemini |
| **Fase y rama** | fase de integracion / feature/return-module  |
| **Objetivo** | Deshacer un commit local de forma segura e inspeccionar los cambios guardados. |
| **Consulta** | `"como puedo deshacer un commit local"` / `"como verifico los cambios guardados en el commit"` |
| **Respuesta** | Explicación del comando `git reset HEAD~1` para deshacer commits locales y `git show` para inspeccionar el contenido del commit. |
| **Decisión** | Se ejecutó el *reset* para corregir el commit e inspeccionó con `git show`. |


---

### Entrada 4

| Campo | Contenido |
| --- | --- |
| **Fecha** | 2026-09-03 |
| **Herramienta** | Gemini |
| **Fase y rama** | fase de documentacion / feature/initial-documentation|
| **Objetivo** | Comprender por qué Git detecta un archivo de texto/análisis como binario al hacer commit. |
| **Consulta** | `"porque git reconoce el archivo de analysis como binario al momento de hacer el commit"` |
| **Respuesta** | Ocurre por guardarse en codificación UTF-16 o con Byte Order Mark (BOM). Git requiere UTF-8 plano; si detecta bytes nulos o secuencias no estándar, lo asume como binario. |
| **Decisión** | Se convirtió la codificación del archivo de análisis a UTF-8 sin BOM. |


---

### Entrada 5

| Campo | Contenido |
| --- | --- |
| **Fecha** | 2026-09-17 |
| **Herramienta** | Gemini |
| **Fase y rama** | fase de integracion final \ feature/promotion-module |
| **Objetivo** | Recuperar commits huérfanos que quedaron inaccesibles tras ejecutar un `git push --force`. |
| **Consulta** | `"como recuperar los commits que quedan huerfanos despues de realiza un push forced"` |
| **Respuesta** | Recomendó crear una nueva rama (ej. `ramarescate`) apuntando al hash del último commit que se deseaba recuperar. |
| **Decisión** | Se creó la rama auxiliar con el hash correspondiente para rescatar los cambios. |

---

### Entrada 6

| Campo | Contenido |
| --- | --- |
| **Fecha** | 2026-09-23 |
| **Herramienta** | Gemini |
| **Fase y rama** | fase de integracion final \ feature/accessory-category-discount |
| **Objetivo** | Comprender el funcionamiento e implementación del manejo de excepciones en Java. |
| **Consulta** | `"como funcionan las excepciones y como se implementan en java"` |
| **Respuesta** | Objetos que representan errores en tiempo de ejecución. Tipos: Checked y Unchecked. Uso: `try-catch`, `finally` / `try-with-resources` y `throw`/`throws`. |
| **Decisión** | Se implementaron bloques `try-catch` y `try-with-resources` en los métodos de la capa de servicio/repositorio. |

---

### Entrada 7

| Campo | Contenido |
| --- | --- |
| **Fecha** | 2026-09-27 |
| **Herramienta** | Gemini |
| **Fase y rama** | fase de integración final \ docs/integration-documentation |
| **Objetivo** | Resolver el error `fatal: invalid reference` al intentar cambiar a una rama remota. |
| **Consulta** | `"porque al intentar cambiarme de rama me aparece el siguiente error([ Error, check your command]> git switch docs/integration-documentation,fatal: invalid reference: docs/integration-documentation)"` |
| **Respuesta** | El error indica que Git no encuentra la referencia localmente. Se sugirió actualizar el listado de referencias remotas mediante `git fetch origin`. |
| **Decisión** | Se ejecutó `git fetch origin` para sincronizar las ramas del remoto y luego realizar el `git switch`. |

