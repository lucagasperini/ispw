package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.model.Restaurant;

import java.util.List;
import java.util.Optional;

public class RestaurantRepositoryFS implements RestaurantRepository {

    public Optional<Restaurant> findRestaurantByOwner(String userID) {
        throw new NotImplementedException();
    }

    public void editRestaurantByOwner(String restaurateurID, Restaurant restaurant) {
        throw new NotImplementedException();
    }

    public List<Restaurant> findRestaurantByCity(String city) {
        throw new NotImplementedException();
    }

    public List<String> allCity() {
        throw new NotImplementedException();
    }
}