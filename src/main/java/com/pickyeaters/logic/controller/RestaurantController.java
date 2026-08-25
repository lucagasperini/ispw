package com.pickyeaters.logic.controller;

import com.pickyeaters.logic.bean.RestaurantBean;
import com.pickyeaters.logic.bean.reply.EditRestaurantReply;
import com.pickyeaters.logic.bean.reply.Result;
import com.pickyeaters.logic.bean.reply.ShowRestaurantReply;
import com.pickyeaters.logic.bean.request.EditRestaurantRequest;
import com.pickyeaters.logic.bean.request.ShowRestaurantRequest;
import com.pickyeaters.logic.dao.RestaurantRepository;
import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.model.Restaurant;
import com.pickyeaters.logic.utils.LiteralMessage;
import com.pickyeaters.logic.utils.Logger;

import java.util.Optional;

public class RestaurantController {

    private final Logger logger;
    private final RestaurantRepository repository;
    private final LoginController loginController;

    public RestaurantController(Logger logger, LoginController loginController, RestaurantRepository restaurantRepository) {
        this.logger = logger;
        this.repository = restaurantRepository;
        this.loginController = loginController;
    }

    public Result<ShowRestaurantReply> showRestaurant(ShowRestaurantRequest request) {
        // if restaurantID is not provided, use token to fetch restaurant
        if(request.getRestaurantID().isEmpty()) {
            String userID = loginController.requestUserID(request);
            Optional<Restaurant> restaurant = repository.findRestaurantByOwner(userID);
            if(restaurant.isEmpty()) {
                logger.warn(LiteralMessage.RESTAURANT_CONTROLLER_NO_RESTAURANT);
                return Result.error(LiteralMessage.RESTAURANT_CONTROLLER_NO_RESTAURANT);
            } else {
                return Result.ok(buildReplyShowRestaurant(restaurant.orElseThrow()));
            }
        }
        NotImplementedException e = new NotImplementedException();
        logger.error(e.getMessage(), e);
        throw e;
    }

    private ShowRestaurantReply buildReplyShowRestaurant(Restaurant restaurant) {
        RestaurantBean restaurantBean = new RestaurantBean(
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getCity()
        );
        return new ShowRestaurantReply(restaurant.getID(), restaurantBean);
    }

    public Result<EditRestaurantReply> editRestaurant(EditRestaurantRequest request) {
        try {
            String userID = loginController.requestUserID(request);
            Restaurant restaurant = new Restaurant(
                    request.getRestaurant().getName(),
                    request.getRestaurant().getPhone(),
                    request.getRestaurant().getAddress(),
                    request.getRestaurant().getCity()
            );
            repository.editRestaurantByOwner(userID, restaurant);
            return Result.ok(new EditRestaurantReply());
        } catch (GenericRepositoryException e) {
            logger.error(LiteralMessage.RESTAURANT_CONTROLLER_NO_RESTAURANT, e);
            return Result.error(LiteralMessage.RESTAURANT_CONTROLLER_NO_RESTAURANT);
        }
    }
}
