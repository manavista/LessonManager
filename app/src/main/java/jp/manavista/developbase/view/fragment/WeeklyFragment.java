package jp.manavista.developbase.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

import javax.inject.Inject;

import jp.manavista.developbase.ManavistaApplication;
import jp.manavista.developbase.R;
import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.service.TimetableService;
import jp.manavista.developbase.view.activity.MainActivity;

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

    private static final String TAG = WeeklyFragment.class.getSimpleName();

    public static final String CALENDAR_OFFSET_WEEK_PROP = "calendarOffsetWeek";
    public static final String DISPLAY_DAYS_PROP = "displayDays";
    public static final String DISPLAY_DAYS_OF_WEEK_PROP = "displayDaysOfWeek";

//    TimetableService timetableService;

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

        buildContents(tableLayout, contents, rootView);

        return rootView;
    }

    /**
     *
     * Build Day Header
     *
     * <p>
     * Overview:<br>
     * Generate the date part of the header.
     * </p>
     *
     * @param context Context
     * @param displayDays display days array
     * @return TableRow Header
     */
    private TableRow buildDayHeader(Context context, String[] displayDays) {

        TableRow tableRow = new TableRow(context);

        TextView originDay = new TextView(context);

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

    /**
     *
     * Build Days of week Header
     *
     * <p>
     * Overview:<br>
     * Generate the days of week part of the header.
     * </p>
     *
     * @param context Context
     * @param displayDays display days of week array
     * @return TableRow Header
     */
    private TableRow buildDaysOfWeekHeader(Context context, String[] displayDays) {

        TableRow tableRow = new TableRow(context);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        tableRow.setLayoutParams(params);

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

    private void buildContents(TableLayout layout, Context context, View rootView) {

        // TODO: First, create timetable structure.


//        for( Timetable row : timetableService.getTimetablesAll() ) {
//            Log.d(TAG, "row id: " + row.id + " lessonNo: " +
//                    row.lessonNo + " start time: " + row.startTime + " end time: " + row.endTime);
//        }


        TableRow row = new TableRow(context);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        row.setBackgroundResource(R.color.lightGray);

        // add row to timetable

        RelativeLayout timetableLayout = new RelativeLayout(context);
        timetableLayout.setLayoutParams(tableRowParams);

        final int margin4dp = getResources().getDimensionPixelSize(R.dimen.margin_4dp);

        TextView begin = new TextView(context);
        begin.setPadding(0, margin4dp, 0, 0);

        begin.setText("00:00");
        timetableLayout.addView(begin);

        TextView lessonNo = new TextView(context);
        RelativeLayout.LayoutParams lessonNoParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lessonNoParams.addRule(RelativeLayout.CENTER_HORIZONTAL, lessonNo.getId());
        lessonNoParams.addRule(RelativeLayout.CENTER_VERTICAL, lessonNo.getId());
        lessonNo.setLayoutParams(lessonNoParams);
        lessonNo.setText("1");
        lessonNo.setTextColor(ContextCompat.getColor(context, R.color.firebrick));
        lessonNo.setTypeface(lessonNo.getTypeface(), Typeface.BOLD);
        timetableLayout.addView(lessonNo);

        TextView end = new TextView(context);
        RelativeLayout.LayoutParams endParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        endParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, end.getId());
        endParams.addRule(RelativeLayout.ALIGN_PARENT_START, end.getId());

        end.setPadding(0, 0, 0, margin4dp);
        end.setText("99:99");
        end.setLayoutParams(endParams);
        timetableLayout.addView(end);

        row.addView(timetableLayout);

        // add row to lesson data (loop)



        // add row to null value
        TextView nullDay = new TextView(context);
        row.addView(nullDay);

        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
        tableLayoutParams.setMargins(0, margin4dp, 0, margin4dp);

        layout.addView(row, tableLayoutParams);
    }
}
