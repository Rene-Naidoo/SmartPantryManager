package com.rene.smartpantrymanager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rene.smartpantrymanager.R;
import com.rene.smartpantrymanager.data.PantryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom RecyclerView Adapter: turns each PantryItem into one row on screen.
 */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    /** The fragment implements this so the adapter can report taps. */
    public interface Listener {
        void onItemClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    /** Replaces the list contents and redraws the rows. */
    public void setItems(List<PantryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    // Called when RecyclerView needs a new row view: inflate the layout once
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    // Called for each visible row: fill the views with that item's data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.name.setText(item.name);

        // Show whole numbers without ".0" (3 instead of 3.0)
        String qty = (item.quantity == Math.floor(item.quantity))
                ? String.valueOf((long) item.quantity)
                : String.valueOf(item.quantity);
        holder.quantity.setText(qty + " " + item.unit);

        if (item.expiryDate == null || item.expiryDate.isEmpty()) {
            holder.expiry.setText("No expiry date");
        } else {
            holder.expiry.setText("Expires: " + item.expiryDate);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.delete.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Holds references to one row's views so they aren't looked up again and again. */
    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, quantity, expiry;
        final ImageButton delete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            quantity = itemView.findViewById(R.id.text_quantity);
            expiry = itemView.findViewById(R.id.text_expiry);
            delete = itemView.findViewById(R.id.button_delete);
        }
    }
}