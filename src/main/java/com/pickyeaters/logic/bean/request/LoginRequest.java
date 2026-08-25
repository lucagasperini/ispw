package com.pickyeaters.logic.bean.request;

import com.pickyeaters.logic.bean.AuthBean;

public class LoginRequest extends Request {
    final AuthBean auth;
    public LoginRequest(AuthBean auth) {
        super("");
        this.auth = auth;
    }

    public AuthBean getAuth() {
        return auth;
    }
}
