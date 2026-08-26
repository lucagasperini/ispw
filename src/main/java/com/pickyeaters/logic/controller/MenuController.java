package com.pickyeaters.logic.controller;

import com.pickyeaters.logic.bean.*;
import com.pickyeaters.logic.bean.reply.*;
import com.pickyeaters.logic.bean.request.*;
import com.pickyeaters.logic.dao.*;
import com.pickyeaters.logic.exception.*;
import com.pickyeaters.logic.factory.DishFactory;
import com.pickyeaters.logic.model.Allergen;
import com.pickyeaters.logic.model.Dish;
import com.pickyeaters.logic.model.Ingredient;
import com.pickyeaters.logic.model.Restaurant;
import com.pickyeaters.logic.utils.LiteralMessage;
import com.pickyeaters.logic.utils.Logger;

import java.util.*;

public class MenuController {
    private final Logger logger;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final IngredientRepository ingredientRepository;
    private final LoginController loginController;
    private DishFactory factory;

    public MenuController(Logger logger, LoginController loginController, MenuRepository menuRepository,
                          RestaurantRepository restaurantRepository, IngredientRepository ingredientRepository,
                          DishFactory dishFactory) {
        this.logger = logger;
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.loginController = loginController;
        this.ingredientRepository = ingredientRepository;
        this.factory = dishFactory;
    }

