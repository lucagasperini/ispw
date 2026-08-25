package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.model.EatingPreference;
import com.pickyeaters.logic.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface PickieRepository {
    Optional<EatingPreference> findEatingPreference(String userID);
    void editEatingPreference(String userID, EatingPreference eatingPreference);
}
