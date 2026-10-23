package com.rene.smartpantrymanager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rene.smartpantrymanager.R;
import com.rene.smartpantrymanager.data.RecipeWithIngredients;

import java.util.ArrayList;
import java.util.List;

/** Custom Adapter that shows one recipe per row. */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public interface Listener {
        void onRecipeClick(RecipeWithIngredients recipe);
    }

    private final List<RecipeWithIngredients> recipes = new ArrayList<>();
    private final Listener listener;

    public RecipeAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<RecipeWithIngredients> newRecipes) {
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeWithIngredients item = recipes.get(position);
        holder.name.setText(item.recipe.name);
        int count = item.ingredients == null ? 0 : item.ingredients.size();
        holder.summary.setText(count + " ingredients - tap to view the method");
        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(item));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, summary;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_recipe_name);
            summary = itemView.findViewById(R.id.text_recipe_summary);
        }
    }
}