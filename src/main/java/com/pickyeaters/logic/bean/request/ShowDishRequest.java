package com.pickyeaters.logic.bean.request;

public class ShowDishRequest extends Request {
    private final String id;

    public ShowDishRequest(Request baseRequest, String id) {
        super(baseRequest);
        this.id = id;
    }

    public String getID() {
        return id;
    }
}
