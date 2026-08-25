package com.pickyeaters.logic.bean.request;

import com.pickyeaters.logic.bean.DishBean;

public class ChangeDishRequest extends Request {
    private final String id;
    private final DishBean dish;

    public ChangeDishRequest(Request request, String id, DishBean dish) {
        super(request);
        this.id = id;
        this.dish = dish;
    }

    public String getID() {
        return id;
    }

    public DishBean getDish() {
        return dish;
    }
}
