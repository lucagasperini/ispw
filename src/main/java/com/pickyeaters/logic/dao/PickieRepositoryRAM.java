package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.model.*;
import com.pickyeaters.logic.utils.Logger;

import java.util.*;

public class PickieRepositoryRAM implements PickieRepository {
    private final Logger logger;
    private final Map<String, EatingPreference> eatingPreferenceMap;

    public PickieRepositoryRAM(Logger logger, IngredientRepository ingredientRepository) {
        this.logger = logger;
        eatingPreferenceMap = new HashMap<>();

        List<Allergen> la1 = new ArrayList<>();
        la1.add(ingredientRepository.findAllergenByName("Allergen 1").orElseThrow());
        la1.add(ingredientRepository.findAllergenByName("Allergen 2").orElseThrow());

        List<Ingredient> li1 = new ArrayList<>();
        li1.add(ingredientRepository.findIngredientByName("Ingredient 2").orElseThrow());
        List<Ingredient> li2 = new ArrayList<>();
        li1.add(ingredientRepository.findIngredientByName("Ingredient 4").orElseThrow());

        List<ExcludedGroup> leg1 = new ArrayList<>();
        leg1.add(ingredientRepository.findExcludedGroupByName(ExcludedGroup.GROUP_NAME_HALAL).orElseThrow());

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
