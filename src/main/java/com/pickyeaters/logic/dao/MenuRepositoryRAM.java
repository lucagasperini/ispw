package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.model.*;
import com.pickyeaters.logic.utils.Logger;

import java.util.*;

public class MenuRepositoryRAM implements MenuRepository {
    private final Logger logger;
    // Map<RestaurantID, Menu>
    private Map<String, List<Dish>> map = new HashMap<>();
    private List<Ingredient> ingredientList = new ArrayList<>();
    private int nextDishID = 100;

    public MenuRepositoryRAM(Logger logger) {
        this.logger = logger;
        Allergen a1 = new Allergen("1", "Allergen 1");
        Allergen a2 = new Allergen("2", "Allergen 2");
        Allergen a3 = new Allergen("3", "Allergen 3");
        Allergen a4 = new Allergen("4", "Allergen 4");

        List<Allergen> la1 = new ArrayList<>();
        la1.add(a1);
        la1.add(a2);
        List<Allergen> la2 = new ArrayList<>();
        la2.add(a3);
        la2.add(a4);
        List<Allergen> la3 = new ArrayList<>();
        la3.add(a1);
        la3.add(a4);
        List<Allergen> la4 = new ArrayList<>();
        la4.add(a3);
        List<Allergen> la5 = new ArrayList<>();

        /*
        Ingredient i1 = new Ingredient("1", "Ingredient 1", la1, false, false);
        Ingredient i2 = new Ingredient("2", "Ingredient 2", la2, true, true);
        Ingredient i3 = new Ingredient("3", "Ingredient 3", la3, false, false);
        Ingredient i4 = new Ingredient("4", "Ingredient 4", la4, true, false);
        Ingredient i5 = new Ingredient("5", "Ingredient 5", la5, false, true);
        */

        Ingredient i1 = new Ingredient("1", "Ingredient 1", la1, false, false);
        Ingredient i2 = new Ingredient("2", "Ingredient 2", la2, false, false);
        Ingredient i3 = new Ingredient("3", "Ingredient 3", la3, false, false);
        Ingredient i4 = new Ingredient("4", "Ingredient 4", la4, false, false);
        Ingredient i5 = new Ingredient("5", "Ingredient 5", la5, false, false);

        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);
        ingredientList.add(i4);
        ingredientList.add(i5);

        List<Ingredient> li1 = new ArrayList<>();
        li1.add(i1);
        li1.add(i2);
        List<Ingredient> li2 = new ArrayList<>();
        li2.add(i1);
        li2.add(i3);
        List<Ingredient> li3 = new ArrayList<>();
        li3.add(i2);
        List<Ingredient> li4 = new ArrayList<>();
        li4.add(i5);
        List<Ingredient> li5 = new ArrayList<>();
        li5.add(i2);
        li5.add(i3);
        li5.add(i4);
        List<Ingredient> li6 = new ArrayList<>();
        li6.add(i5);
        List<Ingredient> li7 = new ArrayList<>();
        li7.add(i1);
        li7.add(i2);
        List<Ingredient> li8 = new ArrayList<>();
        li8.add(i3);
        List<Ingredient> li9 = new ArrayList<>();
        li9.add(i5);

        List<Dish> r1 = new ArrayList<>();
        r1.add(new DishFirst("1", "R1 Dish name 1", "Description 1", li1));
        r1.add(new DishSecond("2", "R1 Dish name 2", "Description 2", li2));
        r1.add(new DishDrink("3", "R1 Dish name 3", "Description 3", li3));
        map.put("1", r1);

        List<Dish> r2 = new ArrayList<>();
        r2.add(new DishFirst("4", "R2 Dish name 1", "Description 1", li4));
        r2.add(new DishSecond("5", "R2 Dish name 2", "Description 2", li5));
        r2.add(new DishDrink("6", "R2 Dish name 3", "Description 3", li6));
        map.put("2", r2);

        List<Dish> r3 = new ArrayList<>();
        r3.add(new DishFirst("7", "R3 Dish name 1", "Description 1", li7));
        r3.add(new DishSecond("8", "R3 Dish name 2", "Description 2", li8));
        r3.add(new DishDrink("9", "R3 Dish name 3", "Description 3", li9));
        map.put("3", r3);
    }

    public List<Dish> findMenuByRestaurantID(String restaurantID){
        return map.get(restaurantID);
    }

    public void addDish(String restaurantID, Dish dish) {
        dish.setID(String.valueOf(nextDishID));
        nextDishID++;
        map.get(restaurantID).add(dish);
    }


    public void removeDish(String restaurantID, String dishID) {
        // TODO: Throw exception if no restaurantID on map
        for (Dish i : map.get(restaurantID)) {
            if (i.getID().equals(dishID)) {
                map.get(restaurantID).remove(i);
                return;
            }
        }

        throw new GenericRepositoryException("Cannot remove selected dish from restaurant");
    }

    public Optional<Dish> findDishByID(String restaurantID, String dishID) {
        for (Dish i : map.get(restaurantID)) {
            if (i.getID().equals(dishID)) {
                return Optional.of(i);
            }
        }
        throw new GenericRepositoryException("Cannot find selected dish from restaurant");
    }

    public Optional<Dish> findDishByID(String dishID) {
        for(List<Dish> dishList : map.values()) {
            for (Dish i : dishList) {
                if (i.getID().equals(dishID)) {
                    return Optional.of(i);
                }
            }
        }
        throw new GenericRepositoryException("Cannot find selected dish from restaurant");
    }


    public void editDish(String restaurantID, Dish dish) {
        removeDish(restaurantID, dish.getID());
        map.get(restaurantID).add(dish);
    }
}
