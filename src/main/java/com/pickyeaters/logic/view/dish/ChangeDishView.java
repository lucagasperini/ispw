package com.pickyeaters.logic.view.dish;

import com.pickyeaters.logic.bean.reply.ShowDishReply;
import com.pickyeaters.logic.bean.request.ChangeDishRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.bean.request.ShowDishRequest;
import com.pickyeaters.logic.controller.MenuController;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.ResultErrorException;

import java.util.ArrayList;

public class ChangeDishView extends EditableDishView {

    private final String id;

    public ChangeDishView(Request baseRequest, MenuController menuController, String dishID) throws GenericViewException {
        super(baseRequest, menuController);
        try {
            ShowDishReply reply = menuController.showDish(new ShowDishRequest(baseRequest, dishID)).getValue();
            this.id = dishID;
            this.dish = reply.getDish();
            this.allergenList = new ArrayList<>(reply.getAllergenList());
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_FETCH");
        }
    }

    public void submit() throws GenericViewException {
        try {
            ChangeDishRequest request = new ChangeDishRequest(baseRequest, id, dish);
            controller.changeDish(request).getValue();
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_SAVE");
        }
    }
}
