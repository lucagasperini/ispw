package com.pickyeaters.logic.bean.request;

public class Request {
    private final String token;

    public Request(String token) {
        this.token = token;
    }

    public Request(Request request) {
        this.token = request.getToken();
    }

    public String getToken() {
        return token;
    }
}
