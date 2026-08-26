package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.model.EatingPreference;
import java.util.Optional;

public class PickieRepositoryFS implements PickieRepository {

    public Optional<EatingPreference> findEatingPreference(String userID) {
        throw new NotImplementedException();
    }


    public void editEatingPreference(String userID, EatingPreference eatingPreference) {
        throw new NotImplementedException();
    }
}