package com.pickyeaters.logic.bean.reply;

import com.pickyeaters.logic.bean.RestaurantBean;

public class ShowRestaurantReply {
    private final String id;

    private final RestaurantBean restaurant;

    public ShowRestaurantReply(String id, RestaurantBean restaurant) {
        this.id = id;
        this.restaurant = restaurant;
    }

    public String getID() {
        return id;
    }

    public RestaurantBean getRestaurant() {
        return restaurant;
    }
}
