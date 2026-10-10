package com.gamezone.exceptions;

public abstract class GameZoneException extends RuntimeException {
    private String errorCode;

    public GameZoneException(String message) {
        super(message);
    }

    public GameZoneException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
