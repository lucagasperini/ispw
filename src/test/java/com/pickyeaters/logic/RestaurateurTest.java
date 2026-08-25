package com.pickyeaters.logic;

import com.pickyeaters.logic.view.Application;
import com.pickyeaters.logic.view.LoginView;
import com.pickyeaters.logic.view.MenuView;
import com.pickyeaters.logic.view.dish.AddDishView;
import com.pickyeaters.logic.view.dish.ChangeDishView;
import com.pickyeaters.logic.view.dish.ShowDishView;
import com.pickyeaters.logic.view.restaurant.EditRestaurantView;
import com.pickyeaters.logic.view.restaurant.ShowRestaurantView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RestaurateurTest {
    private Application app;
    @BeforeEach
    void setUp() {
        app = new Application(new String[]{""});
        app.systemStart();
        LoginView loginView = app.displayLoginView();
        loginView.insertEmail("lucaR");
        loginView.insertPassword("luca");
        app.login();
    }

    @Test
    void showRestaurant() {
        ShowRestaurantView view = app.displayShowRestaurantView();

        Assertions.assertEquals("Test Ristorante", view.showName());
        Assertions.assertEquals("+32 342", view.showPhone());
        Assertions.assertEquals("Via aaa", view.showAddress());
    }

    @Test
    void editRestaurant() {
        EditRestaurantView editView = app.displayEditRestaurantView();

        editView.insertName("New test name");
        editView.insertPhone("+33 3333");
        editView.insertAddress("Via di bbb");
        editView.insertCity("Milano");
        editView.submit();

        ShowRestaurantView showView = app.displayShowRestaurantView();

        Assertions.assertEquals("New test name", showView.showName());
        Assertions.assertEquals("+33 3333", showView.showPhone());
        Assertions.assertEquals("Via di bbb", showView.showAddress());
        Assertions.assertEquals("Milano", showView.showCity());
    }


    @Test
    void showMenu() {
        MenuView menuView = app.displayMenuView();

        List<String> name = new ArrayList<>(3);
        List<String> description = new ArrayList<>(3);
        List<String> type = new ArrayList<>(3);
        List<String> ingredientList = new ArrayList<>(5);

        name.add(menuView.showName("1"));
        description.add(menuView.showDescription("1"));
        type.add(menuView.showType("1"));
        ingredientList.addAll(menuView.showIngredientList("1"));

        name.add(menuView.showName("2"));
        description.add(menuView.showDescription("2"));
        type.add(menuView.showType("2"));
        ingredientList.addAll(menuView.showIngredientList("2"));

        name.add(menuView.showName("3"));
        description.add(menuView.showDescription("3"));
        type.add(menuView.showType("3"));
        ingredientList.addAll(menuView.showIngredientList("3"));

        Assertions.assertTrue(name.remove("R1 Dish name 1"));
        Assertions.assertTrue(description.remove("R1 Description 1"));
        Assertions.assertTrue(type.remove("First"));
        Assertions.assertTrue(ingredientList.remove("Ingredient 1"));
        Assertions.assertTrue(ingredientList.remove("Ingredient 2"));

        Assertions.assertTrue(name.remove("R1 Dish name 2"));
        Assertions.assertTrue(description.remove("R1 Description 2"));
        Assertions.assertTrue(type.remove("Second"));
        Assertions.assertTrue(ingredientList.remove("Ingredient 1"));
        Assertions.assertTrue(ingredientList.remove("Ingredient 3"));

        Assertions.assertTrue(name.remove("R1 Dish name 3"));
        Assertions.assertTrue(description.remove("R1 Description 3"));
        Assertions.assertTrue(type.remove("Drink"));
        Assertions.assertTrue(ingredientList.remove("Ingredient 2"));


        Assertions.assertTrue(name.isEmpty());
        Assertions.assertTrue(description.isEmpty());
        Assertions.assertTrue(type.isEmpty());
        Assertions.assertTrue(ingredientList.isEmpty());
    }

    @Test
    void addDish() {
        AddDishView addView = app.displayAddDishView();
        addView.insertName("Test name dish");
        addView.insertDescription("Test description dish");
        addView.selectType("DESSERT");
        addView.addIngredient("Ingredient 4");
        addView.addIngredient("Ingredient 5");

        addView.submit();

        MenuView showView = app.displayMenuView();
        List<String> idList = showView.showDishID();
        int indexTest = 0;
        for(int i = 1; i <= idList.size(); i++) {
            if(showView.showName(idList.get(i - 1)).equals("Test name dish")) {
                indexTest = i - 1;
                break;
            }
        }
        
        Assertions.assertEquals("Test name dish", showView.showName(idList.get(indexTest)));
        Assertions.assertEquals("Test description dish", showView.showDescription(idList.get(indexTest)));
        Assertions.assertEquals("Dessert", showView.showType(idList.get(indexTest)));
        List<String> ingredientList = showView.showIngredientList(idList.get(indexTest));
        Assertions.assertTrue(ingredientList.contains("Ingredient 4"));
        Assertions.assertTrue(ingredientList.contains("Ingredient 5"));
    }

    @Test
    void showDish() {
        MenuView showView = app.displayMenuView();
        List<String> idList = showView.showDishID();
        int indexTest = 0;
        for(int i = 1; i <= idList.size(); i++) {
            if(showView.showName(idList.get(i - 1)).equals("R1 Dish name 1")) {
                indexTest = i - 1;
                break;
            }
        }

        ShowDishView showDishView = app.displayShowDishView(idList.get(indexTest));

        Assertions.assertEquals("R1 Dish name 1", showDishView.showName());
        Assertions.assertEquals("R1 Description 1", showDishView.showDescription());
        Assertions.assertEquals("First", showDishView.showType());
        List<String> ingredientList = showDishView.showIngredientNameList();
        Assertions.assertTrue(ingredientList.contains("Ingredient 1"));
        Assertions.assertTrue(ingredientList.contains("Ingredient 2"));

        List<String> allergenList = showDishView.showAllergenList();
        Assertions.assertTrue(allergenList.contains("Allergen 1"));
        Assertions.assertTrue(allergenList.contains("Allergen 2"));
        Assertions.assertTrue(allergenList.contains("Allergen 3"));
        Assertions.assertTrue(allergenList.contains("Allergen 4"));
    }

    @Test
    void changeDish() {
        MenuView showView = app.displayMenuView();
        List<String> idList = showView.showDishID();
        int indexTest = 0;
        for(int i = 1; i <= idList.size(); i++) {
            if(showView.showName(idList.get(i - 1)).equals("R1 Dish name 1")) {
                indexTest = i - 1;
                break;
            }
        }

        ChangeDishView changeDishView = app.displayChangeDishView(idList.get(indexTest));

        Assertions.assertEquals("R1 Dish name 1", changeDishView.showName());
        Assertions.assertEquals("R1 Description 1", changeDishView.showDescription());
        Assertions.assertEquals("First", changeDishView.showType());
        List<String> ingredientList = changeDishView.showIngredientNameList();
        Assertions.assertTrue(ingredientList.contains("Ingredient 1"));
        Assertions.assertTrue(ingredientList.contains("Ingredient 2"));

        List<String> allergenList = changeDishView.showAllergenList();
        Assertions.assertTrue(allergenList.contains("Allergen 1"));
        Assertions.assertTrue(allergenList.contains("Allergen 2"));
        Assertions.assertTrue(allergenList.contains("Allergen 3"));
        Assertions.assertTrue(allergenList.contains("Allergen 4"));

        changeDishView.insertName("Test change name");
        changeDishView.insertDescription("Test change description");
        changeDishView.selectType("DESSERT");
        changeDishView.submit();

        showView = app.displayMenuView();
        idList = showView.showDishID();
        for(int i = 1; i <= idList.size(); i++) {
            if(showView.showName(idList.get(i - 1)).equals("Test change name")) {
                indexTest = i - 1;
                break;
            }
        }

        changeDishView = app.displayChangeDishView(idList.get(indexTest));

        Assertions.assertEquals("Test change name", changeDishView.showName());
        Assertions.assertEquals("Test change description", changeDishView.showDescription());
        Assertions.assertEquals("Dessert", changeDishView.showType());
        ingredientList = changeDishView.showIngredientNameList();
        Assertions.assertTrue(ingredientList.contains("Ingredient 1"));
        Assertions.assertTrue(ingredientList.contains("Ingredient 2"));

        allergenList = changeDishView.showAllergenList();
        Assertions.assertTrue(allergenList.contains("Allergen 1"));
        Assertions.assertTrue(allergenList.contains("Allergen 2"));
        Assertions.assertTrue(allergenList.contains("Allergen 3"));
        Assertions.assertTrue(allergenList.contains("Allergen 4"));
    }
}
