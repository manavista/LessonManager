package jp.manavista.developbase.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.ListAdapter;

import java.util.TreeSet;

import jp.manavista.developbase.R;

public class SettingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_START_VIEW = "start_view";

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
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

    private void updateSummary() {

        ListAdapter listAdapter  = getPreferenceScreen().getRootAdapter();
        for( int i = 0 ; i < listAdapter.getCount() ; i++ ) {

            Object item = listAdapter.getItem(i);

            if (item instanceof ListPreference) {

                ListPreference preference = (ListPreference) item;
                preference.setSummary(preference.getEntry() == null ? "" : preference.getEntry());

            } else if (item instanceof MultiSelectListPreference) {

                MultiSelectListPreference preference = (MultiSelectListPreference) item;
                CharSequence[] entries = preference.getEntries();
                StringBuilder sb = new StringBuilder();

                for (String value : new TreeSet<>(preference.getValues())) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(entries[Integer.valueOf(value)]);
                }

                preference.setSummary(preference.getValues().size() == 0 ? "no data" : sb.toString());
            }
        }
    }
}
