package com.pickyeaters.logic.bean.reply;

import java.util.List;

public class AllCityReply {
    private final List<String> cityList;
    public AllCityReply(List<String> cityList) {
        this.cityList = cityList;
    }

    public List<String> getCityList() {
        return cityList;
    }
}
