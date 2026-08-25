package com.pickyeaters.logic.bean.reply;

import com.pickyeaters.logic.bean.EatingPreferenceBean;

public class ShowEatingPreferenceReply {
    private final EatingPreferenceBean eatingPreference;

    public ShowEatingPreferenceReply(EatingPreferenceBean eatingPreference) {
        this.eatingPreference = eatingPreference;
    }

    public EatingPreferenceBean getEatingPreference() {
        return eatingPreference;
    }
}
