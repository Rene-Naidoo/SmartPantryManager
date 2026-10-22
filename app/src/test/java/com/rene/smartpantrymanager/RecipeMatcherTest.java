package com.rene.smartpantrymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.rene.smartpantrymanager.data.PantryItem;
import com.rene.smartpantrymanager.data.Recipe;
import com.rene.smartpantrymanager.data.RecipeIngredient;
import com.rene.smartpantrymanager.data.RecipeWithIngredients;
import com.rene.smartpantrymanager.logic.RecipeMatcher;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecipeMatcherTest {

    private RecipeIngredient ing(String name, double qty, String unit) {
        return new RecipeIngredient(0, name, qty, unit);
    }

    private PantryItem item(String name, double qty, String unit) {
        return new PantryItem(name, qty, unit, null);
    }

    private RecipeWithIngredients recipe(String name, RecipeIngredient... ings) {
        RecipeWithIngredients r = new RecipeWithIngredients();
        r.recipe = new Recipe(name, "steps");
        r.ingredients = Arrays.asList(ings);
        return r;
    }

    private RecipeWithIngredients fiveIngredientRecipe() {
        return recipe("Test Dish",
                ing("egg", 2, "pcs"), ing("tomato", 1, "pcs"), ing("butter", 10, "g"),
                ing("salt", 1, "tsp"), ing("milk", 100, "ml"));
    }

    @Test
    public void allIngredientsPresent_isSuggested() {
        List<PantryItem> pantry = Arrays.asList(
                item("egg", 2, "pcs"), item("tomato", 1, "pcs"), item("butter", 10, "g"),
                item("salt", 1, "tsp"), item("milk", 100, "ml"));
        List<RecipeWithIngredients> result =
                RecipeMatcher.findStrictMatches(pantry, Collections.singletonList(fiveIngredientRecipe()));
        assertEquals(1, result.size());
    }

    @Test
    public void fourOfFiveIngredients_isNotSuggested() {
        List<PantryItem> pantry = Arrays.asList(
                item("egg", 2, "pcs"), item("tomato", 1, "pcs"), item("butter", 10, "g"),
                item("salt", 1, "tsp")); // milk missing
        List<RecipeWithIngredients> result =
                RecipeMatcher.findStrictMatches(pantry, Collections.singletonList(fiveIngredientRecipe()));
        assertTrue(result.isEmpty());
    }

    @Test
    public void notEnoughQuantity_isNotSuggested() {
        List<PantryItem> pantry = Arrays.asList(
                item("egg", 1, "pcs"), // needs 2
                item("tomato", 1, "pcs"), item("butter", 10, "g"),
                item("salt", 1, "tsp"), item("milk", 100, "ml"));
        assertTrue(RecipeMatcher.findStrictMatches(
                pantry, Collections.singletonList(fiveIngredientRecipe())).isEmpty());
    }

    @Test
    public void pluralAndCapitalisedNames_stillMatch() {
        assertEquals("tomato", RecipeMatcher.normalizeName("Tomatoes "));
        assertEquals("potato", RecipeMatcher.normalizeName("POTATOES"));
        assertEquals("egg", RecipeMatcher.normalizeName("Eggs"));
        assertEquals("cheese", RecipeMatcher.normalizeName("cheese"));
    }

    @Test
    public void unitDifferences_areConverted() {
        RecipeWithIngredients r = recipe("Pancakes-lite", ing("flour", 150, "g"), ing("milk", 250, "ml"));
        List<PantryItem> pantry = Arrays.asList(item("Flour", 1, "kg"), item("milk", 1, "l"));
        assertEquals(1, RecipeMatcher.findStrictMatches(pantry, Collections.singletonList(r)).size());
    }

    @Test
    public void duplicatePantryRows_areAddedTogether() {
        RecipeWithIngredients r = recipe("Eggs", ing("egg", 3, "pcs"));
        List<PantryItem> pantry = Arrays.asList(item("egg", 2, "pcs"), item("eggs", 1, "pcs"));
        assertEquals(1, RecipeMatcher.findStrictMatches(pantry, Collections.singletonList(r)).size());
    }

    @Test
    public void missingExactlyOne_isAlmostThereButNotStrict() {
        List<PantryItem> pantry = Arrays.asList(
                item("egg", 2, "pcs"), item("tomato", 1, "pcs"), item("butter", 10, "g"),
                item("salt", 1, "tsp"));
        List<RecipeWithIngredients> all = Collections.singletonList(fiveIngredientRecipe());
        assertTrue(RecipeMatcher.findStrictMatches(pantry, all).isEmpty());
        assertEquals(1, RecipeMatcher.findAlmostThere(pantry, all).size());
    }

    @Test
    public void emptyPantry_suggestsNothing() {
        assertFalse(RecipeMatcher.findStrictMatches(
                Collections.<PantryItem>emptyList(),
                Collections.singletonList(fiveIngredientRecipe())).size() > 0);
    }
}