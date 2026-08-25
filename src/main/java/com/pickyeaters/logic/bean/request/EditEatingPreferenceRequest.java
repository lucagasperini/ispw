package com.pickyeaters.logic.bean.request;

import com.pickyeaters.logic.bean.EatingPreferenceBean;

public class EditEatingPreferenceRequest extends Request {
    private final EatingPreferenceBean eatingPreference;
    public EditEatingPreferenceRequest(Request request, EatingPreferenceBean eatingPreference) {
        super(request);
        this.eatingPreference = eatingPreference;
    }

    public EatingPreferenceBean getEatingPreference() {
        return eatingPreference;
    }
}
