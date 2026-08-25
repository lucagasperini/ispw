package com.pickyeaters.logic.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;

public class FileLogger implements Logger {
    private final String filePath;

    public FileLogger(String filePath) {
        this.filePath = filePath;
        info("Starting Pickyeater...");
    }

    @Override
    public void info(String message) {
        writeToFile("INFO", message);
    }

    @Override
    public void warn(String message) {
        writeToFile("WARN", message);
    }

    @Override
    public void error(String message, Throwable t) {
        // Log the main message first
        writeToFile("ERROR", message + " | Exception: " + t.getClass().getName());
        // Also log the full stack trace to the file
        logStackTrace(t);
    }

    private void writeToFile(String level, String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath, true))) {
            out.println("[" + java.time.LocalDateTime.now(ZoneId.systemDefault()) + "] [" + level + "] " + message);
        } catch (IOException e) {
            // If we fail to write the log file, at least print it to console for debugging
            System.err.println("FATAL ERROR: Could not write to log file " + filePath + ". Error: " + e.getMessage());
        }
    }

    private void logStackTrace(Throwable t) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath, true))) {
            out.println("\n--- Stack Trace Start ---");
            t.printStackTrace(out); // Write stack trace directly to the output stream
            out.println("--- Stack Trace End ---\n");
        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not write stack trace to log file " + filePath + ". Error: " + e.getMessage());
        }
    }
}