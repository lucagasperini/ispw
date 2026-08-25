package com.pickyeaters.logic.bean;

import com.pickyeaters.logic.model.Dish;
import java.util.ArrayList;
import java.util.List;

public class FindRestaurantBean {
    private String city;
    private final List<String> dishTypeNeeded;
    public FindRestaurantBean() {
        this.dishTypeNeeded = new ArrayList<>();
    }
    public FindRestaurantBean(FindRestaurantBean other) {
        this.city = other.city;
        this.dishTypeNeeded = new ArrayList<>(other.dishTypeNeeded);
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void toggleNeedDrink() {
        if(!isNeedDrink()) {
            dishTypeNeeded.add(Dish.TYPE_DRINK);
        } else {
            dishTypeNeeded.remove(Dish.TYPE_DRINK);
        }
    }

    public void toggleNeedAppetizer() {
        if(!isNeedApperizer()) {
            dishTypeNeeded.add(Dish.TYPE_APPETIZER);
        } else {
            dishTypeNeeded.remove(Dish.TYPE_APPETIZER);
        }
    }

    public void toggleNeedContour() {
        if(!isNeedContour()) {
            dishTypeNeeded.add(Dish.TYPE_CONTOUR);
        } else {
            dishTypeNeeded.remove(Dish.TYPE_CONTOUR);
        }
    }

    public void toggleNeedDessert() {
        if(!isNeedDessert()) {
            dishTypeNeeded.add(Dish.TYPE_DESSERT);
        } else {
            dishTypeNeeded.remove(Dish.TYPE_DESSERT);
        }
    }

    public void toggleNeedFirst() {
        if(!isNeedFirst()) {
            dishTypeNeeded.add(Dish.TYPE_FIRST);
        } else {
            dishTypeNeeded.remove(Dish.TYPE_FIRST);
        }
    }


    public void toggleNeedSecond() {
        if(!isNeedSecond()) {
            dishTypeNeeded.add(Dish.TYPE_SECOND);
        } else {
            dishTypeNeeded.remove(Dish.TYPE_SECOND);
        }
    }

    public boolean isNeedDrink() {
        return dishTypeNeeded.contains(Dish.TYPE_DRINK);
    }

    public boolean isNeedApperizer() {
        return dishTypeNeeded.contains(Dish.TYPE_APPETIZER);
    }

    public boolean isNeedContour() {
        return dishTypeNeeded.contains(Dish.TYPE_CONTOUR);
    }
    public boolean isNeedDessert() {
        return dishTypeNeeded.contains(Dish.TYPE_DESSERT);
    }
    public boolean isNeedFirst() {
        return dishTypeNeeded.contains(Dish.TYPE_FIRST);
    }
    public boolean isNeedSecond() {
        return dishTypeNeeded.contains(Dish.TYPE_SECOND);
    }
}
