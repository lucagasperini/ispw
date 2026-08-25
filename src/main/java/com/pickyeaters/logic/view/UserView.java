package com.pickyeaters.logic.view;

import com.pickyeaters.logic.bean.UserBean;
import com.pickyeaters.logic.bean.request.EditUserRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.bean.request.ShowUserRequest;
import com.pickyeaters.logic.controller.UserController;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.ResultErrorException;


public class UserView extends VirtualView {

    private final UserBean user;
    private final UserController userController;

    public UserView(Request baseRequest, UserController userController) {
        super(baseRequest);
        this.userController = userController;

        try {
            user = userController.showUser(new ShowUserRequest(baseRequest)).getValue().getUser();
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_FETCH");
        }
    }

    public String showFullName() {
        return user.getFirstname() + " " + user.getLastname();
    }

    public String showFirstname() {
        return user.getFirstname();
    }

    public String showLastname() {
        return user.getLastname();
    }

    public String showEmail() {
        return user.getEmail();
    }

    public void insertEmail(String email) {
        user.setEmail(email);
    }

    public void insertFirstname(String firstname) {
        user.setFirstname(firstname);
    }

    public void insertLastname(String lastname) {
        user.setLastname(lastname);
    }

    public void submit() {
        try {
            userController.editUser(new EditUserRequest(baseRequest, user));
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_SAVE");
        }
    }

}
