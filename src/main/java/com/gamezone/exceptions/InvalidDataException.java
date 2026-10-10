package com.gamezone.exceptions;

public class InvalidDataException extends GameZoneException {
    public InvalidDataException(String fieldName, String reason) {
        super("El campo " + fieldName + " no es válido: " + reason);
    }
}
