package com.rene.smartpantrymanager.logic;

import com.rene.smartpantrymanager.data.PantryItem;
import com.rene.smartpantrymanager.data.RecipeIngredient;
import com.rene.smartpantrymanager.data.RecipeWithIngredients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Core business logic: decides which recipes can be made from the pantry.
 * STRICT RULE: a recipe qualifies only if EVERY ingredient is in the pantry
 * in at least the required quantity.
 */
public class RecipeMatcher {

    private static final double EPSILON = 1e-9; // avoids floating-point rounding problems

    // ---------- Public API ----------

    /** Recipes that can be made right now (nothing missing). */
    public static List<RecipeWithIngredients> findStrictMatches(
            List<PantryItem> pantry, List<RecipeWithIngredients> recipes) {
        Map<String, Double> totals = buildPantryTotals(pantry);
        List<RecipeWithIngredients> result = new ArrayList<>();
        for (RecipeWithIngredients r : recipes) {
            if (countMissing(r, totals) == 0) {
                result.add(r);
            }
        }
        return result;
    }

    /** Optional bonus: recipes missing exactly ONE ingredient. */
    public static List<RecipeWithIngredients> findAlmostThere(
            List<PantryItem> pantry, List<RecipeWithIngredients> recipes) {
        Map<String, Double> totals = buildPantryTotals(pantry);
        List<RecipeWithIngredients> result = new ArrayList<>();
        for (RecipeWithIngredients r : recipes) {
            if (countMissing(r, totals) == 1) {
                result.add(r);
            }
        }
        return result;
    }

    /** How many of a recipe's ingredients are missing or insufficient. */
    public static int countMissing(RecipeWithIngredients recipe, Map<String, Double> pantryTotals) {
        // A recipe with no ingredients must never count as "makeable"
        if (recipe.ingredients == null || recipe.ingredients.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        int missing = 0;
        for (RecipeIngredient ing : recipe.ingredients) {
            String key = makeKey(ing.name, ing.unit);
            double needed = toBase(ing.quantity, cleanUnit(ing.unit));
            Double have = pantryTotals.get(key);
            if (have == null || have + EPSILON < needed) {
                missing++;
            }
        }
        return missing;
    }

    /** Adds up the pantry per ingredient, so two "egg" rows are summed. */
    public static Map<String, Double> buildPantryTotals(List<PantryItem> pantry) {
        Map<String, Double> totals = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = makeKey(item.name, item.unit);
            double amount = toBase(item.quantity, cleanUnit(item.unit));
            Double existing = totals.get(key);
            totals.put(key, (existing == null ? 0 : existing) + amount);
        }
        return totals;
    }

    // ---------- Name normalising ----------

    /** "Tomatoes " -> "tomato", "Potatoes" -> "potato", "Eggs" -> "egg". */
    public static String normalizeName(String raw) {
        if (raw == null) return "";
        String s = raw.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if (s.length() > 3) {
            if (s.endsWith("ies")) {
                s = s.substring(0, s.length() - 3) + "y";       // berries -> berry
            } else if (s.endsWith("oes") || s.endsWith("ches")
                    || s.endsWith("shes") || s.endsWith("xes") || s.endsWith("sses")) {
                s = s.substring(0, s.length() - 2);             // tomatoes -> tomato
            } else if (s.endsWith("s") && !s.endsWith("ss") && !s.endsWith("us")) {
                s = s.substring(0, s.length() - 1);             // eggs -> egg
            }
        }
        return s;
    }

    // ---------- Unit handling ----------

    private static String makeKey(String name, String unit) {
        return normalizeName(name) + "|" + dimensionOf(cleanUnit(unit));
    }

    /** Maps spelling variants to one standard unit name. */
    private static String cleanUnit(String unit) {
        if (unit == null) return "pcs";
        String u = unit.trim().toLowerCase(Locale.ROOT).replace(".", "");
        switch (u) {
            case "g": case "gr": case "gram": case "grams":
                return "g";
            case "kg": case "kilo": case "kilos": case "kilogram": case "kilograms":
                return "kg";
            case "ml": case "milliliter": case "milliliters":
            case "millilitre": case "millilitres":
                return "ml";
            case "l": case "liter": case "liters": case "litre": case "litres":
                return "l";
            case "tsp": case "teaspoon": case "teaspoons":
                return "tsp";
            case "tbsp": case "tablespoon": case "tablespoons":
                return "tbsp";
            case "": case "pcs": case "pc": case "piece": case "pieces":
                return "pcs";
            default:
                return u;
        }
    }

    /** Only units of the same kind can be compared. */
    private static String dimensionOf(String unit) {
        switch (unit) {
            case "g": case "kg": return "mass";
            case "ml": case "l": case "tsp": case "tbsp": return "volume";
            case "pcs": return "count";
            default: return unit; // unknown units only match themselves
        }
    }

    /** Converts to the base unit: grams, millilitres or pieces. */
    private static double toBase(double quantity, String unit) {
        switch (unit) {
            case "kg": case "l": return quantity * 1000;
            case "tbsp": return quantity * 15;
            case "tsp": return quantity * 5;
            default: return quantity;
        }
    }
}