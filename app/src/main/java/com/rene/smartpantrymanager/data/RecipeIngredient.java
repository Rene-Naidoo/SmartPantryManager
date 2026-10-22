package com.rene.smartpantrymanager.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/** One ingredient line of a recipe, linked to its recipe by recipeId. */
@Entity(
        tableName = "recipe_ingredients",
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("recipeId")})
public class RecipeIngredient {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int recipeId;      // which recipe this line belongs to
    public String name;       // singular, lowercase, e.g. "tomato"
    public double quantity;   // amount required
    public String unit;       // "g", "ml", "pcs", "tsp" or "tbsp"

    public RecipeIngredient(int recipeId, String name, double quantity, String unit) {
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }
}