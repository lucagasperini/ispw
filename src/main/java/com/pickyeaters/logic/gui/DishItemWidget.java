package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.view.dish.ShowDishView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class DishItemWidget {
    @FXML
    private Text textName;
    @FXML
    private Text textType;
    @FXML
    private Text textDescription;
    @FXML
    private Button buttonUpdateDish;
    @FXML
    private Button buttonDeleteDish;
    @FXML
    private ListView<String> listviewIngredient;
    @FXML
    private CheckBox checkBoxActive;

    private String dishID;

    public void init(String dishID) {
        this.dishID = dishID;
        ShowDishView view = AppData.getInstance().getApp().displayShowDishView(dishID);
        textName.setText(view.showName());
        textType.setText(view.showType());
        textDescription.setText(view.showDescription());

        for(String i : view.showIngredientNameList()) {
            listviewIngredient.getItems().add(i);
        }
    }

    @FXML
    private void clickCheckBoxActive(ActionEvent actionEvent) {
        /*
        Map<String, String> arg = new HashMap<>();
        arg.put("activeDish", textName.getText());
        toParent(arg);
         */
    }

    @FXML
    private void clickButtonUpdateDish(ActionEvent actionEvent) {
        Navigator.navigateContent(
                "/form/Dish.fxml",
                new ChangeDishForm(AppData.getInstance().getApp().displayChangeDishView(dishID))
        );
    }

    @FXML
    private void clickButtonDeleteDish(ActionEvent actionEvent) {
        AppData.getInstance().getApp().displayMenuView().submitRemoveDish(dishID);
        Navigator.refreshContent();
    }
}
