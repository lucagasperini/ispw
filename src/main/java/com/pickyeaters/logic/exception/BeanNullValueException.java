package com.pickyeaters.logic.exception;

public class BeanNullValueException extends GenericBeanException {
    public BeanNullValueException(String message) {
        super(message);
    }

    public BeanNullValueException() {
      super("");
    }
}
