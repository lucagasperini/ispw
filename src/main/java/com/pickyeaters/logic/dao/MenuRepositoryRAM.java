package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.model.*;
import com.pickyeaters.logic.utils.LiteralMessage;
import com.pickyeaters.logic.utils.Logger;

import java.util.*;

public class MenuRepositoryRAM implements MenuRepository {
    private final Logger logger;
    private Map<String, List<Dish>> map = new HashMap<>();
    private int nextDishID = 100;

    public MenuRepositoryRAM(Logger logger, IngredientRepository ingredientRepository) {
        this.logger = logger;

        String i1 = "Ingredient 1";
        String i2 = "Ingredient 2";
        String i3 = "Ingredient 3";
        String i4 = "Ingredient 4";
        String i5 = "Ingredient 5";

        List<Ingredient> li1 = new ArrayList<>();
        li1.add(ingredientRepository.findIngredientByName(i1).orElseThrow());
        li1.add(ingredientRepository.findIngredientByName(i2).orElseThrow());
        List<Ingredient> li2 = new ArrayList<>();
        li2.add(ingredientRepository.findIngredientByName(i1).orElseThrow());
        li2.add(ingredientRepository.findIngredientByName(i3).orElseThrow());
        List<Ingredient> li3 = new ArrayList<>();
        li3.add(ingredientRepository.findIngredientByName(i2).orElseThrow());
        List<Ingredient> li4 = new ArrayList<>();
        li4.add(ingredientRepository.findIngredientByName(i5).orElseThrow());
        List<Ingredient> li5 = new ArrayList<>();
        li5.add(ingredientRepository.findIngredientByName(i2).orElseThrow());
        li5.add(ingredientRepository.findIngredientByName(i3).orElseThrow());
        li5.add(ingredientRepository.findIngredientByName(i4).orElseThrow());
        List<Ingredient> li6 = new ArrayList<>();
        li6.add(ingredientRepository.findIngredientByName(i5).orElseThrow());
        List<Ingredient> li7 = new ArrayList<>();
        li7.add(ingredientRepository.findIngredientByName(i1).orElseThrow());
        li7.add(ingredientRepository.findIngredientByName(i2).orElseThrow());
        List<Ingredient> li8 = new ArrayList<>();
        li8.add(ingredientRepository.findIngredientByName(i3).orElseThrow());
        List<Ingredient> li9 = new ArrayList<>();
        li9.add(ingredientRepository.findIngredientByName(i5).orElseThrow());

        List<Dish> r1 = new ArrayList<>();
        r1.add(new DishFirst("1", "R1 Dish name 1", "R1 Description 1", li1));
        r1.add(new DishSecond("2", "R1 Dish name 2", "R1 Description 2", li2));
        r1.add(new DishDrink("3", "R1 Dish name 3", "R1 Description 3", li3));
        map.put("1", r1);

        List<Dish> r2 = new ArrayList<>();
        r2.add(new DishFirst("4", "R2 Dish name 1", "R2 Description 1", li4));
        r2.add(new DishSecond("5", "R2 Dish name 2", "R2 Description 2", li5));
        r2.add(new DishDrink("6", "R2 Dish name 3", "R3 Description 3", li6));
        map.put("2", r2);

        List<Dish> r3 = new ArrayList<>();
        r3.add(new DishFirst("7", "R3 Dish name 1", "R3 Description 1", li7));
        r3.add(new DishSecond("8", "R3 Dish name 2", "R3 Description 2", li8));
        r3.add(new DishDrink("9", "R3 Dish name 3", "R3 Description 3", li9));
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
        GenericRepositoryException e = new GenericRepositoryException(LiteralMessage.MENU_REPOSITORY_CANT_REMOVE_DISH);
        logger.error(e.getMessage(), e);
        throw e;
    }

    public Optional<Dish> findDishByID(String restaurantID, String dishID) {
        for (Dish i : map.get(restaurantID)) {
            if (i.getID().equals(dishID)) {
                return Optional.of(i);
            }
        }
        GenericRepositoryException e = new GenericRepositoryException(LiteralMessage.MENU_REPOSITORY_CANT_FIND_DISH);
        logger.error(e.getMessage(), e);
        throw e;
    }

    public Optional<Dish> findDishByID(String dishID) {
        for(List<Dish> dishList : map.values()) {
            for (Dish i : dishList) {
                if (i.getID().equals(dishID)) {
                    return Optional.of(i);
                }
            }
        }
        GenericRepositoryException e = new GenericRepositoryException(LiteralMessage.MENU_REPOSITORY_CANT_FIND_DISH);
        logger.error(e.getMessage(), e);
        throw e;
    }


    public void editDish(String restaurantID, Dish dish) {
        removeDish(restaurantID, dish.getID());
        map.get(restaurantID).add(dish);
    }
}
