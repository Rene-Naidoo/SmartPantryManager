package com.rene.smartpantrymanager.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for pantry items.
 * Each method below is one CRUD operation.
 */
@Dao
public interface PantryItemDao {

    // CREATE: add a new item and return its generated id
    @Insert
    long insert(PantryItem item);

    // READ: get every item, sorted alphabetically by name
    @Query("SELECT * FROM pantry_items ORDER BY name ASC")
    List<PantryItem> getAll();

    // READ: get one item by id (used when editing)
    @Query("SELECT * FROM pantry_items WHERE id = :id")
    PantryItem getById(int id);

    // UPDATE: save changes to an existing item (matched by primary key)
    @Update
    void update(PantryItem item);

    // DELETE: remove an item
    @Delete
    void delete(PantryItem item);
}