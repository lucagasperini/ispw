package com.pickyeaters.logic.bean;

public class RestaurantBean {
    private String name;
    private String address;
    private String phone;
    private String city;

    public RestaurantBean(String name, String address, String phone, String city) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.city = city;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
