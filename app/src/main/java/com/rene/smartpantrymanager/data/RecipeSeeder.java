package com.rene.smartpantrymanager.data;

import java.util.Arrays;

/** Inserts the starter recipes the first time the app runs. */
public class RecipeSeeder {

    public static void seedIfEmpty(AppDatabase db) {
        RecipeDao dao = db.recipeDao();
        if (dao.getRecipeCount() > 0) return; // already seeded, do nothing

        add(dao, "Tomato Omelette", "Beat the eggs with salt. Fry the chopped tomato in butter, pour in the eggs and cook until set.",
                ing("egg", 3, "pcs"), ing("tomato", 1, "pcs"), ing("butter", 10, "g"), ing("salt", 1, "tsp"));
        add(dao, "Scrambled Eggs on Toast", "Toast the bread. Scramble the eggs in butter with salt and serve on top.",
                ing("egg", 2, "pcs"), ing("bread", 2, "pcs"), ing("butter", 10, "g"), ing("salt", 1, "tsp"));
        add(dao, "Cheese Toastie", "Butter the bread, add cheese between the slices and fry both sides until golden.",
                ing("bread", 2, "pcs"), ing("cheese", 50, "g"), ing("butter", 10, "g"));
        add(dao, "Pancakes", "Whisk flour, egg, milk and sugar into a batter. Fry ladles of batter in butter until golden on both sides.",
                ing("flour", 150, "g"), ing("egg", 1, "pcs"), ing("milk", 250, "ml"), ing("sugar", 1, "tbsp"), ing("butter", 10, "g"));
        add(dao, "Garlic Butter Pasta", "Boil the pasta. Fry crushed garlic in butter, toss with the pasta and salt.",
                ing("pasta", 200, "g"), ing("garlic", 2, "pcs"), ing("butter", 30, "g"), ing("salt", 1, "tsp"));
        add(dao, "Tomato Pasta", "Boil the pasta. Simmer chopped tomatoes and garlic in olive oil with salt, then mix through the pasta.",
                ing("pasta", 200, "g"), ing("tomato", 3, "pcs"), ing("garlic", 1, "pcs"), ing("olive oil", 2, "tbsp"), ing("salt", 1, "tsp"));
        add(dao, "Egg Fried Rice", "Fry chopped onion in oil, add cooked rice and soy sauce, push aside and scramble the eggs in, then mix.",
                ing("rice", 200, "g"), ing("egg", 2, "pcs"), ing("onion", 1, "pcs"), ing("soy sauce", 2, "tbsp"), ing("cooking oil", 1, "tbsp"));
        add(dao, "Mashed Potatoes", "Boil peeled potatoes until soft, drain, then mash with butter, milk and salt.",
                ing("potato", 4, "pcs"), ing("butter", 30, "g"), ing("milk", 50, "ml"), ing("salt", 1, "tsp"));
        add(dao, "Roast Potatoes", "Cut potatoes into chunks, toss in olive oil and salt, roast at 200C for 40 minutes.",
                ing("potato", 4, "pcs"), ing("olive oil", 2, "tbsp"), ing("salt", 1, "tsp"));
        add(dao, "Chicken Rice Bowl", "Season and fry the chicken with onion until cooked, then serve over cooked rice.",
                ing("chicken breast", 300, "g"), ing("rice", 200, "g"), ing("onion", 1, "pcs"), ing("salt", 1, "tsp"));
        add(dao, "Banana Pancakes", "Mash the bananas, mix with eggs and flour, and fry small pancakes until golden.",
                ing("banana", 2, "pcs"), ing("egg", 2, "pcs"), ing("flour", 50, "g"));
        add(dao, "Oat Porridge", "Heat the oats with milk and sugar, stirring, for 5 minutes until thick.",
                ing("oat", 50, "g"), ing("milk", 250, "ml"), ing("sugar", 1, "tsp"));
        add(dao, "Cheesy Scrambled Eggs", "Scramble the eggs in butter and stir in the cheese just before they set.",
                ing("egg", 3, "pcs"), ing("cheese", 30, "g"), ing("butter", 10, "g"));
        add(dao, "Simple Tomato Soup", "Soften onion and garlic in butter, add chopped tomatoes and salt, simmer 20 minutes, then blend.",
                ing("tomato", 4, "pcs"), ing("onion", 1, "pcs"), ing("garlic", 1, "pcs"), ing("butter", 20, "g"), ing("salt", 1, "tsp"));
        add(dao, "Spanish Omelette", "Slowly fry sliced potato and onion in olive oil, add beaten eggs and cook through on both sides.",
                ing("egg", 4, "pcs"), ing("potato", 2, "pcs"), ing("onion", 1, "pcs"), ing("olive oil", 3, "tbsp"));
        add(dao, "French Toast", "Whisk eggs and milk, soak the bread slices, then fry in butter until golden.",
                ing("bread", 4, "pcs"), ing("egg", 2, "pcs"), ing("milk", 100, "ml"), ing("butter", 10, "g"));
        add(dao, "Garlic Bread", "Mix crushed garlic into butter, spread on the bread and bake until crisp.",
                ing("bread", 4, "pcs"), ing("garlic", 2, "pcs"), ing("butter", 40, "g"));
        add(dao, "Rice Pudding", "Simmer rice in milk with sugar, stirring often, for 30 minutes until creamy.",
                ing("rice", 100, "g"), ing("milk", 500, "ml"), ing("sugar", 2, "tbsp"));
    }

    // Inserts the recipe, then its ingredients using the generated recipe id
    private static void add(RecipeDao dao, String name, String steps, RecipeIngredient... ingredients) {
        long recipeId = dao.insertRecipe(new Recipe(name, steps));
        for (RecipeIngredient i : ingredients) {
            i.recipeId = (int) recipeId;
        }
        dao.insertIngredients(Arrays.asList(ingredients));
    }

    private static RecipeIngredient ing(String name, double quantity, String unit) {
        return new RecipeIngredient(0, name, quantity, unit);
    }
}