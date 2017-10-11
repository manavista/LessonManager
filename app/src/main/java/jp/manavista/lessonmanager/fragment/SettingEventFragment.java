/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.ListAdapter;

import jp.manavista.lessonmanager.R;
import lombok.val;

/**
 *
 * SettingEventFragment
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public final class SettingEventFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public SettingEventFragment() {
    }

    public static SettingEventFragment newInstance() {
        return new SettingEventFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_event);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        updateSummary();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        updateSummary();
    }


    /**
     *
     * Update Preference Summary
     *
     * <p>
     * Overview:<br>
     * Update all summary string.<br>
     * Dynamically change when setting is start or update.
     * </p>
     */
    private void updateSummary() {

        final ListAdapter listAdapter  = getPreferenceScreen().getRootAdapter();
        for( int i = 0 ; i < listAdapter.getCount() ; i++ ) {

            final Object item = listAdapter.getItem(i);

            if (item instanceof CheckBoxPreference) {
                final val preferences = (CheckBoxPreference) item;
                preferences.setSummary( preferences.isChecked() ? "ON":"OFF");
            }

        }
    }
}
