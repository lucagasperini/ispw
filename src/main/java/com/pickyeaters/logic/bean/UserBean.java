package com.pickyeaters.logic.bean;

import com.pickyeaters.logic.model.User;

public class UserBean {
    private String id;
    private String email;
    private String firstname;
    private String lastname;

    public UserBean(User user) {
        id = user.getID();
        email = user.getEmail();
        firstname = user.getFirstname();
        lastname = user.getLastname();
    }

    public String getID() {
        return id;
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
