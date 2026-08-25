package com.pickyeaters.logic.view.dish;

import com.pickyeaters.logic.bean.DishBean;
import com.pickyeaters.logic.bean.reply.AddDishReply;
import com.pickyeaters.logic.bean.request.AddDishRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.controller.MenuController;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.ResultErrorException;

public class AddDishView extends EditableDishView {

    public AddDishView(Request baseRequest, MenuController menuController) {
        super(baseRequest, menuController);

        this.dish = new DishBean();
    }

    public void submit() throws GenericViewException {
        try {
            AddDishRequest request = new AddDishRequest(baseRequest, dish);
            controller.addDish(request).getValue();
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_SAVE");
        }
    }
}
