package com.pickyeaters.logic.utils;

public interface Logger {
    void info(String message);
    void warn(String message);
    void error(String message, Throwable t);
}