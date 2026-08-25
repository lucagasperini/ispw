package com.pickyeaters.logic.bean.request;

import com.pickyeaters.logic.bean.RestaurantBean;

public class EditRestaurantRequest extends Request {
    private final RestaurantBean restaurant;
    public EditRestaurantRequest(Request baseRequest, RestaurantBean restaurant) {
        super(baseRequest);
        this.restaurant = restaurant;
    }

    public RestaurantBean getRestaurant() {
        return restaurant;
    }
}
