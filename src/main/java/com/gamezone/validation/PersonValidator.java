package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Person;
import java.util.List;

/** Centraliza las validaciones de datos e identidad de clientes y vendedores. */
public final class PersonValidator {

    private PersonValidator() {
    }

    /** Valida los datos de una persona y el formato básico del correo opcional. */
    public static void validatePersonData(String id, String name, String email, String phone) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("identificador", "no puede estar vacío");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("nombre", "no puede estar vacío");
        }
        if (phone == null || phone.isBlank()) {
            throw new InvalidDataException("teléfono", "no puede estar vacío");
        }
        if (email != null && !email.isBlank()
                && (!email.contains("@") || !email.substring(email.indexOf('@') + 1).contains("."))) {
            throw new InvalidDataException("correo electrónico", "debe contener @ y un punto después de @");
        }
    }

    /** Comprueba que la persona solicitada exista. */
    public static void validatePersonExists(Person person, String personId) {
        if (person == null) {
            throw new ResourceNotFoundException("persona", personId);
        }
    }

    /** Impide registrar otra persona con un identificador ya utilizado. */
    public static void validateUniquePerson(List<Person> existing, String newId) {
        if (newId == null || newId.isBlank()) {
            throw new InvalidDataException("identificador", "no puede estar vacío");
        }
        if (existing != null && existing.stream()
                .filter(person -> person != null)
                .anyMatch(person -> newId.equalsIgnoreCase(person.getId()))) {
            throw new BusinessRuleException("Ya existe una persona registrada con el ID: " + newId);
        }
    }
}
