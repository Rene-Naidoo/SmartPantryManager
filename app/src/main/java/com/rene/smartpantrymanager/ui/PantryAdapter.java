package com.rene.smartpantrymanager.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rene.smartpantrymanager.R;
import com.rene.smartpantrymanager.data.PantryItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        // Reset the colour first, because RecyclerView reuses row views
        holder.expiry.setTextColor(holder.defaultExpiryColor);

        if (item.expiryDate == null || item.expiryDate.isEmpty()) {
            holder.expiry.setText("No expiry date");
        } else {
            SharedPreferences prefs = holder.itemView.getContext()
                    .getSharedPreferences(SettingsFragment.PREFS, Context.MODE_PRIVATE);
            boolean alertsOn = prefs.getBoolean(SettingsFragment.KEY_EXPIRY_ALERTS, true);

            if (alertsOn && isExpiringSoon(item.expiryDate)) {
                holder.expiry.setText("Expiring soon: " + item.expiryDate);
                holder.expiry.setTextColor(Color.RED);
            } else {
                holder.expiry.setText("Expires: " + item.expiryDate);
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.delete.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    /** True if the date (yyyy-MM-dd) is today, in the past, or within the next 3 days. */
    private boolean isExpiringSoon(String date) {
        try {
            Date expiry = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            if (expiry == null) return false;
            long dayMillis = 24L * 60 * 60 * 1000;
            return (expiry.getTime() - System.currentTimeMillis()) / dayMillis <= 3;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Holds references to one row's views so they aren't looked up again and again. */
    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, quantity, expiry;
        final ImageButton delete;
        final int defaultExpiryColor;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            quantity = itemView.findViewById(R.id.text_quantity);
            expiry = itemView.findViewById(R.id.text_expiry);
            delete = itemView.findViewById(R.id.button_delete);
            defaultExpiryColor = expiry.getCurrentTextColor();
        }
    }
}