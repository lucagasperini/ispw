package com.pickyeaters.logic.model;

import java.util.List;

public class Restaurant {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String city;
    private Restaurateur restaurateur;

    private List<Dish> menu;

    public Restaurant(String id, String name, String phone, String address, String city, Restaurateur restaurateur, List<Dish> menu) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.restaurateur = restaurateur;
    }

    public Restaurant(String id, String name, String phone, String address, String city, Restaurateur restaurateur) {
        this(id, name, phone, address,city, restaurateur, null);
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public Restaurateur getRestaurateur() {
        return restaurateur;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
