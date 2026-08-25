package com.pickyeaters.logic.view;

import com.pickyeaters.logic.bean.SystemParameterBean;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.exception.BeanInvalidValueException;
import com.pickyeaters.logic.exception.LocaleViewException;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleView extends VirtualView {

    private final SystemParameterBean systemParameter;
    private static final String BUNDLE_NAME = "i18n";

    public LocaleView(SystemParameterBean systemParameter) {
        super(new Request(""));
        this.systemParameter = systemParameter;
    }

    public void loadLocale() {
        final String lang = this.systemParameter.getLocaleLang();
        if(lang == null) {
            throw new LocaleViewException("");
        }

        localeBundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(lang));
    }


    public void selectLocaleLang(String localeLang) {
        try {
            this.systemParameter.setLocaleLang(localeLang);
        } catch (BeanInvalidValueException e) {
            throw new LocaleViewException(e.getMessage());
        }
    }
}