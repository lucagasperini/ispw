package com.pickyeaters.logic;

import com.pickyeaters.logic.view.Application;
import com.pickyeaters.logic.view.FindRestaurantView;
import com.pickyeaters.logic.view.LoginView;
import com.pickyeaters.logic.view.eatingpreference.EditEatingPreferenceView;
import com.pickyeaters.logic.view.eatingpreference.ShowEatingPreferenceView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class PickieTest {
    private Application app;
    @BeforeEach
    void setUp() {
        app = new Application(new String[]{""});
        app.systemStart();
        LoginView loginView = app.displayLoginView();
        loginView.insertEmail("lucaP");
        loginView.insertPassword("luca");
        app.login();
    }

    @Test
    void showEatingPreference() {
        ShowEatingPreferenceView view = app.displayShowEatingPreferenceView();

        List<String> excludedIngredientList = view.showExcludedIngredientList();
        List<String> allergenList = view.showAllergenList();
        List<String> excludedGroupList = view.showExcludedGroupList();

        Assertions.assertTrue(excludedIngredientList.contains("Ingredient 2"));
        Assertions.assertTrue(excludedIngredientList.contains("Ingredient 4"));

        Assertions.assertTrue(allergenList.contains("Allergen 1"));
        Assertions.assertTrue(allergenList.contains("Allergen 2"));

        Assertions.assertTrue(excludedGroupList.contains("HALAL"));
    }

    @Test
    void editEatingPreference() {
        EditEatingPreferenceView editView = app.displayEditEatingPreferenceView();

        editView.removeDislikeIngredient("Ingredient 2");
        editView.addDislikeIngredient("Ingredient 1");

        editView.removeAllergen("Allergen 2");
        editView.addAllergen("Allergen 4");

        editView.removeExcludedGroup("HALAL");
        editView.addExcludedGroup("PREGNANT");

        editView.submit();

        ShowEatingPreferenceView view = app.displayShowEatingPreferenceView();
        List<String> excludedIngredientList = view.showExcludedIngredientList();
        List<String> allergenList = view.showAllergenList();
        List<String> excludedGroupList = view.showExcludedGroupList();

        Assertions.assertTrue(excludedIngredientList.contains("Ingredient 1"));
        Assertions.assertTrue(excludedIngredientList.contains("Ingredient 4"));

        Assertions.assertTrue(allergenList.contains("Allergen 1"));
        Assertions.assertTrue(allergenList.contains("Allergen 4"));

        Assertions.assertTrue(excludedGroupList.contains("PREGNANT"));
    }

    @Test
    void findRestaurant() {
        FindRestaurantView view = app.displayFindRestaurantView();

        view.toggleNeedDrink();
        view.insertCity("Roma");
        view.startSearch();
        List<String> idList = view.showRestaurantID();

        Assertions.assertEquals("Test Ristorante 2", view.showRestaurantName(idList.get(0)));
        Assertions.assertEquals("Via bbb", view.showRestaurantAddress(idList.get(0)));
        Assertions.assertEquals("+43 342", view.showRestaurantPhone(idList.get(0)));
        Assertions.assertEquals("Roma", view.showRestaurantCity(idList.get(0)));
    }
}
