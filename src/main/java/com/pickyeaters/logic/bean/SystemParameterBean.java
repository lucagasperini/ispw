package com.pickyeaters.logic.bean;

import com.pickyeaters.logic.exception.BeanInvalidValueException;
import com.pickyeaters.logic.exception.BeanNullValueException;

import java.util.Set;

public class SystemParameterBean {
    private static final Set<String> ACCEPTED_LOCALE_LANG = Set.of( "it", "en" );

    private static final String ACCEPTED_PROVIDER_RAM = "ram";
    private static final String ACCEPTED_PROVIDER_POSTGRES = "psql";
    private static final Set<String> ACCEPTED_PROVIDER = Set.of(ACCEPTED_PROVIDER_RAM, ACCEPTED_PROVIDER_POSTGRES);

    private String logFile = "";
    private String configFile = "";
    private String localeLang = "";
    private String databaseName = "";
    private String databaseHost = "";
    private String databasePort = "";
    private String databasePassword = "";
    private String databaseUser = "";
    private String provider = "";

    public String getLogFile() {
        return logFile;
    }

    public String getConfigFile() {
        return configFile;
    }


    public String getLocaleLang() {
        return localeLang;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getProvider() {
        return provider;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public void setLocaleLang(String localeLang) {
        if(ACCEPTED_LOCALE_LANG.contains(localeLang)) {
            this.localeLang = localeLang;
        } else {
            throw new BeanInvalidValueException("Locale can be only 'it' or 'en'!");
        }
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public void setProvider(String provider) {
        if(provider == null) {
            throw new BeanNullValueException();
        }

        if(ACCEPTED_PROVIDER.contains(provider)) {
            this.provider = provider;
        } else {
            throw new BeanInvalidValueException("Provider can be only 'psql' or 'ram'!");
        }
    }

    public void setupFromKey(String key, String value) {
        // Dynamically set the parameter based on the key
        switch (key) {
            case "logFile":
                this.setLogFile(value);
                break;
            case "configFile":
                this.setConfigFile(value);
                break;
            case "localeLang":
                this.setLocaleLang(value);
                break;
            case "databaseName":
                this.setDatabaseName(value);
                break;
            case "databaseHost":
                this.setDatabaseHost(value);
                break;
            case "databasePort":
                this.setDatabasePort(value);
                break;
            case "databaseUser":
                this.setDatabaseUser(value);
                break;
            case "databasePassword":
                this.setDatabasePassword(value);
                break;
            case "provider":
                this.setProvider(value);
                break;
            default:
                break;
        }
    }

    public boolean isProviderRAM() {
        if(this.provider == null) {
            throw new BeanNullValueException();
        }
        return ACCEPTED_PROVIDER_RAM.equals(this.provider);
    }

    public boolean isProviderPostgres() {
        if(this.provider == null) {
            throw new BeanNullValueException();
        }
        return ACCEPTED_PROVIDER_POSTGRES.equals(this.provider);
    }
}
