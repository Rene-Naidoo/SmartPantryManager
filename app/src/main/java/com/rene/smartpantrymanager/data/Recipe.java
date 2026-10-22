package com.rene.smartpantrymanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** One recipe: a name plus the preparation steps. Ingredients live in their own table. */
@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String steps;

    public Recipe(String name, String steps) {
        this.name = name;
        this.steps = steps;
    }
}