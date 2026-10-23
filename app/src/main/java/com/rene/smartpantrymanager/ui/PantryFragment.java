package com.rene.smartpantrymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rene.smartpantrymanager.R;
import com.rene.smartpantrymanager.data.AppDatabase;
import com.rene.smartpantrymanager.data.PantryItem;

import java.util.List;

/** Tab 1: shows every pantry item from the database in a RecyclerView. */
public class PantryFragment extends Fragment implements PantryAdapter.Listener {

    private PantryAdapter adapter;
    private TextView emptyText;
    private AppDatabase db;

    public PantryFragment() {
        super(R.layout.fragment_pantry);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = AppDatabase.getInstance(requireContext());

        RecyclerView recycler = view.findViewById(R.id.recycler_pantry);
        emptyText = view.findViewById(R.id.text_empty);
        FloatingActionButton fab = view.findViewById(R.id.fab_add);

        adapter = new PantryAdapter(this);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        // Placeholder: replaced by the Add/Edit screen in the next step
        fab.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddEditActivity.class)));
    }

    // onResume runs every time the tab becomes visible, so the list is always fresh
    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        List<PantryItem> items = db.pantryItemDao().getAll();
        adapter.setItems(items);
        emptyText.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(PantryItem item) {
        Intent intent = new Intent(requireContext(), AddEditActivity.class);
        intent.putExtra(AddEditActivity.EXTRA_ITEM_ID, item.id);   // pass data to the next screen
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete ingredient")
                .setMessage("Remove " + item.name + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.pantryItemDao().delete(item);
                    loadItems();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}