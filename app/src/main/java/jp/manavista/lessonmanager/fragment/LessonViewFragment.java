package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.activity.EventActivity;
import jp.manavista.lessonmanager.activity.MemberLessonScheduleActivity;
import jp.manavista.lessonmanager.facade.LessonViewFacade;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import jp.manavista.lessonmanager.view.week.DateTimeInterpreter;
import jp.manavista.lessonmanager.view.week.LessonView;
import jp.manavista.lessonmanager.view.week.MonthLoader;
import jp.manavista.lessonmanager.view.week.WeekView;
import jp.manavista.lessonmanager.view.week.WeekViewEvent;
import lombok.Getter;
import lombok.val;

import static jp.manavista.lessonmanager.constants.MemberLessonScheduleStatus.ABSENT;
import static jp.manavista.lessonmanager.constants.MemberLessonScheduleStatus.DONE;
import static jp.manavista.lessonmanager.constants.MemberLessonScheduleStatus.SCHEDULED;

/**
 *
 * LessonView fragment
 *
 * <p>
 * Overview:<br>
 * LessonView control fragment.<br>
 * Handing lesson view days, timetable, and other events.
 * </p>
 */
public final class LessonViewFragment extends Fragment implements
        WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener,
        WeekView.EmptyViewLongPressListener, DateTimeInterpreter {

    /** Logger Tag string  */
    private static final String TAG = LessonViewFragment.class.getSimpleName();
    /** bundle key: Visible Days */
    public static final String KEY_VISIBLE_DAYS = "VISIBLE_DAYS";

    /** LessonView */
    @Getter
    private LessonView lessonView;
    /** RootView */
    private View rootView;
    /** Start Visible Days */
    private int visibleDays;
    /** Timetable DTO List */
    private List<TimetableDto> timetableList;
    /** Schedule (Lesson and Event) List */
    private List<WeekViewEvent> scheduleList;

    /** Activity Contents */
    private Activity contents;

    /** Shared preferences */
    @Inject
    SharedPreferences sharedPreferences;
    /** Timetable service */
    @Inject
    LessonViewFacade facade;

    /** disposable */
    private Disposable disposable;


    /** Constructor */
    public LessonViewFragment() {
        // Required empty public constructor
    }

    /**
     *
     * New Instance
     *
     * <p>
     * Overview:<br>
     * Create new {@code LessonViewFragment} object.
     * </p>
     *
     * @param visibleDays display week days
     * @return {@code LessonViewFragment} object
     */
    public static LessonViewFragment newInstance(final int visibleDays) {
        final LessonViewFragment fragment = new LessonViewFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_VISIBLE_DAYS, visibleDays);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        visibleDays = args.getInt(KEY_VISIBLE_DAYS, 3);

        disposable = Disposables.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_lesson_view, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        this.contents = getActivity();
        DependencyInjector.appComponent().inject(this);

        lessonView = rootView.findViewById(R.id.weekView);
        lessonView.setNumberOfVisibleDays(visibleDays);
        lessonView.setOnEventClickListener(this);
        lessonView.setMonthChangeListener(this);
        lessonView.setEventLongPressListener(this);
        lessonView.setEmptyViewLongPressListener(this);
        lessonView.setDateTimeInterpreter(this);

        timetableList = new ArrayList<>();
        scheduleList = new ArrayList<>();
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        final List<WeekViewEvent> events = new ArrayList<>();

        for( val lesson : scheduleList ) {
            if( isMatched(lesson.getStartTime(), newYear, newMonth) ) {
                events.add(lesson);
            }
        }

        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

        final Intent intent;

        if( event.isAllDay() ) {
            intent = new Intent(contents, EventActivity.class);
            intent.putExtra(EventActivity.EXTRA_EVENT_ID, event.getId());
        } else {
            intent = new Intent(contents, MemberLessonScheduleActivity.class);
            intent.putExtra(MemberLessonScheduleActivity.EXTRA_SCHEDULE_ID, event.getId());
        }
        contents.startActivity(intent);
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

    @Override
    public String interpretDate(Calendar date) {

        /*
         * Header Date Format
         *
         * Since the display width varies depending on the number of days displayed,
         * specify the format dynamically.
         */
        switch (visibleDays) {

            case 1:
                return DateTimeUtil.DATE_FORMAT_LLLLDDEEEYYYY.format(date.getTime());
            case 3:
                return DateTimeUtil.DATE_FORMAT_LLLDDE.format(date.getTime());
            case 5:
            case 7:
                return DateTimeUtil.DATE_FORMAT_MMDD.format(date.getTime());
            default:
                return DateTimeUtil.DATE_FORMAT_MMDDE.format(date.getTime());
        }
    }

    @Override
    public String interpretTime(int hour) {
        return hour + ":00";
    }

    @Override
    public void onResume() {

        super.onResume();

        final int startHour = sharedPreferences.getInt(getString(R.string.preferences_key_display_start_time), 9);
        final int endHour = sharedPreferences.getInt(getString(R.string.preferences_key_display_end_time), 21);
        lessonView.setLimitTime(startHour, endHour);

        Log.d(TAG, "set limit time start: " + startHour + " end: " + endHour);

        /* Display status target */
        final Set<Integer> statusSet = new HashSet<>();
        statusSet.add(SCHEDULED.getId());
        statusSet.add(ABSENT.getId());
        statusSet.add(DONE.getId());

        disposable = facade.getViewData(lessonView, timetableList, scheduleList, statusSet);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    /**
     *
     * Change Visible Days
     *
     * <p>
     * Overview:<br>
     * Change the number of days displayed in the schedule
     * to the number of days set in the argument.<br>
     * After change days, keep displaying the current date.
     * </p>
     *
     * @param days Visible days
     */
    public void changeVisibleDays(int days) {
        this.visibleDays = days;
        final Calendar calendar = lessonView.getFirstVisibleDay();
        lessonView.setNumberOfVisibleDays(days);
        lessonView.goToDate(calendar);
    }

    private boolean isMatched(Calendar calendar, final int year, final int month) {
        return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month;
    }
}
