package jp.manavista.lessonmanager.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.ListAdapter;

import java.util.Set;

import jp.manavista.lessonmanager.R;
import lombok.val;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public final class SettingLessonAndScheduleFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public SettingLessonAndScheduleFragment() {
    }

    public static SettingLessonAndScheduleFragment newInstance() {
        final Bundle args = new Bundle();
        final SettingLessonAndScheduleFragment fragment = new SettingLessonAndScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_lesson_and_schedule);
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
        updateSummary();
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

            if ( item instanceof CheckBoxPreference ) {

                final val preferences = (CheckBoxPreference) item;
                preferences.setSummary( preferences.isChecked() ? "ON":"OFF");

            } else if( item instanceof MultiSelectListPreference ) {

                final val preference = (MultiSelectListPreference) item;
                final CharSequence[] entries = preference.getEntries();
                final CharSequence[] entryValues = preference.getEntryValues();
                final StringBuilder sb = new StringBuilder();
                final Set<String> result = preference.getValues();

                for( int j = 0 ; j < entries.length ; j++ ) {

                    if( result.contains(String.valueOf(entryValues[j])) ) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(entries[j]);
                    }
                }

                preference.setSummary(sb.length() == 0 ? "Not Selected" : sb.toString());
            }
        }
    }
}
