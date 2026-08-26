package com.pickyeaters.logic.controller;

import com.pickyeaters.logic.bean.EatingPreferenceBean;
import com.pickyeaters.logic.bean.FindRestaurantBean;
import com.pickyeaters.logic.bean.RestaurantBean;
import com.pickyeaters.logic.bean.reply.*;
import com.pickyeaters.logic.bean.request.*;
import com.pickyeaters.logic.dao.IngredientRepository;
import com.pickyeaters.logic.dao.MenuRepository;
import com.pickyeaters.logic.dao.PickieRepository;
import com.pickyeaters.logic.dao.RestaurantRepository;
import com.pickyeaters.logic.exception.*;
import com.pickyeaters.logic.model.*;
import com.pickyeaters.logic.utils.LiteralMessage;
import com.pickyeaters.logic.utils.Logger;

import java.util.*;

public class PickieController {
    private final Logger logger;
    private final PickieRepository pickieRepository;
    private final LoginController loginController;
    private final IngredientRepository ingredientRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public PickieController(Logger logger, LoginController loginController, PickieRepository pickieRepository,
                            IngredientRepository ingredientRepository, RestaurantRepository restaurantRepository,
                            MenuRepository menuRepository) {
        this.logger = logger;
        this.pickieRepository = pickieRepository;
        this.loginController = loginController;
        this.ingredientRepository = ingredientRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    public Result<ShowEatingPreferenceReply> showEatingPreference(ShowEatingPreferenceRequest request) {
        try {
            String userID = loginController.requestUserID(request);
            loginController.checkUserPermission(request, LoginController.PERMISSION_SHOW_EATINGPREFERENCE);
            EatingPreference eatingPreference = pickieRepository.findEatingPreference(userID).orElseThrow();

            List<String> dislikeIngredientList = new ArrayList<>();
            List<String> allergenList = new ArrayList<>();
            List<String> excludedGroupList = new ArrayList<>();

            for(Ingredient i: eatingPreference.getIngredientList()) {
                dislikeIngredientList.add(i.getName());
            }

            for(Allergen i: eatingPreference.getAllergenList()) {
                allergenList.add(i.getName());
            }

            for(ExcludedGroup i: eatingPreference.getGroupList()) {
                excludedGroupList.add(i.getName());
            }

            EatingPreferenceBean bean = new EatingPreferenceBean(dislikeIngredientList,allergenList,excludedGroupList);
            return Result.ok(new ShowEatingPreferenceReply(bean));
        } catch (LoginControllerException | LoginControllerPermissionException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    public Result<Void> editEatingPreference(EditEatingPreferenceRequest request) {
        try {
            String userID = loginController.requestUserID(request);
            loginController.checkUserPermission(request, LoginController.PERMISSION_EDIT_EATINGPREFERENCE);

            List<Ingredient> dislikeIngredientList = new ArrayList<>();
            List<Allergen> allergenList = new ArrayList<>();
            List<ExcludedGroup> excludedGroupList = new ArrayList<>();

            for(String i: request.getEatingPreference().getDislikeIngredientList()) {
                dislikeIngredientList.add(ingredientRepository.findIngredient(i).orElseThrow());
            }

            for(String i: request.getEatingPreference().getAllergenList()) {
                allergenList.add(ingredientRepository.findAllergen(i).orElseThrow());
            }

            for(String i: request.getEatingPreference().getExcludedGroupList()) {
                excludedGroupList.add(ingredientRepository.findExcludedGroupByName(i).orElseThrow());
            }

            EatingPreference eatingPreference = new EatingPreference(dislikeIngredientList, excludedGroupList, allergenList);
            pickieRepository.editEatingPreference(userID, eatingPreference);
            return Result.ok(null);
        } catch (LoginControllerException | LoginControllerPermissionException | GenericRepositoryException e ) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage(), e);
            return Result.error("Ingredient, allergen or excluded group is invalid: " + e.getMessage());
        }
    }

    private void checkDishUserCompatible(Dish dish, FindRestaurantBean bean, EatingPreference eatingPreference) {
        // check if dish respect the user preference
        if(!eatingPreference.checkDish(dish)) {
            return;
        }
        // if dish is "type" and user need "type", then we found a match,
        // so for next restaurant dish, turn off the need of this dish "type"
        if(dish.isTypeAppetizer() && bean.isNeedApperizer()) {
            bean.toggleNeedAppetizer();
        }
        if(dish.isTypeDrink() && bean.isNeedDrink() && eatingPreference.checkDish(dish)) {
            bean.toggleNeedDrink();
        }
        if(dish.isTypeDessert() && bean.isNeedDessert() && eatingPreference.checkDish(dish)) {
            bean.toggleNeedDessert();
        }
        if(dish.isTypeContour() && bean.isNeedContour() && eatingPreference.checkDish(dish)) {
            bean.toggleNeedContour();
        }
        if(dish.isTypeFirst() && bean.isNeedFirst() && eatingPreference.checkDish(dish)) {
            bean.toggleNeedFirst();
        }
        if(dish.isTypeSecond() && bean.isNeedSecond() && eatingPreference.checkDish(dish)) {
            bean.toggleNeedSecond();
        }
    }

    private Map<String, RestaurantBean> getRestaurantUserCompatible(String userID, FindRestaurantBean findRestaurantBean) {
        Map<String, RestaurantBean> outMap = new HashMap<>();
        EatingPreference eatingPreference = pickieRepository.findEatingPreference(userID).orElseThrow();
        List<Restaurant> restaurantList = restaurantRepository.findRestaurantByCity(findRestaurantBean.getCity());

        // foreach restaurant on the system
        for(Restaurant restaurant : restaurantList) {
            // create a copy of original FindRestaurantBean
            FindRestaurantBean bean = new FindRestaurantBean(findRestaurantBean);
            List<Dish> menu = menuRepository.findMenuByRestaurantID(restaurant.getID());
            // if this restaurant has an empty menu, then skip it
            // will not be a useful restaurant for the user
            if(menu.isEmpty()) {
                break;
            }
            // foreach dish in menu
            for(Dish dish : menu) {
                // check if dish is compatible, will turn off some need eventually
                checkDishUserCompatible(dish, bean, eatingPreference);
            }
            // check if all need are off, if so add the restaurant on the
            // output map, else go to next restaurant
            if(!bean.isNeedFirst() && !bean.isNeedSecond() && !bean.isNeedContour() && !bean.isNeedDessert()
                    && !bean.isNeedDrink() && !bean.isNeedApperizer()) {

                outMap.put(restaurant.getID(), new RestaurantBean(restaurant.getName(), restaurant.getAddress(),
                        restaurant.getPhone(), restaurant.getCity()
                ));
            }
        }

        return outMap;
    }

    public Result<FindRestaurantReply> findRestaurant(FindRestaurantRequest request) {
        try {
            String userID = loginController.requestUserID(request);
            loginController.checkUserPermission(request, LoginController.PERMISSION_FIND_RESTAURANT);

            if(request.getFindRestaurant().getCity().isEmpty()) {
                logger.warn(LiteralMessage.PICKIE_CONTROLLER_CITY_EMPTY);
                return Result.error(LiteralMessage.PICKIE_CONTROLLER_CITY_EMPTY);
            }

            return Result.ok(new FindRestaurantReply(getRestaurantUserCompatible(userID, request.getFindRestaurant())));
        } catch (LoginControllerException | LoginControllerPermissionException | GenericRepositoryException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    public Result<AllAllergenReply> allAllergen(AllAllergenRequest request) {
        try {
            loginController.checkUserPermission(request, LoginController.PERMISSION_ALLALLERGEN);
            return Result.ok(new AllAllergenReply(ingredientRepository.allAllergenName()));
        } catch (LoginControllerException | LoginControllerPermissionException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    public Result<AllCityReply> allCity(AllCityRequest request) {
        try {
            loginController.checkUserPermission(request, LoginController.PERMISSION_ALLCITY);
            return Result.ok(new AllCityReply(restaurantRepository.allCity()));
        } catch (LoginControllerException | LoginControllerPermissionException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }


}
