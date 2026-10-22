package com.rene.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rene.smartpantrymanager.data.AppDatabase;
import com.rene.smartpantrymanager.data.RecipeSeeder;
import com.rene.smartpantrymanager.ui.PantryFragment;
import com.rene.smartpantrymanager.ui.RecipesFragment;
import com.rene.smartpantrymanager.ui.SettingsFragment;

/**
 * Host activity: holds the bottom navigation bar and swaps the
 * Pantry / Recipes / Settings fragments in and out.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Insert the starter recipes the first time the app runs
        RecipeSeeder.seedIfEmpty(AppDatabase.getInstance(this));

        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.nav_recipes) {
                fragment = new RecipesFragment();
            } else if (id == R.id.nav_settings) {
                fragment = new SettingsFragment();
            } else {
                fragment = new PantryFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        });

        // Show the Pantry tab on first launch only (not after screen rotation)
        if (savedInstanceState == null) {
            nav.setSelectedItemId(R.id.nav_pantry);
        }
    }
}