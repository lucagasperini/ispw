package com.pickyeaters.logic.bean.request;

import com.pickyeaters.logic.bean.DishBean;

public class AddDishRequest extends Request {
    private final DishBean dish;

    public AddDishRequest(Request request, DishBean dish) {
        super(request);
        this.dish = dish;
    }

    public DishBean getDish() {
        return dish;
    }
}
