package com.pickyeaters.logic.bean.reply;

public class LoginReply {
    final String userType;
    final String token;

    public LoginReply(String userType, String token) {
        this.userType = userType;
        this.token = token;
    }

    public String getUserType() {
        return userType;
    }

    public String getToken() {
        return token;
    }
}
