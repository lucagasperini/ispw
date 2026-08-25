package com.pickyeaters.logic.view;

import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.exception.GenericViewException;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class VirtualView implements ViewSubject {

    protected final List<ViewObserver> viewObserverList = new ArrayList<>();
    protected final Request baseRequest;

    public VirtualView(Request request) {
        baseRequest = request;
    }

    static protected ResourceBundle localeBundle = null;

    static public String i18n(String key) {
        try {
            if(localeBundle == null) {
                return "#" + key + "#";
            }
            return localeBundle.getString(key);
        } catch (MissingResourceException e) {
            return "[" + key + "]";
        }
    }

    public void addObserver(ViewObserver observer) {
        viewObserverList.add(observer);
    }

    public void removeObserver(ViewObserver observer) {
        viewObserverList.remove(observer);
    }

    public void notifyAllObserver() {
        for(ViewObserver i : viewObserverList) {
            i.onViewChanged();
        }
    }
}
