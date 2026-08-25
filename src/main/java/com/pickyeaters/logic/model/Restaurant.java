package com.pickyeaters.logic.model;

public class Restaurant {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String city;

    public Restaurant(String id, String name, String phone, String address, String city) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
    }

    public Restaurant(String name, String phone, String address, String city) {
        this("", name, phone, address, city);
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

    public void setID(String id) {
        this.id = id;
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
