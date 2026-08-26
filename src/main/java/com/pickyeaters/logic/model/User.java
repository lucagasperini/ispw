package com.pickyeaters.logic.model;

import com.pickyeaters.logic.exception.NotImplementedException;

import java.io.Serializable;

public abstract class User implements Serializable {
    private final String id;
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
