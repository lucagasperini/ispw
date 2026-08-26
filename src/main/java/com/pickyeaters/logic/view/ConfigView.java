package com.pickyeaters.logic.view;

import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.controller.DatabaseController;
import com.pickyeaters.logic.bean.DatabaseConfigBean;
import com.pickyeaters.logic.bean.SystemParameterBean;
import com.pickyeaters.logic.exception.BeanConversionException;
import com.pickyeaters.logic.exception.BeanInvalidValueException;
import com.pickyeaters.logic.exception.BeanNullValueException;
import com.pickyeaters.logic.exception.ConfigViewException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfigView extends VirtualView {

    private final SystemParameterBean systemParameter;
    private static final String DEFAULT_DRIVER = "postgresql";
    private final DatabaseController databaseController;

    public void loadConfigByFile(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comments or empty lines
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    systemParameter.setupFromKey(key, value);
                }
            }
        } catch (IOException e) {
            throw new ConfigViewException("Error opening the configuration file.");
        }
    }

    public ConfigView(SystemParameterBean systemParameter, DatabaseController databaseController) {
        super(new Request(""));
        this.systemParameter = systemParameter;
        this.databaseController = databaseController;
    }

    public void loadConfig() {
        DatabaseConfigBean bean = new DatabaseConfigBean();
        try {
            bean.setDriver(DEFAULT_DRIVER);
            bean.setHost(systemParameter.getDatabaseHost());
            bean.setName(systemParameter.getDatabaseName());
            bean.setUser(systemParameter.getDatabaseUser());
            bean.setPassword(systemParameter.getDatabasePassword());

            bean.setPort(systemParameter.getDatabasePort());

            this.databaseController.init(bean);
        } catch (BeanConversionException e) {
            throw new ConfigViewException(e.getMessage());
        } catch (BeanNullValueException e) {
            throw  new ConfigViewException("No config provided.");
        }
    }

    public void insertDatabaseName(String databaseName) {
        systemParameter.setDatabaseName(databaseName);
    }
    public void insertDatabaseHost(String databaseHost) {
        systemParameter.setDatabaseHost(databaseHost);
    }
    public void insertDatabasePort(String databasePort) {
        systemParameter.setDatabasePort(databasePort);
    }
    public void insertDatabaseUser(String databaseUser) {
        systemParameter.setDatabaseUser(databaseUser);
    }
    public void insertDatabasePassword(String databasePassword) {
        systemParameter.setDatabasePassword(databasePassword);
    }

    public void selectProvider(String provider) {
        try {
            systemParameter.setProvider(provider);
        } catch (BeanInvalidValueException e) {
            throw new ConfigViewException(e.getMessage());
        }
    }

    public boolean checkedProviderDatabase() {
        try {
            return systemParameter.isProviderPostgres();
        } catch (BeanNullValueException e) {
            throw new ConfigViewException("");
        }
    }

    public boolean checkedProviderRAM() {
        try {
            return systemParameter.isProviderRAM();
        } catch (BeanNullValueException e) {
            throw new ConfigViewException("");
        }
    }

    public boolean checkedProviderFileSystem() {
        try {
            return systemParameter.isProviderFS();
        } catch (BeanNullValueException e) {
            throw new ConfigViewException("");
        }
    }

}
