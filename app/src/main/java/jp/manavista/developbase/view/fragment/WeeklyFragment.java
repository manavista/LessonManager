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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import jp.manavista.developbase.R;
import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.injector.DependencyInjector;
import jp.manavista.developbase.service.TimetableService;
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

    private static final String TAG = WeeklyFragment.class.getSimpleName();

    public static final String CALENDAR_OFFSET_WEEK_PROP = "calendarOffsetWeek";
    public static final String DISPLAY_DAYS_PROP = "displayDays";
    public static final String DISPLAY_DAYS_OF_WEEK_PROP = "displayDaysOfWeek";

    @Inject
    TimetableService timetableService;

    /** RootView object */
    private View rootView;
    /** Timetable list disposable */
    private Disposable timetableDisposable = Disposables.empty();

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DependencyInjector.appComponent().inject(this);

        Bundle args = getArguments();
        Activity contents = getActivity();

        TableLayout tableLayout = rootView.findViewById(R.id.weekly);

        String[] displayDaysOfWeek = args.getStringArray(DISPLAY_DAYS_OF_WEEK_PROP);
        if( ArrayUtils.getLength(displayDaysOfWeek) > 1 ) {
            for( int i = 1 ; i <= ArrayUtils.getLength(displayDaysOfWeek) ; i++ ) {
                tableLayout.setColumnStretchable(i, true);
            }
        }

        TableRow row = buildDayHeader(contents, args.getStringArray(DISPLAY_DAYS_PROP));
        TableRow row2 = buildDaysOfWeekHeader(contents, displayDaysOfWeek);
        tableLayout.addView(row);
        tableLayout.addView(row2);

        buildContents(tableLayout, contents, displayDaysOfWeek);
    }

    @Nullable
    @Override
    public View onCreateView (
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_weekly, container, false);

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

    /**
     *
     * Build Contents
     *
     * <p>
     * Overview:<br>
     * Generate screen content that is the center of Weekly fragment.<br>
     * Asynchronously retrieve data from the database.
     * </p>
     *
     * @param layout table layout
     * @param context context
     * @param displayDays display days code array
     */
    private void buildContents(final TableLayout layout, final Context context, final String[] displayDays) {

        timetableDisposable = timetableService.getListAll().subscribe(new Consumer<Timetable>() {
            @Override
            public void accept(@NonNull Timetable timetable) throws Exception {

                Log.d(TAG, "row id: " + timetable.id + " lessonNo: " +
                        timetable.lessonNo + " start: " + timetable.startTime + " end: " + timetable.endTime);

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

                begin.setText(DateUtil.TIME_FORMAT_HHMM.format(timetable.startTime));
                timetableLayout.addView(begin);

                TextView lessonNo = new TextView(context);
                RelativeLayout.LayoutParams lessonNoParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lessonNoParams.addRule(RelativeLayout.CENTER_HORIZONTAL, lessonNo.getId());
                lessonNoParams.addRule(RelativeLayout.CENTER_VERTICAL, lessonNo.getId());
                lessonNo.setLayoutParams(lessonNoParams);
                lessonNo.setText(String.valueOf(timetable.lessonNo));
                lessonNo.setTextColor(ContextCompat.getColor(context, R.color.firebrick));
                lessonNo.setTypeface(lessonNo.getTypeface(), Typeface.BOLD);
                timetableLayout.addView(lessonNo);

                TextView end = new TextView(context);
                RelativeLayout.LayoutParams endParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                endParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, end.getId());
                endParams.addRule(RelativeLayout.ALIGN_PARENT_START, end.getId());

                end.setPadding(0, 0, 0, margin4dp);
                end.setText(DateUtil.TIME_FORMAT_HHMM.format(timetable.endTime));
                end.setLayoutParams(endParams);
                timetableLayout.addView(end);

                row.addView(timetableLayout);

                // add row to lesson data (loop)
                buildContentsLesson(row, context, displayDays);

                // add row to null value
                TextView nullDay = new TextView(context);
                row.addView(nullDay);

                TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
                tableLayoutParams.setMargins(0, margin4dp, 0, margin4dp);

                layout.addView(row, tableLayoutParams);

            }
        });
    }

    /**
     *
     * Build Contents Lesson Data
     *
     * <p>
     * Overview:<br>
     * Generate screen content that is the lesson description.<br>
     * </p>
     *
     * @param row lesson table row
     * @param context context
     * @param displayDays display days code array
     */
    private void buildContentsLesson(TableRow row, Context context, String[] displayDays) {

        final int margin4dp = getResources().getDimensionPixelSize(R.dimen.margin_4dp);

        for ( String day : displayDays ) {

            Log.d(TAG, day);

            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

            RelativeLayout lessonLayout = new RelativeLayout(context);
            lessonLayout.setLayoutParams(tableRowParams);

            TextView subject = new TextView(context);
            RelativeLayout.LayoutParams subjectParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            subjectParams.setMargins(margin4dp, margin4dp, margin4dp, margin4dp);
            subject.setLayoutParams(subjectParams);
            subject.setBackgroundResource(R.color.lightSkyBlue);
            subject.setGravity(Gravity.CENTER);
            subject.setText("SUB");

            ImageView imageView = new ImageView(context);
            RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            imageViewParams.setMargins(margin4dp, margin4dp, margin4dp, margin4dp);
            imageViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            imageViewParams.addRule(RelativeLayout.ALIGN_PARENT_END);

            imageView.setLayoutParams(imageViewParams);
            imageView.setContentDescription(getString(R.string.text_image_member));
            imageView.setImageResource(R.drawable.ic_person_white);

            lessonLayout.addView(subject);
            lessonLayout.addView(imageView);

            row.addView(lessonLayout);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        timetableDisposable.dispose();
    }
}