    private boolean isDishDuplicatedName(Restaurant restaurant, Dish newDish) {
        try {
            // find dish with same name, if not exists, return false
            Dish otherDish = menuRepository.findDishByName(restaurant.getID(), newDish.getName()).orElseThrow();
            // if they have same id, they are same dish then return false,
            // otherwise they are different dish, so return true
            return !otherDish.getID().equals(newDish.getID());
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public Result<Void> changeDish(ChangeDishRequest request) {
        try {
            String userID = loginController.requestUserID(request);
            loginController.checkUserPermission(request, LoginController.PERMISSION_CHANGE_DISH);
            if(request.getDish().getIngredientList().isEmpty()) {
                return Result.error(LiteralMessage.MENU_CONTROLLER_DISH_MUST_HAVE_ONE_INGREDIENT);
            }

            Restaurant restaurant = restaurantRepository.findRestaurantByOwner(userID).orElseThrow();
            List<Ingredient> ingredientList = ingredientRepository.findIngredientList(request.getDish().getIngredientList());

            Dish dish = factory.createDish(
                    request.getID(),
                    request.getDish().getName(),
                    request.getDish().getDescription(),
                    request.getDish().getType(),
                    ingredientList
            );

            if(isDishDuplicatedName(restaurant, dish)) {
                logger.warn(LiteralMessage.MENU_CONTROLLER_DISH_DUPLICATED_NAME);
                return Result.error(LiteralMessage.MENU_CONTROLLER_DISH_DUPLICATED_NAME);
            }

            menuRepository.editDish(dish);
            return Result.ok(null);
        } catch (LoginControllerException | GenericFactoryException | LoginControllerPermissionException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }

    }

    public Result<Void> addDish(AddDishRequest request) {
        try {
            String userID = loginController.requestUserID(request);
            loginController.checkUserPermission(request, LoginController.PERMISSION_ADD_DISH);

            if(request.getDish().getIngredientList().isEmpty()) {
                logger.warn(LiteralMessage.MENU_CONTROLLER_DISH_MUST_HAVE_ONE_INGREDIENT);
                return Result.error(LiteralMessage.MENU_CONTROLLER_DISH_MUST_HAVE_ONE_INGREDIENT);
            }

            Restaurant restaurant = restaurantRepository.findRestaurantByOwner(userID).orElseThrow();
            List<Ingredient> ingredientList = ingredientRepository.findIngredientList(request.getDish().getIngredientList());

            Dish dish = factory.createDish(
                    "",
                    request.getDish().getName(),
                    request.getDish().getDescription(),
                    request.getDish().getType(),
                    ingredientList
            );

            if(isDishDuplicatedName(restaurant, dish)) {
                logger.warn(LiteralMessage.MENU_CONTROLLER_DISH_DUPLICATED_NAME);
                return Result.error(LiteralMessage.MENU_CONTROLLER_DISH_DUPLICATED_NAME);
            }

            menuRepository.addDish(restaurant.getID(), dish);
            return Result.ok(null);
        } catch (LoginControllerException | GenericFactoryException | LoginControllerPermissionException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        } catch (NoSuchElementException e) {
            logger.error(LiteralMessage.MENU_CONTROLLER_CANT_FIND_RESTAURANT_BY_USERID, e);
            return Result.error(LiteralMessage.MENU_CONTROLLER_CANT_FIND_RESTAURANT_BY_USERID);
        }
    }

    public Result<ShowDishReply> showDish(ShowDishRequest request) {
        try {
            loginController.checkUserPermission(request, LoginController.PERMISSION_SHOW_DISH);
            Dish dish = menuRepository.findDishByID(request.getID()).orElseThrow();

            List<DishIngredientBean> ingredientList = new ArrayList<>();
            Map<String, DishAllergenBean> allergenMap = new HashMap<>();
            for(Ingredient i : dish.getIngredientList()) {
                ingredientList.add(new DishIngredientBean(i.getName(),i.isOptional(), i.isCooked()));
                for(Allergen a : i.getAllergenList()) {
                    if(!allergenMap.containsKey(a.getName())) {
                        allergenMap.put(a.getName(), new DishAllergenBean(a.getName()));
                    }
                }
            }

            DishBean dishBean = new DishBean(dish.getName(), dish.getDescription(), dish.getType(), ingredientList);
            return Result.ok(new ShowDishReply(dishBean, List.copyOf(allergenMap.values())));
        } catch (NoSuchElementException e) {
            logger.error(LiteralMessage.MENU_CONTROLLER_CANT_FIND_RESTAURANT_BY_USERID, e);
            return Result.error(LiteralMessage.MENU_CONTROLLER_CANT_FIND_RESTAURANT_BY_USERID);
        }  catch (LoginControllerException | GenericRepositoryException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    public Result<Void> removeDish(RemoveDishRequest request) {
        try {
            loginController.checkUserPermission(request, LoginController.PERMISSION_REMOVE_DISH);
            menuRepository.removeDish(request.getID());
            return Result.ok(null);
        } catch (LoginControllerException | GenericRepositoryException | LoginControllerPermissionException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }


    }

    public Result<ShowMenuReply> showMenu(ShowMenuRequest request) {
        try {
            loginController.checkUserPermission(request, LoginController.PERMISSION_SHOW_MENU);

            List<Dish> menu = menuRepository.findMenuByRestaurantID(request.getRestaurantID());
            Map<String, DishBean> dishMap = new HashMap<>();
            for (Dish d : menu) {
                dishMap.put(d.getID(), new DishBean(d));
            }

            return Result.ok(new ShowMenuReply(dishMap));
        } catch (LoginControllerException | LoginControllerPermissionException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        } catch (NoSuchElementException e) {
            logger.error(LiteralMessage.MENU_CONTROLLER_CANT_MENU_BY_RESTAURANTID, e);
            return Result.error(LiteralMessage.MENU_CONTROLLER_CANT_MENU_BY_RESTAURANTID);
        }
    }

    public Result<AllIngredientReply> allIngredient(AllIngredientRequest request) {
        try {
            loginController.checkUserPermission(request, LoginController.PERMISSION_ALLINGREDIENT);
            return Result.ok(new AllIngredientReply(ingredientRepository.allIngredientName()));
        } catch (LoginControllerException | LoginControllerPermissionException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        } catch (NoSuchElementException e) {
            logger.error(LiteralMessage.MENU_CONTROLLER_CANT_MENU_BY_RESTAURANTID, e);
            return Result.error(LiteralMessage.MENU_CONTROLLER_CANT_MENU_BY_RESTAURANTID);
        }
    }
    public Result<ShowAllergenIngredientReply> showAllergenIngredient(ShowAllergenIngredientRequest request) {
        try {
            loginController.checkUserPermission(request, LoginController.PERMISSION_SHOW_ALLERGENINGREDIENT);
            Ingredient ingredient = ingredientRepository.findIngredient(request.getIngredientName()).orElseThrow();
            List<Allergen> allergenList = ingredientRepository.findAllergenByIngredient(ingredient);
            List<DishAllergenBean> outList = new ArrayList<>();
            for(Allergen i : allergenList) {
                outList.add(new DishAllergenBean(i));
            }

            return Result.ok(new ShowAllergenIngredientReply(outList));
        } catch (LoginControllerException | LoginControllerPermissionException e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage(), e);
            return Result.error(LiteralMessage.MENU_CONTROLLER_CANT_FIND_INGREDIENT);
        }
    }
}
