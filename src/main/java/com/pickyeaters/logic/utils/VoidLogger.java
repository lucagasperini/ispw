package com.pickyeaters.logic.utils;

public class VoidLogger implements Logger {

    public void info(String message) {
        // Simply do nothing
    }
    public void warn(String message) {
        // Simply do nothing
    }
    public void error(String message, Throwable t) {
        // Simply do nothing
    }
}
