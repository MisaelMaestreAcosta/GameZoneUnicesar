# AI Usage Log - Developer 1
---
**Student:** Juan Esteban Vera Mendoza
**Role:** developer 1 
**Project:** GameZoneUnicesar

---

## Entry 1 :
* **Tool:** ChatGPT
* **Query / Context:**
    * *"porque me sale este error (error: release version 26 not supported) al intentar compilar el código"*
* **AI Output Summary:**Use the same version of the IDE that your colleagues are using*
---
### Entry 2: 
* **Tool:** Gemini
* **Query / Context:**
    * *"como puedo nombrar los commit en ingles "*
* **AI Output Summary:**Recommended Conventional Commits standard,combined with layer prefixes or class scope for example:
feat: add Product abstract class

---

### Entry 3: 
* **Tool:** Gemini
* **Query / Context:**
    * *"como puedo deshacer un commit local"*
    * *"como verifico los cambios guardados en el commit"*
* **AI Output Summary:** The use of `git reset HEAD~1` to safely undo commits was explained. Details were provided on how to inspect the contents of commits using git show

### Entry 4: 
* **Date:** 2026-09-03
* **Tool:** Gemini 
* **Query / Context:**
    * *"porque git reconoce el archivo de analysis como binario al momento de hacer el commit*
* **AI Output Summary:**It is very likely that it was saved using UTF-16 encoding or with a Byte Order Mark (BOM). Git expects plain text in UTF-8; if it detects null bytes or non-standard sequences, it assumes the file is binary.