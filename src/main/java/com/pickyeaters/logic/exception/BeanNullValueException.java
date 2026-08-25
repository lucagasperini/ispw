package com.pickyeaters.logic.exception;

public class BeanNullValueException extends RuntimeException {
    public BeanNullValueException(String message) {
        super(message);
    }

    public BeanNullValueException() {
      super("");
    }
}
