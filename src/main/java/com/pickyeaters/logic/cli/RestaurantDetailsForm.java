package com.pickyeaters.logic.cli;

import com.pickyeaters.logic.view.Application;
import com.pickyeaters.logic.view.restaurant.EditRestaurantView;
import com.pickyeaters.logic.view.restaurant.ShowRestaurantView;

import java.util.Map;

public class RestaurantDetailsForm extends VirtualForm {
    public RestaurantDetailsForm(Application app) {
        super(app,"RestaurantDetails");
    }

    @Override
    public void show(Map<String, String> arg) {
        requestLoop();
    }

    @Override
    protected boolean request(String request) {
        switch (request) {
            case "show", "s":
                showDetails();
                return true;
            case "edit", "e":
                editDetails();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String requestHelp() {
        return """
                [show, s]
                [edit, e]""";
    }

    private void showDetails() {
        ShowRestaurantView view = app.displayShowRestaurantView();
        printField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_NAME", view.showName());
        printField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_ADDRESS", view.showAddress());
        printField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_PHONE", view.showPhone());
        printField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_CITY", view.showCity());
    }

    private void editDetails() {
        EditRestaurantView view = app.displayEditRestaurantView();
        view.insertName(askField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_NAME"));
        view.insertAddress(askField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_ADDRESS"));
        view.insertPhone(askField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_PHONE"));
        view.insertCity(askField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_CITY"));
        view.submit();
    }
}
