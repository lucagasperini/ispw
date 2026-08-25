package com.pickyeaters.logic.bean.reply;

import com.pickyeaters.logic.bean.RestaurantBean;

import java.util.List;
import java.util.Map;

public class FindRestaurantReply {
    private final Map<String, RestaurantBean> restaurantMap;
    public FindRestaurantReply(Map<String, RestaurantBean> restaurantMap) {
        this.restaurantMap = restaurantMap;
    }

    public Map<String, RestaurantBean> getRestaurantMap() {
        return restaurantMap;
    }
}
