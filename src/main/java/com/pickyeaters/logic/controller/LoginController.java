package com.pickyeaters.logic.controller;

import com.pickyeaters.logic.bean.AuthBean;
import com.pickyeaters.logic.bean.reply.LoginReply;
import com.pickyeaters.logic.bean.reply.Result;
import com.pickyeaters.logic.bean.request.LoginRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.dao.UserRepository;
import com.pickyeaters.logic.dao.UserRepositoryDB;
import com.pickyeaters.logic.dao.UserRepositoryRAM;
import com.pickyeaters.logic.exception.LoginControllerException;
import com.pickyeaters.logic.exception.LoginControllerPermissionException;
import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.model.Session;
import com.pickyeaters.logic.model.User;
import com.pickyeaters.logic.utils.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class LoginController  {
    public static final String PERMISSION_SHOW_ALLERGENINGREDIENT = "SHOW_ALLERGENINGREDIENT";
    public static final String PERMISSION_ALLALLERGEN = "ALLALLERGEN";
    public static final String PERMISSION_ALLCITY = "ALLCITY";

    public static final String PERMISSION_ALLINGREDIENT = "ALLINGREDIENT";
    public static final String PERMISSION_SHOW_USER = "SHOW_USER";

    public static final String PERMISSION_FIND_RESTAURANT = "FINDRESTAURANT";

    public static final String PERMISSION_SHOW_EATINGPREFERENCE = "SHOW_EATINGPREFERENCE";
    public static final String PERMISSION_EDIT_EATINGPREFERENCE = "EDIT_EATINGPREFERENCE";

    public static final String PERMISSION_SHOW_MENU = "SHOW_MENU";
    public static final String PERMISSION_SHOW_DISH = "SHOW_DISH";
    public static final String PERMISSION_ADD_DISH = "ADD_DISH";
    public static final String PERMISSION_REMOVE_DISH = "REMOVE_DISH";
    public static final String PERMISSION_CHANGE_DISH = "CHANGE_DISH";

    public static final String USER_TYPE_RESTAURATEUR="RESTAURATEUR";
    public static final String USER_TYPE_PICKIE="USER_TYPE_PICKIE";
    public static final String USER_TYPE_ADMIN="USER_TYPE_ADMIN";

    private final Logger logger;

    private final List<Session> sessionList = new ArrayList<>();
    private final UserRepository repository;

    public LoginController(Logger logger, UserRepository userRepository) {
        this.logger = logger;
        repository = userRepository;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO: generate message error too
            return password;
        }
    }

    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] b = new byte[32];
        secureRandom.nextBytes(b);

        String hex = HexFormat.of().formatHex(b);
        if(checkIfTokenUsed(hex)) {
            return generateToken();
        } else {
            return hex;
        }
    }

    private boolean checkIfTokenUsed(String token) {
        for(Session s : sessionList) {
            if(s.getToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

    public Result<LoginReply> login(LoginRequest request) {
        try {
            AuthBean auth = request.getAuth();
            User user = repository.getUserByEmail(auth.getEmail()).orElseThrow();
            if(user.checkPassword(auth.getPassword())) {
                // User is logged in
                Session session = new Session(generateToken(), user); generateToken();
                LoginReply reply = new LoginReply(getLoggedUserType(session), session.getToken());
                sessionList.add(session);
                logger.info("Login: success for user: " + user.getEmail());
                return Result.ok(reply);
            }
            logger.warn("Login: No such user or password");
            return Result.error("No such user or password");
        } catch (NoSuchElementException e) {
            logger.warn("Login: No such user or password");
            return Result.error("No such user or password");
        }
    }

    public void checkUserPermission(Request request, String permission) throws LoginControllerException, LoginControllerPermissionException {
        Optional<Session> optionalSession = getSession(request.getToken());
        if(optionalSession.isEmpty()) {
            throw new LoginControllerException("No valid user provided!");
        }

        Session session = optionalSession.orElseThrow();

        switch (permission) {
            case PERMISSION_SHOW_MENU,
                 PERMISSION_SHOW_DISH,
                 PERMISSION_SHOW_USER,
                 PERMISSION_ALLALLERGEN,
                 PERMISSION_SHOW_ALLERGENINGREDIENT,
                 PERMISSION_ALLINGREDIENT,
                 PERMISSION_ALLCITY:
                if(session.isRestaurateur() || session.isPickie()) {
                    return;
                } else {
                    break;
                }
            case PERMISSION_ADD_DISH,
                 PERMISSION_REMOVE_DISH,
                 PERMISSION_CHANGE_DISH:
                if(session.isRestaurateur()) {
                    return;
                } else {
                    break;
                }
            case PERMISSION_SHOW_EATINGPREFERENCE,
                 PERMISSION_EDIT_EATINGPREFERENCE,
                 PERMISSION_FIND_RESTAURANT:
                if(session.isPickie()) {
                    return;
                } else {
                    break;
                }
            default:
                throw new NotImplementedException();
        }

        LoginControllerPermissionException e = new LoginControllerPermissionException(
                "Login: Permission error for token: " + request.getToken() + " permission: " + permission
        );
        logger.error(e.getMessage(), e);
        throw e;
    }

    private Optional<Session> getSession(String token) {
        for (Session s : sessionList) {
            if (s.getToken().equals(token)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    private String getUserID(String token) {
        try {
            return getSession(token).orElseThrow().getUserID();
        } catch (NoSuchElementException e) {
            throw new LoginControllerException("Invalid token provided, cannot fetch userID");
        }
    }

    public String requestUserID(Request request) {
        return getUserID(request.getToken());
    }

    public String getLoggedUserType(Session session) {
        if(session.isRestaurateur()) {
            return USER_TYPE_RESTAURATEUR;
        } else if (session.isPickie()) {
            return USER_TYPE_PICKIE;
        } else if (session.isAdmin()) {
            return USER_TYPE_ADMIN;
        } else {
            throw new NotImplementedException();
        }
    }


}
