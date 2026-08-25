package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.view.IngredientView;
import com.pickyeaters.logic.view.dish.AddDishView;
import com.pickyeaters.logic.view.dish.ChangeDishView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class IngredientItemWidget {

    @FXML
    private Button buttonDeleteIngredient;

    @FXML
    private Text textIngredient;

    private ChangeDishView changeDishView;
    private AddDishView addDishView;
    private IngredientView ingredientView;

    public void init(String ingredientName) {
        this.ingredientView = null;
        this.changeDishView = null;
        textIngredient.setText(ingredientName);

        buttonDeleteIngredient.setVisible(false);
    }

    public void init(ChangeDishView changeDishView, IngredientView ingredientView) {
        this.ingredientView = ingredientView;
        this.changeDishView = changeDishView;
        textIngredient.setText(ingredientView.showFullName());

        buttonDeleteIngredient.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDDISH_REMOVEINGREDIENT"));
    }

    public void init(AddDishView addDishView, IngredientView ingredientView) {
        this.ingredientView = ingredientView;
        this.addDishView = addDishView;
        textIngredient.setText(ingredientView.showFullName());

        buttonDeleteIngredient.setText(AppData.getInstance().i18n("RESTAURATEUR_ADDDISH_REMOVEINGREDIENT"));
    }

    @FXML
    private void clickButtonDeleteIngredient(ActionEvent actionEvent) {
        if(changeDishView != null) {
            changeDishView.removeIngredient(ingredientView.showName());
        } else if(addDishView != null) {
            addDishView.removeIngredient(ingredientView.showName());
        } else {
            throw new NotImplementedException();
        }

    }
}
