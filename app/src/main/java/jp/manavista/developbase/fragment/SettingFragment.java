package jp.manavista.developbase.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import org.apache.commons.lang3.StringUtils;

import java.util.TreeSet;

import jp.manavista.developbase.R;

/**
 *
 * Setting Fragment
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class SettingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    /** preference key: start view mode */
    public static final String KEY_START_VIEW = "start_view";
    /** preference key: display day of week */
    public static final String KEY_DISPLAY_DAY_OF_WEEK = "display_day_of_week";
    /** preference key: first day of week */
    public static final String KEY_FIRST_DAY_OF_WEEK = "first_day_of_week";

    // TODO: Delete old preferences key.
    // TODO: Add Lesson View preferences. start time, end time, and first day (or today?)

    private static final String TIME_FORMAT_HHMM = "%02d:%02d";

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
    }

    @Override
    public void onStart() {
        super.onStart();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.action_settings);
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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

        ListAdapter listAdapter  = getPreferenceScreen().getRootAdapter();
        for( int i = 0 ; i < listAdapter.getCount() ; i++ ) {

            final Object item = listAdapter.getItem(i);

            if (item instanceof ListPreference) {

                ListPreference preference = (ListPreference) item;
                preference.setSummary(preference.getEntry() == null ? StringUtils.EMPTY : preference.getEntry());

            } else if (item instanceof MultiSelectListPreference) {

                MultiSelectListPreference preference = (MultiSelectListPreference) item;
                CharSequence[] entries = preference.getEntries();
                StringBuilder sb = new StringBuilder();

                for (String value : new TreeSet<>(preference.getValues())) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(entries[Integer.valueOf(value) - 1]);
                }

                final String noData = getResources().getString(R.string.summary_no_data);
                preference.setSummary(preference.getValues().size() == 0 ? noData : sb.toString());
            }
        }
    }
}
