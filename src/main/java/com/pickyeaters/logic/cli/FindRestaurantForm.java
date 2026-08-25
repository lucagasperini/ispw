package com.pickyeaters.logic.cli;

import com.pickyeaters.logic.view.Application;
import com.pickyeaters.logic.view.FindRestaurantView;
import com.pickyeaters.logic.view.MenuView;
import com.pickyeaters.logic.view.restaurant.ShowRestaurantView;

import java.util.List;
import java.util.Map;

public class FindRestaurantForm extends VirtualForm {
    private final FindRestaurantView view;
    public FindRestaurantForm(Application app) {
        super(app,"FindRestaurant");
        view = app.displayFindRestaurantView();

        view.toggleNeedDrink();
        view.toggleNeedDessert();
        view.toggleNeedAppetizer();
        view.toggleNeedFirst();
        view.toggleNeedSecond();
        view.toggleNeedContour();
    }

    @Override
    public void show(Map<String, String> arg) {
        requestLoop();
    }

    public boolean request(String request) {
        switch (request) {
            case "search", "s":
                search();
                return true;
            case "drink", "dk":
                view.toggleNeedDrink();
                return true;
            case "dessert", "de":
                view.toggleNeedDessert();
                return true;
            case "apperizer", "da":
                view.toggleNeedAppetizer();
                return true;
            case "first", "df":
                view.toggleNeedFirst();
                return true;
            case "second", "ds":
                view.toggleNeedSecond();
                return true;
            case "countour", "dc":
                view.toggleNeedContour();
                return true;
            case "restaurant", "r":
                showRestaurant();
                return true;
            case "menu", "m":
                showMenu();
                return true;
            case "clear", "c":
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String requestHelp() {
        return """
            [search, s]
            [restaurant, r]
            [menu, m]
            [apperizer, da]
            [drink, dk]
            [first, df]
            [countour, dc]
            [second, ds]
            [dessert, de]
            [clear, c]""";
    }

    private void search() {
        view.insertCity(askField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_CITY"));
        view.startSearch();
        List<String> idList = view.showRestaurantID();
        for(int i = 1; i <= idList.size(); i++) {
            print(i + ") " + view.showRestaurantName(idList.get(i)));
        }
    }


    private void showRestaurant() {
        List<String> idList = view.showRestaurantID();
        for(int i = 1; i <= idList.size(); i++) {
            print(i + ") " + view.showRestaurantName(idList.get(i - 1)));
        }
        int vid = askFieldInteger("ID") - 1;

        printField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_NAME", view.showRestaurantName(idList.get(vid)));
        printField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_ADDRESS", view.showRestaurantAddress(idList.get(vid)));
        printField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_PHONE", view.showRestaurantPhone(idList.get(vid)));
        printField("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_CITY", view.showRestaurantCity(idList.get(vid)));
    }

    private void showMenu() {
        List<String> idList = view.showRestaurantID();
        for(int i = 1; i <= idList.size(); i++) {
            print(i + ") " + view.showRestaurantName(idList.get(i - 1)));
        }
        int vid = askFieldInteger("ID") - 1;

        MenuView menuView = app.displayMenuView(idList.get(vid));
        for(String i : menuView.showDishID()) {
            printField("RESTAURATEUR_MANAGEMENUDETAILS_NAME", menuView.showName(i));
            printField("RESTAURATEUR_MANAGEMENUDETAILS_DESCRIPTION",menuView.showDescription(i));
            printField("RESTAURATEUR_MANAGEMENUDETAILS_CATEGORY",menuView.showType(i));
            for(String str : menuView.showIngredientList(i)) {
                printField("RESTAURATEUR_MANAGEMENUDETAILS_INGREDIENTS", str);
            }
            print("##################################");
        }
    }
}
