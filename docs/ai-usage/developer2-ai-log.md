# Bitácora de Uso de IA — Desarrollador 2 (Módulo de Personas)

Esta bitácora documenta el uso de asistencia de IA (Claude) durante el desarrollo del módulo de Personas del sistema GameZone Unicesar, según lo exige la política de uso de IA del taller.

## Sesión 1 — Flujo de trabajo con Git y configuración del entorno

**Propósito:** Entender el flujo de Git exigido por el taller y cómo aplicarlo en mi entorno local (PowerShell + NetBeans).

**Preguntas realizadas a la IA:**
- Aclaración sobre qué hace `git checkout develop && git pull` y por qué es necesario antes de empezar a trabajar.
- Consulta sobre si la rama `develop` debía ser creada por el Líder Técnico antes de que los desarrolladores puedan ramificar desde ella.
- Consulta sobre si se necesita una rama nueva por cada clase, o una sola rama por módulo.
- Solicitud de una lista completa de comandos Git para el flujo de trabajo del desarrollador (clonar, crear rama, commit, push, merge, PR, limpieza).
- Consulta sobre si el paso de clonar debe repetirse cada vez que se enciende el computador.
- Consulta sobre cómo adaptar el flujo de trabajo a NetBeans y específicamente a PowerShell.
- Consulta sobre la diferencia entre `git add <archivo>` y `git add .`, y cuál usar para mantener los commits atómicos.

**Cómo se usaron las respuestas de la IA:**
- Se confirmó que el Líder Técnico es responsable de crear `main` y `develop`; yo solo creo mi rama a partir de `develop` una vez que esta exista.
- Se confirmó que se usa una sola rama feature (`feature/person-module`) para todo el módulo, con un commit por cada cambio lógico (por clase), en vez de una rama por clase.
- Se utilizó la secuencia de comandos sugerida (clonar → checkout develop → pull → crear rama feature → add/commit/push por clase → merge de develop → abrir PR → limpieza) como el flujo de trabajo real seguido durante la implementación.
- Se adoptó la práctica de usar `git add <archivo-específico>` en lugar de `git add .` para mantener cada commit enfocado en una sola clase, en línea con el requisito de "commit atómico".

**Decisiones tomadas por mí (no delegadas a la IA):**
- El diseño real de `Person`, `Client`, `Seller`, `PersonRepository` y `PersonService` (atributos, métodos, relaciones) se definió con base en el contexto de negocio del taller y el análisis propio del equipo — no fue generado por la IA.
- No se le pidió a la IA que respondiera las preguntas orientadoras de `analysis.md`, ni que diseñara los diagramas de clases, jerarquías o capas.

## Sesión 2 — Prácticas de documentación

**Propósito:** Aclarar en qué momento debe agregarse la documentación JavaDoc durante el desarrollo.

**Preguntas realizadas a la IA:**
- Consulta sobre si el JavaDoc debe agregarse durante la creación de cada clase o como un paso separado al final.

**Cómo se usó la respuesta:**
- Se adoptó la práctica de escribir el JavaDoc de cada clase y método público al mismo tiempo que se escribe la clase, de modo que cada commit incluya código y documentación juntos.

## Sesión 3 — Preparación del Pull Request

**Propósito:** Obtener ayuda para redactar un título y descripción claros en inglés antes de solicitar la revisión.

**Preguntas realizadas a la IA:**
- Solicitud de un título y descripción sugeridos (en inglés) que resumieran los cambios realizados en el módulo de personas.

**Cómo se usó la respuesta:**
- Se usó el título/descripción sugeridos como plantilla inicial, adaptada para reflejar con precisión las clases y métodos realmente implementados en mi propio código.

## Resumen de clasificación del uso de IA

| Uso | Tipo |
|---|---|
| Explicación de comandos y flujo de Git | Legítimo (ayuda conceptual/de herramientas) |
| Aclaración de la estrategia de ramificación según las reglas del taller | Legítimo (ayuda conceptual) |
| Explicación del comportamiento de `git add` | Legítimo (ayuda de herramientas) |
| Sugerencia de redacción del título/descripción del PR | Legítimo (ayuda de redacción/documentación) |
| Diseño de clases, atributos y lógica de negocio | No delegado a la IA — realizado de forma independiente |
| Respuestas de analysis.md | No delegado a la IA — realizado de forma independiente |
