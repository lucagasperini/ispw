package com.pickyeaters.logic.exception;

public class LoginControllerPermissionException extends RuntimeException {
    public LoginControllerPermissionException(String message) {
        super(message);
    }
}
