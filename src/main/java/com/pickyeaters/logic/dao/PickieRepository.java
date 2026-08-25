package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.model.EatingPreference;

import java.util.Optional;

public interface PickieRepository {
    Optional<EatingPreference> findEatingPreference(String userID);
    void editEatingPreference(String userID, EatingPreference eatingPreference);
}
