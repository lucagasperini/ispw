package com.pickyeaters.logic.view.restaurant;

import com.pickyeaters.logic.bean.RestaurantBean;
import com.pickyeaters.logic.bean.reply.ShowRestaurantReply;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.bean.request.ShowRestaurantRequest;
import com.pickyeaters.logic.controller.RestaurantController;
import com.pickyeaters.logic.view.VirtualView;

abstract class RestaurantView extends VirtualView {
    protected final RestaurantController controller;
    protected RestaurantBean restaurant;

    public RestaurantView(Request baseRequest, RestaurantController restaurantController, String restaurantID) {
        super(baseRequest);
        controller = restaurantController;

        ShowRestaurantRequest request = new ShowRestaurantRequest(baseRequest, restaurantID);
        ShowRestaurantReply reply = restaurantController.showRestaurant(request).getValue();
        restaurant = reply.getRestaurant();
    }

    public String showName() {
        return restaurant.getName();
    }

    public String showAddress() {
        return restaurant.getAddress();
    }

    public String showPhone() {
        return restaurant.getPhone();
    }

    public String showCity() {
        return  restaurant.getCity();
    }
}
