package com.pickyeaters.logic.model;

import java.util.ArrayList;
import java.util.List;

public class Ingredient {
    private String id;
    private String name;
    private boolean cooked;
    private boolean optional;

    private List<Allergen> allergenList;

    public Ingredient(String id, String name, List<Allergen> allergenList, boolean cooked, boolean optional) {
        setID(id);
        setName(name);
        this.allergenList = List.copyOf(allergenList);
        setCooked(cooked);
        setOptional(optional);
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return id;
    }

    public List<Allergen> getAllergenList() {
        return allergenList;
    }

    public boolean isCooked() {
        return cooked;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setCooked(boolean cooked) {
        this.cooked = cooked;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

}
