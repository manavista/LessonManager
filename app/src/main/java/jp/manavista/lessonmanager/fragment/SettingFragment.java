package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.Locale;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.activity.SettingActivity;
import jp.manavista.lessonmanager.view.preference.NumberPickerPreference;

/**
 *
 * Setting Fragment
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public final class SettingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TIME_FORMAT_HMM = "%01d:00";

    /** Activity Contents */
    private Activity contents;

    /**
     *
     * New Instance
     *
     * <p>
     * Overview:<br>
     * Create new {@code SettingFragment} object.
     * </p>
     *
     * @return {@code SettingFragment} object
     */
    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final Preference preference = findPreference(getString(R.string.preferences_key_delete_using_data));
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final Intent intent = new Intent(contents, SettingActivity.class);
                intent.putExtra(SettingActivity.EXTRA_FRAGMENT_TYPE, SettingActivity.FragmentType.DELETE);
                contents.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contents = getActivity();
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
                .unregisterOnSharedPreferenceChangeListener(this);
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

            if (item instanceof ListPreference) {

                final ListPreference preference = (ListPreference) item;
                final String summary = preference.getSummary().toString();
                preference.setSummary(preference.getEntry() == null ? summary : preference.getEntry());

            } else if( item instanceof NumberPickerPreference ) {
                NumberPickerPreference preference = (NumberPickerPreference) item;
                preference.setSummary(String.format(Locale.getDefault(), TIME_FORMAT_HMM, preference.getSelectedValue()));
            }
        }
    }
}
