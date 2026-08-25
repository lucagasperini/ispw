package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.view.dish.ChangeDishView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ChangeDishForm extends DishForm {

    private final ChangeDishView changeDishView;

    public ChangeDishForm(ChangeDishView changeDishView) {
        this.changeDishView = changeDishView;
    }

    @FXML
    public void initialize() {
        super.initialize();

        Navigator.getMainForm().setTitle("RESTAURATEUR_EDITDISH");
        buttonSave.setText(AppData.getInstance().i18n("SAVECHANGES"));
        Navigator.getMainForm().setDefaultActionBackButton();

        onViewChanged();

        inputName.textProperty().addListener((observable, oldValue, newValue ) -> {
            changeDishView.insertName(newValue);
        });

        inputDescription.textProperty().addListener((observable, oldValue, newValue ) -> {
            changeDishView.insertDescription(newValue);
        });

        comboBoxCategory.valueProperty().addListener((observable, oldValue, newValue ) -> {
            changeDishView.selectType(getCurrentComboBoxItem());
        });

        changeDishView.addObserver(this);

        for (String i : changeDishView.showAllergenList()) {
            listViewAllergen.getItems().add(i);
        }
    }

    @FXML
    protected void clickAddIngredient(ActionEvent event) {
        Navigator.navigateContent(
                "/form/AddIngredient.fxml",
                new AddIngredientForm(changeDishView)
        );
    }

    @FXML
    protected void clickSaveChanges(ActionEvent event) {
        try {
            changeDishView.submit();
            changeDishView.removeObserver(this);
            Navigator.navigateContentParent();
        } catch (GenericViewException e) {
            Navigator.showError(e);
        }
    }

    @Override
    public void onViewChanged() {
        vboxIngredient.getChildren().clear();

        inputName.setText(changeDishView.showName());
        inputDescription.setText(changeDishView.showDescription());
        comboBoxCategory.setValue(changeDishView.showType());

        for (String i : changeDishView.showIngredientNameList()) {
            Navigator.LoadedForm<IngredientItemWidget> node = Navigator.loadNode("/form/IngredientItemWidget.fxml");
            node.getController().init(changeDishView, changeDishView.displayIngredientView(i));
            vboxIngredient.getChildren().add(node.getNode());
        }
    }
}
