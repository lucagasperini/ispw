package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.view.dish.ShowDishView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class ShowDishItemWidget {

    @FXML
    private Button buttonShowDish;

    @FXML
    private ListView<String> listviewIngredient;

    @FXML
    private Text textDescription;

    @FXML
    private Text textName;

    @FXML
    private Text textType;

    private String dishID;
    private String restaurantID;

    public void init(String dishID, String restaurantID) {
        this.dishID = dishID;
        this.restaurantID = restaurantID;
        ShowDishView view = AppData.getInstance().getApp().displayShowDishView(dishID);
        textName.setText(view.showName());
        textType.setText(view.showType());
        textDescription.setText(view.showDescription());

        for(String i : view.showIngredientNameList()) {
            listviewIngredient.getItems().add(i);
        }
    }

    @FXML
    void clickButtonShowDish(ActionEvent event) {
        Navigator.navigateContent(
                "/form/Dish.fxml",
                new ShowDishForm(AppData.getInstance().getApp().displayShowDishView(dishID), restaurantID)
        );
    }

}

