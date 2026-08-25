package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.model.Restaurant;
import com.pickyeaters.logic.model.Restaurateur;
import com.pickyeaters.logic.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class RestaurantRepositoryRAM implements RestaurantRepository {
    private final Logger logger;
    private final List<Restaurant> restaurantList = new ArrayList<>();

    public RestaurantRepositoryRAM(Logger logger, UserRepository userRepository) {
        this.logger = logger;

        Restaurant r1 = new Restaurant(
                "1",
                "Test Ristorante",
                "+32 342",
                "Via aaa",
                "Roma",
                (Restaurateur) userRepository.getUserByEmail("lucaR").orElseThrow()
        );

        Restaurant r2 = new Restaurant(
                "2",
                "Test Ristorante 2",
                "+43 342",
                "Via bbb",
                "Roma",
                (Restaurateur) userRepository.getUserByEmail("testR").orElseThrow()
        );

        Restaurant r3 = new Restaurant(
                "3",
                "Test Ristorante 3",
                "+45 342",
                "Via ccc",
                "Torino",
                (Restaurateur) userRepository.getUserByEmail("test").orElseThrow()
        );

        restaurantList.add(r1);
        restaurantList.add(r2);
        restaurantList.add(r3);
    }

    public Optional<Restaurant> findRestaurantByOwner(String restaurateurID){
        for(Restaurant r: restaurantList) {
            if(r.getRestaurateur().getID().equals(restaurateurID)) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public void editRestaurantByOwner(String restaurateurID, Restaurant restaurant) {
        try {
            Restaurant old = findRestaurantByOwner(restaurateurID).orElseThrow();
            old.setName(restaurant.getName());
            old.setAddress(restaurant.getAddress());
            old.setPhone(restaurant.getPhone());
            old.setCity(restaurant.getCity());

        } catch (NoSuchElementException e) {
            GenericRepositoryException ex = new GenericRepositoryException("Cannot find selected restaurant");
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
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
