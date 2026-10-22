package com.rene.smartpantrymanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents one ingredient the user currently has at home.
 * Room turns this class into a table called "pantry_items".
 */
@Entity(tableName = "pantry_items")
public class PantryItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;        // e.g. "tomato"
    public double quantity;    // e.g. 3
    public String unit;        // e.g. "g", "ml", "pcs"
    public String expiryDate;  // optional, e.g. "2026-11-05" (may be null)

    public PantryItem(String name, double quantity, String unit, String expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }
}