package com.pickyeaters.logic.bean.reply;

import java.util.List;

public class AllAllergenReply {
    private final List<String> allergenList;
    public AllAllergenReply(List<String> allergenList) {
        this.allergenList = allergenList;
    }

    public List<String> getAllergenList() {
        return allergenList;
    }
}
