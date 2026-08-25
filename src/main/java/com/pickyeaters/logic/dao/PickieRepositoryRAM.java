package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.model.*;
import com.pickyeaters.logic.utils.Logger;

import java.util.*;

public class PickieRepositoryRAM implements PickieRepository {
    private final Logger logger;
    private final Map<String, EatingPreference> eatingPreferenceMap;
    private final IngredientRepository ingredientRepository;

    public PickieRepositoryRAM(Logger logger, IngredientRepository ingredientRepository) {
        this.logger = logger;
        this.ingredientRepository = ingredientRepository;
        eatingPreferenceMap = new HashMap<>();

        Allergen a1 = new Allergen("1", "Allergen 1");
        Allergen a2 = new Allergen("2", "Allergen 2");
        Allergen a3 = new Allergen("3", "Allergen 3");
        Allergen a4 = new Allergen("4", "Allergen 4");

        List<Allergen> la1 = new ArrayList<>();
        la1.add(a1);
        la1.add(a2);
        List<Allergen> la2 = new ArrayList<>();
        la2.add(a3);
        la2.add(a4);
        List<Allergen> la4 = new ArrayList<>();
        la4.add(a3);

        Ingredient i2 = new Ingredient("2", "Ingredient 2", la2, false, false);
        Ingredient i4 = new Ingredient("4", "Ingredient 4", la4, false, false);

        List<Ingredient> li1 = new ArrayList<>();
        li1.add(i2);
        List<Ingredient> li2 = new ArrayList<>();
        li1.add(i4);

        ExcludedGroup eg1 = new ExcludedGroup("1", "HALAL", li2);

        List<ExcludedGroup> leg1 = new ArrayList<>();
        leg1.add(eg1);

        EatingPreference ep1 = new EatingPreference(li1, leg1, la1);

        EatingPreference ep2 = new EatingPreference(li2, leg1, la1);

        eatingPreferenceMap.put("2", ep1);
        eatingPreferenceMap.put("3", ep2);
    }
    public Optional<EatingPreference> findEatingPreference(String userID) {
        return Optional.ofNullable(eatingPreferenceMap.get(userID));
    }

    public void editEatingPreference(String userID, EatingPreference eatingPreference) {
        try {
            eatingPreferenceMap.replace(userID, eatingPreference);
        } catch (NullPointerException ex) {
            logger.error(ex.getMessage(), ex);
            throw new GenericRepositoryException("userID not found");
        }
    }
}
