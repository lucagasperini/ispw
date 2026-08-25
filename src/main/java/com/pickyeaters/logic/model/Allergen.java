package com.pickyeaters.logic.model;

public class Allergen {
    private final String id;
    private final String name;

    public Allergen(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }
}
