package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.view.dish.AddDishView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AddDishForm extends DishForm {

    private final AddDishView addDishView;

    public AddDishForm(AddDishView addDishView) {
        this.addDishView = addDishView;
    }

    @FXML
    public void initialize() {
        super.initialize();

        Navigator.getMainForm().setTitle("RESTAURATEUR_ADDDISH");
        buttonSave.setText(AppData.getInstance().i18n("SAVECHANGES"));
        Navigator.getMainForm().setDefaultActionBackButton();

        onViewChanged();

        inputName.textProperty().addListener((observable, oldValue, newValue ) -> {
            addDishView.insertName(newValue);
        });

        inputDescription.textProperty().addListener((observable, oldValue, newValue ) -> {
            addDishView.insertDescription(newValue);
        });

        comboBoxCategory.valueProperty().addListener((observable, oldValue, newValue ) -> {
            addDishView.selectType(getCurrentComboBoxItem());
        });

        addDishView.addObserver(this);

        for (String i : addDishView.showAllergenList()) {
            listViewAllergen.getItems().add(i);
        }
    }

    @FXML
    protected void clickAddIngredient(ActionEvent event) {
        Navigator.navigateContent(
                "/form/AddIngredient.fxml",
                new AddIngredientForm(addDishView)
        );
    }

    @FXML
    protected void clickSaveChanges(ActionEvent event) {
        addDishView.submit();
        Navigator.navigateContentParent();
    }

    @Override
    public void onViewChanged() {
        vboxIngredient.getChildren().clear();
        inputName.setText(addDishView.showName());
        inputDescription.setText(addDishView.showDescription());
        comboBoxCategory.setValue(addDishView.showType());

        for (String i : addDishView.showIngredientNameList()) {
            Navigator.LoadedForm<IngredientItemWidget> node = Navigator.loadNode("/form/IngredientItemWidget.fxml");
            node.getController().init(addDishView, addDishView.displayIngredientView(i));
            vboxIngredient.getChildren().add(node.getNode());
        }
    }
}
