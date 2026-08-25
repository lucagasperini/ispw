package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.model.EatingPreference;
import com.pickyeaters.logic.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantByOwner(String restaurateurID);
    void editRestaurantByOwner(String restaurateurID, Restaurant restaurant);
    List<Restaurant> findRestaurantByCity(String city);
    List<String> allCity();
}
