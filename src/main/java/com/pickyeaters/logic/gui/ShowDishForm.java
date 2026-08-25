package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.view.dish.ShowDishView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ShowDishForm extends DishForm {

    private ShowDishView showDishView;
    private String restaurantID;

    public ShowDishForm(ShowDishView showDishView, String restaurantID) {
        this.showDishView = showDishView;
        this.restaurantID = restaurantID;
    }

    @FXML
    @Override
    public void initialize() {
        super.initialize();

        Navigator.getMainForm().setActionBackButton(() -> {
            ShowMenuDetailsForm form = Navigator.navigateContentParent();
            form.init(restaurantID);
        });

        Navigator.getMainForm().setTitle("RESTAURATEUR_SHOWDISH");


        inputName.setText(showDishView.showName());
        inputName.setEditable(false);
        inputDescription.setText(showDishView.showDescription());
        inputDescription.setEditable(false);
        comboBoxCategory.setValue(showDishView.showType());
        comboBoxCategory.setDisable(true);

        buttonSave.setVisible(false);
        buttonAddIngredient.setVisible(false);



        for (String i : showDishView.showIngredientNameList()) {
            Navigator.LoadedForm<IngredientItemWidget> node = Navigator.loadNode("/form/IngredientItemWidget.fxml");
            node.getController().init(showDishView.displayIngredientView(i).showFullName());
            vboxIngredient.getChildren().add(node.getNode());
        }

        for (String i : showDishView.showAllergenList()) {
            listViewAllergen.getItems().add(i);
        }
    }


    @FXML
    protected void clickAddIngredient(ActionEvent event) {
        throw new NotImplementedException();
    }

    @FXML
    protected void clickSaveChanges(ActionEvent event) {
        throw new NotImplementedException();
    }


    @Override
    public void onViewChanged() {
        throw new NotImplementedException();
    }
}
