package com.pickyeaters.logic.bean;

import com.pickyeaters.logic.controller.LoginController;

public class AuthBean {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = LoginController.hashPassword(password);
    }

}
