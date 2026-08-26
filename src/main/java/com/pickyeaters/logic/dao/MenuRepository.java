package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.model.Dish;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    List<Dish> findMenuByRestaurantID(String restaurantID);
    void addDish(String restaurantID, Dish dish);
    void removeDish(String dishID);
    Optional<Dish> findDishByID(String dishID);
    void editDish(Dish dish);
    Optional<Dish> findDishByName(String restaurantID, String dishName);
}
