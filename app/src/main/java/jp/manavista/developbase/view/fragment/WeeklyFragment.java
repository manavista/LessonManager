package jp.manavista.developbase.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

import jp.manavista.developbase.R;

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
    public static final String DISPLAY_DAYS_PROP = "displayDays";
    public static final String DISPLAY_DAYS_OF_WEEK_PROP = "displayDaysOfWeek";

    /** Default Constructor */
    public WeeklyFragment() {
        // Every fragment must have an empty constructor.
        // https://developer.android.com/reference/android/app/Fragment.html#Fragment()
    }

    /**
     *
     * New Instance
     *
     * <p>
     * Overview:<br>
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * </p>
     *
     * @param offset Calendar offset of offset.
     * @return A new instance of fragment WeeklyFragment.
     */
    public static WeeklyFragment newInstance(int offset, String[] displayDays, String[] displayDaysOfWeek) {

        WeeklyFragment weeklyFragment = new WeeklyFragment();
        Bundle args = new Bundle();

        args.putInt(CALENDAR_OFFSET_WEEK_PROP, offset);
        args.putStringArray(DISPLAY_DAYS_PROP, displayDays);
        args.putStringArray(DISPLAY_DAYS_OF_WEEK_PROP, displayDaysOfWeek);

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
        View rootView = inflater.inflate(R.layout.fragment_weekly, container, false);
        Bundle args = getArguments();

        String[] displayDays = args.getStringArray(DISPLAY_DAYS_PROP);
        String[] displayDaysOfWeek = args.getStringArray(DISPLAY_DAYS_OF_WEEK_PROP);

        Activity contents = getActivity();

        TableLayout tableLayout = rootView.findViewById(R.id.weekly);

        TableRow row = buildDayHeader(contents, displayDays);
        TableRow row2 = buildDaysOfWeekHeader(contents, displayDaysOfWeek);
        tableLayout.addView(row);
        tableLayout.addView(row2);

        return rootView;
    }

    private TableRow buildDayHeader(Context context, String[] displayDays) {

        TableRow tableRow = new TableRow(context);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(params);

        TextView originDay = new TextView(context);

        /*
         * Do not set layout params.
         * If params sets, do not show TextView. I do not know this reason.
         */
//        ViewGroup.LayoutParams originDayParams = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        originDay.setLayoutParams(originDayParams);

        tableRow.addView(originDay);

        for( String day : displayDays ) {

            TextView view = new TextView(context);
            view.setText(day);
            view.setGravity(Gravity.CENTER);
            tableRow.addView(view);
        }

        TextView nullDay = new TextView(context);
        tableRow.addView(nullDay);

        return tableRow;
    }

    private TableRow buildDaysOfWeekHeader(Context context, String[] displayDays) {

        TableRow tableRow = new TableRow(context);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(params);

        TextView originDay = new TextView(context);
        tableRow.addView(originDay);

        String[] dayValuesOfWeek = getResources().getStringArray(R.array.entry_values_day_of_week);
        String[] dayEntriesOnWeek = getResources().getStringArray(R.array.entries_day_of_week);

        for( String day : displayDays ) {

            final int index = ArrayUtils.indexOf(dayValuesOfWeek, day);
            final String dayOfWeek = String.format("(%s)", dayEntriesOnWeek[index]) ;

            TextView cell = new TextView(context);
            cell.setText(dayOfWeek);
            cell.setGravity(Gravity.CENTER);
            tableRow.addView(cell);
        }

        TextView nullDay = new TextView(context);
        nullDay.setLayoutParams(params);
        tableRow.addView(nullDay);

        return tableRow;
    }
}
