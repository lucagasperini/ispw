package com.pickyeaters.logic.view;

import com.pickyeaters.logic.bean.FindRestaurantBean;
import com.pickyeaters.logic.bean.RestaurantBean;
import com.pickyeaters.logic.bean.reply.FindRestaurantReply;
import com.pickyeaters.logic.bean.request.AllCityRequest;
import com.pickyeaters.logic.bean.request.FindRestaurantRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.controller.PickieController;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.ResultErrorException;

import java.util.List;
import java.util.Map;

public class FindRestaurantView extends VirtualView {
    private final PickieController pickieController;
    private final FindRestaurantBean findRestaurant;
    private Map<String, RestaurantBean> restaurantMap;
    private final List<String> cityList;
    public FindRestaurantView(Request baseRequest, PickieController pickieController) {
        super(baseRequest);
        this.pickieController = pickieController;
        this.restaurantMap = null;
        this.findRestaurant = new FindRestaurantBean();
        try {
            cityList = List.copyOf(pickieController.allCity(new AllCityRequest(baseRequest)).getValue().getCityList());
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_FETCH");
        }
    }

    private void checkIfSearched() throws GenericViewException {
        if(restaurantMap == null) {
            throw new GenericViewException("Cannot do this operation without search first!", "");
        }
    }

    public void insertCity(String city) {
        findRestaurant.setCity(city);
        notifyAllObserver();
    }

    public String showCity() {
        return findRestaurant.getCity();
    }

    public void toggleNeedAppetizer() {
        findRestaurant.toggleNeedAppetizer();
        notifyAllObserver();
    }


    public void toggleNeedDrink() {
        findRestaurant.toggleNeedDrink();
        notifyAllObserver();
    }


    public void toggleNeedDessert() {
        findRestaurant.toggleNeedDessert();
        notifyAllObserver();
    }


    public void toggleNeedContour() {
        findRestaurant.toggleNeedContour();
        notifyAllObserver();
    }


    public void toggleNeedFirst() {
        findRestaurant.toggleNeedFirst();
        notifyAllObserver();
    }


    public void toggleNeedSecond() {
        findRestaurant.toggleNeedSecond();
        notifyAllObserver();
    }

    public void startSearch() {
        try {
            if(findRestaurant.getCity() == null || findRestaurant.getCity().isEmpty()) {
                throw new GenericViewException("Please, insert a city first!", "");
            }
            FindRestaurantRequest request = new FindRestaurantRequest(baseRequest, findRestaurant);
            FindRestaurantReply reply = pickieController.findRestaurant(request).getValue();
            restaurantMap = reply.getRestaurantMap();
            notifyAllObserver();
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_RESTAURANT_SEARCH");
        }
    }

    public List<String> showRestaurantID() {
        return List.copyOf(restaurantMap.keySet());
    }

    private RestaurantBean getRestaurant(String restaurantID) {
        checkIfSearched();
        return restaurantMap.get(restaurantID);
    }

    public String showRestaurantName(String vid) {
        return getRestaurant(vid).getName();
    }

    public String showRestaurantAddress(String vid) {
        return getRestaurant(vid).getAddress();
    }

    public String showRestaurantPhone(String vid) {
        return getRestaurant(vid).getPhone();
    }

    public String showRestaurantCity(String vid) {
        return  getRestaurant(vid).getCity();
    }

    public List<String> showAllCity() {
        return cityList;
    }
}
