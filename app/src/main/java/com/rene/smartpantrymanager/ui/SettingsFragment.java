package com.rene.smartpantrymanager.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.rene.smartpantrymanager.R;

/** Tab 3: app preferences, stored in SharedPreferences so they persist. */
public class SettingsFragment extends Fragment {

    public static final String PREFS = "smart_pantry_prefs";
    public static final String KEY_EXPIRY_ALERTS = "expiry_alerts";

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        SwitchMaterial expirySwitch = view.findViewById(R.id.switch_expiry);
        expirySwitch.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, true)); // default: on

        expirySwitch.setOnCheckedChangeListener((button, isChecked) -> {
            prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply();
            Toast.makeText(requireContext(),
                    isChecked ? "Expiry highlights on" : "Expiry highlights off",
                    Toast.LENGTH_SHORT).show();
        });
    }
}