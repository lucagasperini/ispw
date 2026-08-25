package com.pickyeaters.logic.bean.reply;


import com.pickyeaters.logic.bean.DishBean;

import java.util.List;
import java.util.Map;

public class ShowMenuReply {
    private final Map<String, DishBean> dishMap;

    public ShowMenuReply(Map<String, DishBean> dishMap) {
        this.dishMap = Map.copyOf(dishMap);
    }

    public Map<String, DishBean> getDishMap() {
        return dishMap;
    }

}
