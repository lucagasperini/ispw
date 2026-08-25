package com.pickyeaters.logic.factory;

import com.pickyeaters.logic.bean.DishBean;
import com.pickyeaters.logic.bean.request.AddDishRequest;
import com.pickyeaters.logic.bean.DishIngredientBean;
import com.pickyeaters.logic.dao.IngredientRepository;
import com.pickyeaters.logic.dao.MenuRepository;
import com.pickyeaters.logic.exception.GenericFactoryException;
import com.pickyeaters.logic.model.*;
import com.pickyeaters.logic.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class DishFactory {
    private final Logger logger;

    public DishFactory(Logger logger) {
        this.logger = logger;
    }

    public Dish createDish(String id, String name, String description, String type, List<Ingredient> ingredientList) {
        switch (type) {
            case Dish.TYPE_APPETIZER:
                return new DishAppetizer(id, name, description, ingredientList);
            case Dish.TYPE_FIRST:
                return new DishFirst(id, name, description, ingredientList);
            case Dish.TYPE_CONTOUR:
                return new DishContour(id, name, description, ingredientList);
            case Dish.TYPE_SECOND:
                return new DishSecond(id, name, description, ingredientList);
            case Dish.TYPE_DESSERT:
                return new DishDessert(id, name, description, ingredientList);
            case Dish.TYPE_DRINK:
                return new DishDrink(id, name, description, ingredientList);
            default:
                throw new GenericFactoryException("Cannot find dish type: " + type);
        }
    }
}
