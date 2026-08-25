package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.model.Dish;
import com.pickyeaters.logic.model.Ingredient;
import com.pickyeaters.logic.model.Restaurateur;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    List<Dish> findMenuByRestaurantID(String restaurantID);
    void addDish(String restaurantID, Dish dish);
    void removeDish(String restaurantID, String dishID);
    Optional<Dish> findDishByID(String restaurantID, String dishID);
    Optional<Dish> findDishByID(String dishID);
    void editDish(String restaurantID, Dish dish);
}
