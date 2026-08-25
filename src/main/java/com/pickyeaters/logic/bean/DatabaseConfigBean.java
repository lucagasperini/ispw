package com.pickyeaters.logic.bean;

import com.pickyeaters.logic.exception.BeanConversionException;
import com.pickyeaters.logic.exception.BeanNullValueException;

public class DatabaseConfigBean {
    private String driver;
    private String host;
    private int port;
    private String name;
    private String user;
    private String password;

    public String getDriver() {
        return driver;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setDriver(String driver) throws BeanConversionException {
        if(driver == null) {
            throw new BeanNullValueException();
        }
        this.driver = driver;
    }

    public void setHost(String host) throws BeanConversionException {
        if(host == null) {
            throw new BeanNullValueException();
        }
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPort(String port) throws BeanConversionException{
        try {
            this.port = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            throw new BeanConversionException("Database port invalid format.");
        }
    }

    public void setName(String name) throws BeanConversionException {
        if(name == null) {
            throw new BeanNullValueException();
        }
        this.name = name;
    }

    public void setUser(String user) throws BeanNullValueException {
        if(user == null) {
            throw new BeanNullValueException();
        }
        this.user = user;
    }

    public void setPassword(String password) throws BeanConversionException {
        if(password == null) {
            throw new BeanNullValueException();
        }
        this.password = password;
    }
}
