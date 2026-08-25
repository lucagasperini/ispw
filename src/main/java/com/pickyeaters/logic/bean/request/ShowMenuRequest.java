package com.pickyeaters.logic.bean.request;

public class ShowMenuRequest extends Request {
    private final String restaurantID;

    public ShowMenuRequest(Request request, String restaurantID) {
        super(request);
        this.restaurantID = restaurantID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }
}
