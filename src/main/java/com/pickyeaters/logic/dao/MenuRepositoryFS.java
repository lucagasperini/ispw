package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.model.Dish;

import java.util.List;
import java.util.Optional;

public class MenuRepositoryFS implements MenuRepository {

    public List<Dish> findMenuByRestaurantID(String restaurantID) {
        throw new NotImplementedException();
    }

    public void addDish(String restaurantID, Dish dish) {
        throw new NotImplementedException();
    }

    public void removeDish(String dishID) {
        throw new NotImplementedException();
    }

    public Optional<Dish> findDishByID(String dishID) {
        throw new NotImplementedException();
    }

    public void editDish(Dish dish) {
        throw new NotImplementedException();
    }

    public Optional<Dish> findDishByName(String restaurantID, String dishName) {
        throw new NotImplementedException();
    }
}