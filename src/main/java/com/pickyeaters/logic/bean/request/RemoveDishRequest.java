package com.pickyeaters.logic.bean.request;

public class RemoveDishRequest extends Request {
    private final String id;

    public RemoveDishRequest(Request request, String id) {
        super(request);
        this.id = id;
    }

    public String getID() {
        return id;
    }
}
