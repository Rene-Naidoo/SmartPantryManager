package com.rene.smartpantrymanager.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rene.smartpantrymanager.R;
import com.rene.smartpantrymanager.data.AppDatabase;
import com.rene.smartpantrymanager.data.RecipeIngredient;
import com.rene.smartpantrymanager.data.RecipeWithIngredients;

/** Shows one recipe's full ingredient list and method. Receives the recipe id via an Intent. */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView name = findViewById(R.id.text_detail_name);
        TextView ingredients = findViewById(R.id.text_detail_ingredients);
        TextView steps = findViewById(R.id.text_detail_steps);
        Button back = findViewById(R.id.button_back);
        back.setOnClickListener(v -> finish());

        // Read the data that the previous screen put into the Intent
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        RecipeWithIngredients data = AppDatabase.getInstance(this).recipeDao().getById(recipeId);

        if (data == null) {
            name.setText("Recipe not found");
            return;
        }

        name.setText(data.recipe.name);
        steps.setText(data.recipe.steps);

        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient i : data.ingredients) {
            String qty = (i.quantity == Math.floor(i.quantity))
                    ? String.valueOf((long) i.quantity)
                    : String.valueOf(i.quantity);
            sb.append("\u2022 ").append(qty).append(" ").append(i.unit)
                    .append(" ").append(i.name).append("\n");
        }
        ingredients.setText(sb.toString().trim());
    }
}