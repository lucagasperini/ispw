package com.pickyeaters.logic.bean.request;

import com.pickyeaters.logic.bean.FindRestaurantBean;


public class FindRestaurantRequest extends Request {
    private final FindRestaurantBean findRestaurant;
    public FindRestaurantRequest(Request request, FindRestaurantBean findRestaurant) {
        super(request);
        this.findRestaurant = new FindRestaurantBean(findRestaurant);
    }

    public FindRestaurantBean getFindRestaurant() {
        return findRestaurant;
    }
}
