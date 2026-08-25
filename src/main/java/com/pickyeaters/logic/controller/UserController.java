package com.pickyeaters.logic.controller;

import com.pickyeaters.logic.bean.UserBean;
import com.pickyeaters.logic.bean.reply.*;
import com.pickyeaters.logic.bean.request.EditUserRequest;
import com.pickyeaters.logic.bean.request.ShowUserRequest;
import com.pickyeaters.logic.dao.UserRepository;
import com.pickyeaters.logic.exception.GenericFactoryException;
import com.pickyeaters.logic.exception.LoginControllerException;
import com.pickyeaters.logic.exception.LoginControllerPermissionException;
import com.pickyeaters.logic.model.Pickie;
import com.pickyeaters.logic.model.Restaurant;
import com.pickyeaters.logic.model.User;
import com.pickyeaters.logic.utils.Logger;

import java.util.NoSuchElementException;

public class UserController {
    private final Logger logger;

    private final UserRepository repository;
    private final LoginController loginController;

    public UserController(Logger logger, LoginController loginController, UserRepository userRepository) {
        this.logger = logger;
        this.loginController = loginController;
        repository = userRepository;
    }

    public Result<ShowUserReply> showUser(ShowUserRequest request) {
        try {
            String userID = loginController.requestUserID(request);
            loginController.checkUserPermission(request, LoginController.PERMISSION_SHOW_USER);
            User user = repository.getUserByID(userID).orElseThrow();
            return Result.ok(new ShowUserReply(new UserBean(user)));
        } catch (LoginControllerException | LoginControllerPermissionException e) {
            return Result.error(e.getMessage());
        } catch (NoSuchElementException e) {
            return Result.error("Can't find user for requested userID");
        }
    }

    public Result<EditUserReply> editUser(EditUserRequest request) {
        try {
            String userID = loginController.requestUserID(request);
            loginController.checkUserPermission(request, LoginController.PERMISSION_CHANGE_DISH);
            // TODO: This is a workaround, we cant create an instance of User, so I create a Pickie
            User user = new Pickie(
                    userID, request.getUser().getEmail(), "",
                    request.getUser().getFirstname(), request.getUser().getLastname()
            );
            repository.editUser(user);
            return Result.ok(new EditUserReply());
        } catch (LoginControllerException | LoginControllerPermissionException e) {
            return Result.error(e.getMessage());
        } catch (NoSuchElementException e) {
            return Result.error("Can't find user for requested userID");
        }
    }
}
