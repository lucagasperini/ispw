package com.pickyeaters.logic.model;

import com.pickyeaters.logic.controller.LoginController;
import com.pickyeaters.logic.exception.NotImplementedException;

import java.util.Date;

public abstract class User {
    private String id;
    private String password;
    private String email;
    private String firstname;
    private String lastname;

    protected User(String id, String email, String password, String firstname, String lastname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }


    public boolean checkPassword(String password) {
        if(password == null) {
            throw new NotImplementedException();
        }
        return this.password.equals(password);
    }

    public String getID() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
