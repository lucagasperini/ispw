package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.view.eatingpreference.EditEatingPreferenceView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class EatingPreferenceForm {

    @FXML
    private Button buttonSave;

    @FXML
    private CheckBox checkboxHealthPregnant;

    @FXML
    private CheckBox checkboxLifestyleCarnivore;

    @FXML
    private CheckBox checkboxLifestylePescatarian;

    @FXML
    private CheckBox checkboxLifestyleVegan;

    @FXML
    private CheckBox checkboxLifestyleVegetarian;

    @FXML
    private CheckBox checkboxReligiousHalal;

    @FXML
    private CheckBox checkboxReligiousKosher;

    @FXML
    private Text textHealthAllergies;

    @FXML
    private Text textHealthNeeds;

    @FXML
    private Text textHealthOthers;

    @FXML
    private Text textLifeStyleNeeds;

    @FXML
    private Text textReligiousNeeds;

    @FXML
    private Text textTaste;

    @FXML
    private Text textTasteExplanation;

    @FXML
    private Text textTasteMyDislikedIngredients;

    @FXML
    private VBox vboxIngredient;

    @FXML
    private VBox vboxAllergy;

    private EditEatingPreferenceView view;

    @FXML
    public void initialize() {
        Navigator.getMainForm().setTitle("PICKY_PERSONALIZEEATINGPREFERENCES");
        Navigator.getMainForm().showBackButton();
        view = AppData.getInstance().getApp().displayEditEatingPreferenceView();
        List<String> allAllergyList = view.showAllAllergen();
        List<String> allergyList = view.showAllergenList();

        for (String allergy : allAllergyList) {
            CheckBox cb = new CheckBox(allergy);
            VBox.setMargin(cb, new Insets(2,0,2,0));
            cb.setFont(new Font(16));
            cb.setSelected(allergyList.contains(allergy));
            cb.selectedProperty().addListener((a,b,c) -> {
                if(allergyList.contains(cb.getText())) {
                    view.removeAllergen(cb.getText());
                } else {
                    view.addAllergen(cb.getText());
                }
            });
            vboxAllergy.getChildren().add(cb);
        }

        checkboxHealthPregnant.setText(AppData.getInstance().i18n(checkboxHealthPregnant.getText()));
        checkboxLifestyleCarnivore.setText(AppData.getInstance().i18n(checkboxLifestyleCarnivore.getText()));
        checkboxLifestylePescatarian.setText(AppData.getInstance().i18n(checkboxLifestylePescatarian.getText()));
        checkboxLifestyleVegan.setText(AppData.getInstance().i18n(checkboxLifestyleVegan.getText()));
        checkboxLifestyleVegetarian.setText(AppData.getInstance().i18n(checkboxLifestyleVegetarian.getText()));
        checkboxReligiousHalal.setText(AppData.getInstance().i18n(checkboxReligiousHalal.getText()));
        checkboxReligiousKosher.setText(AppData.getInstance().i18n(checkboxReligiousKosher.getText()));

        textHealthAllergies.setText(AppData.getInstance().i18n(textHealthAllergies.getText()));
        textHealthNeeds.setText(AppData.getInstance().i18n(textHealthNeeds.getText()));
        textHealthOthers.setText(AppData.getInstance().i18n(textHealthOthers.getText()));
        textLifeStyleNeeds.setText(AppData.getInstance().i18n(textLifeStyleNeeds.getText()));
        textReligiousNeeds.setText(AppData.getInstance().i18n(textReligiousNeeds.getText()));
        textTaste.setText(AppData.getInstance().i18n(textTaste.getText()));
        textTasteExplanation.setText(AppData.getInstance().i18n(textTasteExplanation.getText()));
        textTasteMyDislikedIngredients.setText(AppData.getInstance().i18n(textTasteMyDislikedIngredients.getText()));

        buttonSave.setText(AppData.getInstance().i18n(buttonSave.getText()));

        checkboxHealthPregnant.setSelected(view.isPregnant());
        checkboxHealthPregnant.selectedProperty().addListener((a,b,c)-> {
            view.togglePregnant();
        });
        checkboxLifestyleCarnivore.setSelected(view.isCarnivore());
        checkboxLifestyleCarnivore.selectedProperty().addListener((a,b,c)-> {
            view.toggleCarnivore();
        });
        checkboxLifestylePescatarian.setSelected(view.isPescatarian());
        checkboxLifestylePescatarian.selectedProperty().addListener((a,b,c)-> {
            view.togglePescatarian();
        });
        checkboxLifestyleVegetarian.setSelected(view.isVegetarian());
        checkboxLifestyleVegetarian.selectedProperty().addListener((a,b,c)-> {
            view.toggleVegetarian();
        });
        checkboxReligiousHalal.setSelected(view.isHalal());
        checkboxReligiousHalal.selectedProperty().addListener((a,b,c)-> {
            view.toggleHalal();
        });
        checkboxReligiousKosher.setSelected(view.isKosher());
        checkboxReligiousKosher.selectedProperty().addListener((a,b,c)-> {
            view.toggleKosher();
        });
        checkboxLifestyleVegan.setSelected(view.isVegan());
        checkboxLifestyleVegan.selectedProperty().addListener((a,b,c)-> {
            view.toggleVegan();
        });

        List<String> allIngredientList = view.showAllIngredient();
        List<String> dislikeIngredientList = view.showExcludedIngredientList();
        for (String ingredient : allIngredientList) {

            CheckBox cb = new CheckBox(ingredient);
            VBox.setMargin(cb, new Insets(2,0,2,0));
            cb.setFont(new Font(16));
            cb.setSelected(dislikeIngredientList.contains(ingredient));
            cb.selectedProperty().addListener((a,b,c) -> {
                if(dislikeIngredientList.contains(cb.getText())) {
                    view.removeDislikeIngredient(cb.getText());
                } else {
                    view.addDislikeIngredient(cb.getText());
                }
            });
            vboxIngredient.getChildren().add(cb);
        }
    }

    @FXML
    void clickSaveChanges(ActionEvent event) {
        view.submit();
        Navigator.navigateContentParent();
    }
}
