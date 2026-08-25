package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.model.Dish;
import com.pickyeaters.logic.view.ViewObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public abstract class DishForm implements ViewObserver {
    @FXML
    protected Button buttonAddIngredient;

    @FXML
    protected Text textName;
    @FXML
    protected Text textIngredients;
    @FXML
    protected Text textDescription;
    @FXML
    protected Text textCategory;
    @FXML
    protected Button buttonSave;
    @FXML
    protected Text textAllergens;
    @FXML
    protected TextField inputName;
    @FXML
    protected TextArea inputDescription;
    @FXML
    protected ComboBox<String> comboBoxCategory;
    @FXML
    protected String comboBoxItemDrink;
    @FXML
    protected String comboBoxItemFirst;
    @FXML
    protected String comboBoxItemAppetizer;
    @FXML
    protected String comboBoxItemSecond;
    @FXML
    protected String comboBoxItemContour;
    @FXML
    protected String comboBoxItemDessert;

    @FXML
    protected ListView<String> listViewAllergen;
    @FXML
    protected VBox vboxIngredient;

    @FXML
    public void initialize() {
        Navigator.getMainForm().showBackButton();

        comboBoxItemDrink = AppData.getInstance().i18n("DISH_TYPE_DRINK");
        comboBoxItemFirst = AppData.getInstance().i18n("DISH_TYPE_FIRST");
        comboBoxItemAppetizer = AppData.getInstance().i18n("DISH_TYPE_APPETIZER");
        comboBoxItemSecond = AppData.getInstance().i18n("DISH_TYPE_SECOND");
        comboBoxItemContour = AppData.getInstance().i18n("DISH_TYPE_CONTOUR");
        comboBoxItemDessert = AppData.getInstance().i18n("DISH_TYPE_DESSERT");

        textCategory.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDDISH_CATEGORY"));
        textDescription.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDDISH_DESCRIPTION"));
        textAllergens.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDDISH_ALLERGENS"));
        textIngredients.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDDISH_INGREDIENTS"));
        textName.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDDISH_NAME"));
        buttonAddIngredient.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDDISH_ADDINGREDIENT"));

        comboBoxCategory.getItems().addAll(
                comboBoxItemDrink,
                comboBoxItemAppetizer,
                comboBoxItemFirst,
                comboBoxItemContour,
                comboBoxItemSecond,
                comboBoxItemDessert
        );
    }


    protected String getCurrentComboBoxItem() {
        String cur = comboBoxCategory.getValue();
        if(cur == null) {
            return "";
        }

        if(cur.equals(comboBoxItemDrink))
            return Dish.TYPE_DRINK;
        else if(cur.equals(comboBoxItemAppetizer))
            return Dish.TYPE_APPETIZER;
        else if(cur.equals(comboBoxItemFirst))
            return Dish.TYPE_FIRST;
        else if(cur.equals(comboBoxItemContour))
            return Dish.TYPE_CONTOUR;
        else if(cur.equals(comboBoxItemSecond))
            return Dish.TYPE_SECOND;
        else if(cur.equals(comboBoxItemDessert))
            return Dish.TYPE_DESSERT;
        else
            return "";
    }

    @FXML
    protected abstract void clickAddIngredient(ActionEvent event);

    @FXML
    protected abstract void clickSaveChanges(ActionEvent event);
}
