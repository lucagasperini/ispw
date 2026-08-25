package com.pickyeaters.logic.exception;

public class GenericViewException extends RuntimeException {
    private final String key;

    public GenericViewException(String message, String key) {
        super(message);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
