package com.pickyeaters.logic.model;

import java.util.List;

public class DishSecond extends Dish {

    public DishSecond(String id, String name, String description, List<Ingredient> ingredientList) {
        super(id, name, description, ingredientList);
    }

    @Override
    public String getType() {
        return TYPE_SECOND;
    }
}
