package com.pickyeaters.logic.utils;

public class LiteralMessage {
    private LiteralMessage() {}

    public static final String LOGIN_CONTROLLER_NO_SUCH_USER = "No such user or password";
    public static final String RESTAURANT_CONTROLLER_NO_RESTAURANT = "No restaurant found!";
    public static final String MENU_CONTROLLER_CANT_FIND_RESTAURANT_BY_USERID = "Can't find restaurant for requested userID";
    public static final String MENU_CONTROLLER_CANT_FIND_INGREDIENT = "Can't find ingredient";

    public static final String MENU_CONTROLLER_CANT_MENU_BY_RESTAURANTID = "Cannot find menu for restaurantID";
    public static final String MENU_CONTROLLER_DISH_MUST_HAVE_ONE_INGREDIENT = "The dish must contain at least one ingredient";
    public static final String USER_CONTROLLER_CANT_FIND_USER_BY_USERID = "Can't find user for requested userID";

    public static final String MENU_REPOSITORY_CANT_REMOVE_DISH = "Cannot remove selected dish from restaurant";

    public static final String FXML_ERROR = "FXML ERROR";
}
