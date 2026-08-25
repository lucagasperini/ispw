package com.pickyeaters.logic.view;

import com.pickyeaters.logic.bean.AuthBean;
import com.pickyeaters.logic.bean.reply.LoginReply;
import com.pickyeaters.logic.bean.request.LoginRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.controller.LoginController;
import com.pickyeaters.logic.exception.LoginViewException;
import com.pickyeaters.logic.exception.ResultErrorException;

public class LoginView extends VirtualView {

    private final AuthBean auth = new AuthBean();
    private final LoginController loginController;
    private LoginReply loginReply = null;

    public LoginView(LoginController loginController) {
        super(new Request(""));
        this.loginController = loginController;
    }

    public void insertPassword(String password) {
        auth.setPassword(password);
    }

    public void insertEmail(String email) {
        auth.setEmail(email);
    }

    public void login() {
        try {
            loginReply = loginController.login(new LoginRequest(auth)).getValue();
        } catch (ResultErrorException e) {
            throw new LoginViewException("", "BAD_AUTH");
        }
    }

    public void throwIfUserNotLogged() {
        if(loginReply == null) {
            throw new LoginViewException("User is not logged.", "USER_NOT_LOGGED");
        }
    }

    public String getToken() {
        throwIfUserNotLogged();
        return loginReply.getToken();
    }

    public boolean isLoggedRestaurateur() {
        throwIfUserNotLogged();
        return loginReply.getUserType().equals(LoginController.USER_TYPE_RESTAURATEUR);
    }

    public boolean isLoggedPickie() {
        throwIfUserNotLogged();
        return loginReply.getUserType().equals(LoginController.USER_TYPE_PICKIE);
    }

    public boolean isLoggedAdmin() {
        throwIfUserNotLogged();
        return loginReply.getUserType().equals(LoginController.USER_TYPE_ADMIN);
    }

}
