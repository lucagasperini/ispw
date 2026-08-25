package com.pickyeaters.logic.view.restaurant;

import com.pickyeaters.logic.bean.request.EditRestaurantRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.controller.RestaurantController;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.ResultErrorException;

public class EditRestaurantView extends RestaurantView {

    public EditRestaurantView(Request baseRequest, RestaurantController restaurantController, String restaurantID) {
        super(baseRequest, restaurantController, restaurantID);
    }

    public void insertName(String name) {
        restaurant.setName(name);
        notifyAllObserver();
    }

    public void insertPhone(String phone) {
        restaurant.setPhone(phone);
        notifyAllObserver();
    }

    public void insertAddress(String address) {
        restaurant.setAddress(address);
        notifyAllObserver();
    }

    public void insertCity(String city) {
        restaurant.setCity(city);
        notifyAllObserver();
    }

    public void submit() throws GenericViewException {
        try {
            EditRestaurantRequest request = new EditRestaurantRequest(baseRequest, restaurant);
            controller.editRestaurant(request).getValue();
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_SAVE");
        }
    }

}
