package jp.manavista.developbase.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import jp.manavista.developbase.R;
import jp.manavista.developbase.util.DateUtil;

/**
 *
 * Weekly Fragment
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public final class WeeklyFragment extends Fragment {

    public static final String CALENDAR_OFFSET_WEEK_PROP = "calendarOffsetWeek";

    /** Default Constructor */
    public WeeklyFragment() {
        // Every fragment must have an empty constructor.
        // https://developer.android.com/reference/android/app/Fragment.html#Fragment()
    }

    public static WeeklyFragment newInstance(int offset) {

        WeeklyFragment weeklyFragment = new WeeklyFragment();
        Bundle args = new Bundle();
        args.putInt(CALENDAR_OFFSET_WEEK_PROP, offset);
        weeklyFragment.setArguments(args);

        return weeklyFragment;
    }

    @Nullable
    @Override
    public View onCreateView (
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_weekly, container, false);
    }
}
