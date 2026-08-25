package com.pickyeaters.logic.view.restaurant;

import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.controller.RestaurantController;

public class ShowRestaurantView extends RestaurantView {

    public ShowRestaurantView(Request baseRequest, RestaurantController restaurantController, String restaurantID) {
        super(baseRequest, restaurantController, restaurantID);
    }
}
