package com.pickyeaters.logic.model;

import java.sql.Connection;

public class DatabaseConnection {
    private Connection connection = null;

    public DatabaseConnection() {
        this.connection = null;
    }
    public DatabaseConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public boolean isConnected() {
        return this.connection != null;
    }

}
