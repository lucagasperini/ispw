package com.pickyeaters.logic.factory;

import com.pickyeaters.logic.exception.GenericFactoryException;
import com.pickyeaters.logic.model.*;
import com.pickyeaters.logic.utils.Logger;

import java.util.List;

public class DishFactory {
    private final Logger logger;

    public DishFactory(Logger logger) {
        this.logger = logger;
    }

    public Dish createDish(String id, String name, String description, String type, List<Ingredient> ingredientList) {
        if(name.isEmpty()) {
            GenericFactoryException e = new GenericFactoryException("Dish must have a name");
            logger.error(e.getMessage(), e);
            throw e;
        }

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
                GenericFactoryException e = new GenericFactoryException("Cannot find dish type: " + type);
                logger.error(e.getMessage(), e);
                throw e;
        }
    }
}
