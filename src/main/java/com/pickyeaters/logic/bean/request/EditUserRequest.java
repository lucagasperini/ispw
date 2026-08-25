package com.pickyeaters.logic.bean.request;

import com.pickyeaters.logic.bean.UserBean;

public class EditUserRequest extends Request {
    private final UserBean user;
    public EditUserRequest(Request request, UserBean user) {
        super(request);
        this.user = user;
    }

    public UserBean getUser() {
        return user;
    }
}
