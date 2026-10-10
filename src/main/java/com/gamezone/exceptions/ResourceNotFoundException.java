package com.gamezone.exceptions;

public class ResourceNotFoundException extends GameZoneException {
    public ResourceNotFoundException(String resourceType, String identifier) {
        super("El recurso " + resourceType + " con identificador " + identifier + " no fue encontrado");
    }
}
