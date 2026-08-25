package com.pickyeaters.logic.bean.request;

public class ShowRestaurantRequest extends Request {
    private final String restaurantID;

    public ShowRestaurantRequest(Request request, String restaurantID) {
        super(request);
        this.restaurantID = restaurantID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }
}
