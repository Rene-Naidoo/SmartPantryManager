package com.rene.smartpantrymanager.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/** Not a table. Room fills it by joining a recipe with its ingredients. */
public class RecipeWithIngredients {

    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipeId")
    public List<RecipeIngredient> ingredients;
}