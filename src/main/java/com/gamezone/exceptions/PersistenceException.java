package com.gamezone.exceptions;

public class PersistenceException extends GameZoneException {
    public PersistenceException(String message, Throwable cause) {
        super(message);
        this.initCause(cause);
    }
}
