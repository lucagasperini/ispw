package com.pickyeaters.logic.bean.reply;

import com.pickyeaters.logic.bean.UserBean;

public class ShowUserReply {
    private final UserBean user;
    public ShowUserReply(UserBean user) {
        this.user = user;
    }

    public UserBean getUser() {
        return user;
    }
}
