package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.model.Pickie;
import com.pickyeaters.logic.model.Restaurant;
import com.pickyeaters.logic.model.Restaurateur;
import com.pickyeaters.logic.model.User;
import com.pickyeaters.logic.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantRepositoryRAM implements RestaurantRepository {
    private final Logger logger;
    private final List<Restaurateur> restaurateurList = new ArrayList<>();
    private final List<Restaurant> restaurantList = new ArrayList<>();

    public RestaurantRepositoryRAM(Logger logger) {
        this.logger = logger;
        Restaurant r1 = new Restaurant("1","Test Ristorante", "+32 342", "Via aaa", "Roma");
        Restaurant r2 = new Restaurant("2","Test Ristorante 2", "+43 342", "Via bbb", "Roma");
        Restaurant r3 = new Restaurant("3","Test Ristorante 3", "+45 342", "Via ccc", "Torino");

        restaurantList.add(r1);
        restaurantList.add(r2);
        restaurantList.add(r3);

        Restaurateur u1 = new Restaurateur("1", "lucaR", "luca", "Luca", "Bianchi", r1);
        Restaurateur u2 = new Restaurateur("4", "testR", "test", "Marco", "Rossi", r3);
        Restaurateur u3 = new Restaurateur("5", "test", "test", "Giuseppe", "Verdi", r2);

        restaurateurList.add(u1);
        restaurateurList.add(u2);
        restaurateurList.add(u3);

    }

    public Optional<Restaurant> findRestaurantByOwner(String restaurateurID){
        for(Restaurateur r: restaurateurList) {
            if(r.getID().equals(restaurateurID)) {
                return Optional.ofNullable(r.getRestaurant());
            }
        }
        return Optional.empty();
    }

    public void editRestaurantByOwner(String restaurateurID, Restaurant restaurant) {
        for(Restaurateur r: restaurateurList) {
            if(r.getID().equals(restaurateurID)) {
                Restaurant old = r.getRestaurant();
                old.setName(restaurant.getName());
                old.setAddress(restaurant.getAddress());
                old.setPhone(restaurant.getPhone());
                old.setCity(restaurant.getCity());
                return;
            }
        }
        throw new GenericRepositoryException("Cannot find selected restaurant");
    }

    public List<Restaurant> findRestaurantByCity(String city) {
        List<Restaurant> outList = new ArrayList<>();
        for(Restaurant r: restaurantList) {
            if(r.getCity().equals(city)) {
                outList.add(r);
            }
        }
        return outList;
    }

    public List<String> allCity() {
        List<String> cityList = new ArrayList<>();
        for(Restaurant r: restaurantList) {
            if(!cityList.contains(r.getCity())) {
                cityList.add(r.getCity());
            }
        }
        return cityList;
    }
}
