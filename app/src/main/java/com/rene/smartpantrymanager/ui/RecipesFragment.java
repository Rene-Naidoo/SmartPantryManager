package com.rene.smartpantrymanager.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rene.smartpantrymanager.R;
import com.rene.smartpantrymanager.data.AppDatabase;
import com.rene.smartpantrymanager.data.PantryItem;
import com.rene.smartpantrymanager.data.RecipeWithIngredients;
import com.rene.smartpantrymanager.logic.RecipeMatcher;

import java.util.List;

/** Tab 2: lists ONLY the recipes whose every ingredient is in the pantry (strict rule). */
public class RecipesFragment extends Fragment implements RecipeAdapter.Listener {

    private RecipeAdapter adapter;
    private TextView noMatchText;
    private AppDatabase db;

    public RecipesFragment() {
        super(R.layout.fragment_recipes);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = AppDatabase.getInstance(requireContext());

        RecyclerView recycler = view.findViewById(R.id.recycler_recipes);
        noMatchText = view.findViewById(R.id.text_no_match);

        adapter = new RecipeAdapter(this);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);
    }

    // Re-run the matching every time the tab is shown, so it reflects the current pantry
    @Override
    public void onResume() {
        super.onResume();
        loadSuggestions();
    }

    private void loadSuggestions() {
        List<PantryItem> pantry = db.pantryItemDao().getAll();
        List<RecipeWithIngredients> allRecipes = db.recipeDao().getAllWithIngredients();

        // The strict-matching rule is applied here
        List<RecipeWithIngredients> matches = RecipeMatcher.findStrictMatches(pantry, allRecipes);

        adapter.setRecipes(matches);
        noMatchText.setVisibility(matches.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRecipeClick(RecipeWithIngredients recipe) {
        // Placeholder: opens the Recipe Detail screen in the next step
        Toast.makeText(requireContext(), recipe.recipe.name, Toast.LENGTH_SHORT).show();
    }
}