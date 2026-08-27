package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.view.dish.AddDishView;
import com.pickyeaters.logic.view.dish.ChangeDishView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.List;

public class AddIngredientForm {
    @FXML
    private Text textIngredient;
    @FXML
    private Text textAllergens;
    @FXML
    private Text textCookingMethod;
    @FXML
    private Text textOptionality;
    @FXML
    private Text textIngredientSelect;
    @FXML
    private Text textIngredientSelectExplanation;
    @FXML
    private Text textAllergensExplanation;
    @FXML
    private Text textAllergensSelectExplanation;

    @FXML
    private Button buttonSave;
    @FXML
    private CheckBox checkBoxOptional;
    @FXML
    private TreeView<String> treeIngredient;
    @FXML
    private CheckBox checkBoxCooked;
    @FXML
    private ListView<String> listViewAllergen;


    private final ChangeDishView changeDishView;
    private final AddDishView addDishView;

    public AddIngredientForm(ChangeDishView changeDishView) {
        this.changeDishView = changeDishView;
        this.addDishView = null;
    }

    public AddIngredientForm(AddDishView addDishView) {
        this.addDishView = addDishView;
        this.changeDishView = null;
    }

    @FXML
    public void initialize() {
        Navigator.getMainForm().setTitle("RESTAURATEUR_ADDINGREDIENT");

        List<String> allIngredient;
        if(changeDishView != null) {
            Navigator.getMainForm().setActionBackButton(
                    () ->
                Navigator.navigateContentParent(new ChangeDishForm(changeDishView))
            );

            treeIngredient.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        listViewAllergen.getItems().clear();
                        for(String  i : changeDishView.showAllergenIngredient(newValue.getValue())) {
                            listViewAllergen.getItems().add(i);
                        }
                    }
            );

            allIngredient = changeDishView.showAllIngredient();
        } else {
            Navigator.getMainForm().setActionBackButton(
                    () -> Navigator.navigateContentParent(new AddDishForm(addDishView))
            );

            treeIngredient.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        listViewAllergen.getItems().clear();
                        for(String  i : addDishView.showAllergenIngredient(newValue.getValue())) {
                            listViewAllergen.getItems().add(i);
                        }
                    }
            );

            allIngredient = addDishView.showAllIngredient();
        }

        TreeItem<String> treeIngredientRoot = new TreeItem<>(AppData.getInstance().i18n("RESTAURATEUR_ADDINGREDIENT_INGREDIENT"));
        for(String i : allIngredient) {
            treeIngredientRoot.getChildren().add(new TreeItem<>(i));
        }
        treeIngredient.setRoot(treeIngredientRoot);

        textIngredient.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDINGREDIENT_INGREDIENT"));
        textIngredientSelect.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDINGREDIENT_INGREDIENT_SELECT"));
        textIngredientSelectExplanation.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDINGREDIENT_INGREDIENT_SELECTEXPLANATION"));
        textAllergens.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDINGREDIENT_ALLERGENS"));
        textAllergensExplanation.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDINGREDIENT_ALLERGENS_EXPLANATION"));
        textAllergensSelectExplanation.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDINGREDIENT_ALLERGENS_SELECTEXPLANATION"));
        textCookingMethod.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDINGREDIENT_COOKINGMETHOD"));
        textOptionality.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDINGREDIENT_OPTIONALITY"));
        buttonSave.setText(AppData.getInstance().i18n("SAVECHANGES"));

        checkBoxOptional.setText(AppData.getInstance().i18n(checkBoxOptional.getText()));
        checkBoxCooked.setText(AppData.getInstance().i18n(checkBoxCooked.getText()));

    }


    @FXML
    void clickButtonSave(ActionEvent event) {
        try {
            TreeItem<String> selectedItem = treeIngredient.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (addDishView != null) {
                    addDishView.addIngredient(
                            selectedItem.getValue(), checkBoxOptional.isSelected(), checkBoxCooked.isSelected()
                    );
                    Navigator.navigateContentParent(new AddDishForm(addDishView));
                } else {
                    changeDishView.addIngredient(
                            selectedItem.getValue(), checkBoxOptional.isSelected(), checkBoxCooked.isSelected()
                    );
                    Navigator.navigateContentParent(new ChangeDishForm(changeDishView));
                }
            }
        } catch (GenericViewException e) {
            Navigator.showError(e);
        }
    }
}
