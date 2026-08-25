package com.pickyeaters.logic.view.dish;

import com.pickyeaters.logic.bean.reply.ShowDishReply;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.bean.request.ShowDishRequest;
import com.pickyeaters.logic.controller.MenuController;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.ResultErrorException;

public class ShowDishView extends ReadableDishView {

    public ShowDishView(Request baseRequest, MenuController menuController, String dishID) {
        super(baseRequest, menuController);
        try {
            ShowDishReply reply = menuController.showDish(new ShowDishRequest(baseRequest, dishID)).getValue();
            this.dish = reply.getDish();
            this.allergenList = reply.getAllergenList();
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_FETCH");
        }
    }
}
