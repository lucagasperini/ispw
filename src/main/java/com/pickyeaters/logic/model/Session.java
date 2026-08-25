package com.pickyeaters.logic.model;

import java.util.Optional;

public class Session {

    private final String token;
    private final User user;

    public Session(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public Optional<Restaurateur> getRestaurateur() {
        if( isRestaurateur() ) {
            return Optional.ofNullable((Restaurateur) this.user);
        } else {
            // TODO: Throw error?
            return Optional.empty();
        }
    }

    public Optional<Pickie> getPickie() {
        if( isPickie() ) {
            return Optional.ofNullable((Pickie) this.user);
        } else {
            // TODO: Throw error?
            return Optional.empty();
        }
    }

    public Optional<Admin> getAdmin() {
        if( isAdmin() ) {
            return Optional.ofNullable((Admin) this.user);
        } else {
            // TODO: Throw error?
            return Optional.empty();
        }
    }

    public boolean isRestaurateur() {
        return this.user instanceof Restaurateur;
    }


    public boolean isPickie() {
        return this.user instanceof Pickie;
    }

    public boolean isAdmin() {
        return this.user instanceof Admin;
    }

    public String getUserID() {
        return this.user.getID();
    }
}
