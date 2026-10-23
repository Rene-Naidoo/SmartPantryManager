package com.rene.smartpantrymanager.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rene.smartpantrymanager.R;
import com.rene.smartpantrymanager.data.AppDatabase;
import com.rene.smartpantrymanager.data.PantryItem;

import java.util.Calendar;
import java.util.Locale;

/**
 * One screen for both adding and editing a pantry item.
 * If the Intent carries EXTRA_ITEM_ID it edits that item, otherwise it adds a new one.
 */
public class AddEditActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";
    private static final String[] UNITS = {"pcs", "g", "kg", "ml", "l", "tsp", "tbsp"};

    private AppDatabase db;
    private PantryItem existing;   // null when adding a new item

    private TextInputLayout layoutName, layoutQuantity;
    private TextInputEditText editName, editQuantity, editExpiry;
    private Spinner spinnerUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        db = AppDatabase.getInstance(this);

        layoutName = findViewById(R.id.layout_name);
        layoutQuantity = findViewById(R.id.layout_quantity);
        editName = findViewById(R.id.edit_name);
        editQuantity = findViewById(R.id.edit_quantity);
        editExpiry = findViewById(R.id.edit_expiry);
        spinnerUnit = findViewById(R.id.spinner_unit);
        TextView title = findViewById(R.id.text_title);
        Button save = findViewById(R.id.button_save);
        Button cancel = findViewById(R.id.button_cancel);
        Button clearExpiry = findViewById(R.id.button_clear_expiry);

        spinnerUnit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, UNITS));

        // Edit mode: the calling screen passed us the id of the item to edit
        int itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            existing = db.pantryItemDao().getById(itemId);
        }
        if (existing != null) {
            title.setText("Edit ingredient");
            editName.setText(existing.name);
            editQuantity.setText(formatQuantity(existing.quantity));
            editExpiry.setText(existing.expiryDate);
            for (int i = 0; i < UNITS.length; i++) {
                if (UNITS[i].equals(existing.unit)) spinnerUnit.setSelection(i);
            }
        }

        editExpiry.setOnClickListener(v -> showDatePicker());
        clearExpiry.setOnClickListener(v -> editExpiry.setText(""));
        save.setOnClickListener(v -> saveItem());
        cancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (picker, year, month, day) ->
                editExpiry.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /** Validates the form; saves only if every field is valid. */
    private void saveItem() {
        layoutName.setError(null);
        layoutQuantity.setError(null);

        String name = editName.getText() == null ? "" : editName.getText().toString().trim();
        String qtyText = editQuantity.getText() == null ? "" : editQuantity.getText().toString().trim();
        boolean valid = true;

        if (name.isEmpty()) {
            layoutName.setError("Please enter an ingredient name");
            valid = false;
        }

        double quantity = 0;
        if (qtyText.isEmpty()) {
            layoutQuantity.setError("Please enter a quantity");
            valid = false;
        } else {
            try {
                quantity = Double.parseDouble(qtyText);
                if (quantity <= 0) {
                    layoutQuantity.setError("Quantity must be greater than 0");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                layoutQuantity.setError("Enter a valid number");
                valid = false;
            }
        }

        if (!valid) return;

        String unit = (String) spinnerUnit.getSelectedItem();
        String expiry = editExpiry.getText() == null ? "" : editExpiry.getText().toString().trim();
        if (expiry.isEmpty()) expiry = null;   // expiry date is optional

        if (existing == null) {
            db.pantryItemDao().insert(new PantryItem(name, quantity, unit, expiry));   // CREATE
        } else {
            existing.name = name;
            existing.quantity = quantity;
            existing.unit = unit;
            existing.expiryDate = expiry;
            db.pantryItemDao().update(existing);                                       // UPDATE
        }
        finish();   // go back; the list refreshes in onResume
    }

    private String formatQuantity(double q) {
        return q == Math.floor(q) ? String.valueOf((long) q) : String.valueOf(q);
    }
}