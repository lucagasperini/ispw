package com.pickyeaters.logic.model;

public class Restaurateur extends User {
    private final Restaurant restaurant;

    public Restaurateur(
            String id,
            String email,
            String password,
            String firstname,
            String lastname) {
        super(id, email, password, firstname, lastname);
        this.restaurant = null;
    }

    public Restaurateur(
            String id,
            String email,
            String password,
            String firstname,
            String lastname,
            Restaurant restaurant) {
        super(id, email, password, firstname, lastname);
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

}
