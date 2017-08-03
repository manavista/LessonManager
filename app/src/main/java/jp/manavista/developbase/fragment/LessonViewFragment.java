package jp.manavista.developbase.fragment;

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
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import jp.manavista.developbase.R;
import jp.manavista.developbase.dto.TimetableDto;
import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.injector.DependencyInjector;
import jp.manavista.developbase.service.TimetableService;
import jp.manavista.developbase.util.DateTimeUtil;
import jp.manavista.developbase.view.week.DateTimeInterpreter;
import jp.manavista.developbase.view.week.LessonView;
import jp.manavista.developbase.view.week.MonthLoader;
import jp.manavista.developbase.view.week.WeekView;
import jp.manavista.developbase.view.week.WeekViewEvent;

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

    /** LessonView */
    private LessonView lessonView;
    /** RootView */
    private View rootView;
    /** Timetable DTO List */
    private List<TimetableDto> timetableList;

    /** Shared preferences */
    @Inject
    SharedPreferences sharedPreferences;
    /** Timetable service */
    @Inject
    TimetableService timetableService;

    /** Timetable list disposable */
    private Disposable timetableDisposable = Disposables.empty();


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
     * @return {@code LessonViewFragment} object
     */
    public static LessonViewFragment newInstance() {
        return new LessonViewFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_lesson_view, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        DependencyInjector.appComponent().inject(this);

        lessonView = rootView.findViewById(R.id.weekView);
        lessonView.setOnEventClickListener(this);
        lessonView.setMonthChangeListener(this);
        lessonView.setEventLongPressListener(this);
        lessonView.setEmptyViewLongPressListener(this);
        lessonView.setDateTimeInterpreter(this);

        timetableList = new ArrayList<>();

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 10);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, newMonth-1);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 17);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 18);
        endTime.set(Calendar.MINUTE, 20);
        endTime.set(Calendar.MONTH, newMonth-1);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 4);
        startTime.set(Calendar.MINUTE, 20);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 5);
        endTime.set(Calendar.MINUTE, 0);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth-1);
        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        startTime.add(Calendar.DATE, 1);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 15);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_04));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);

        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

    @Override
    public String interpretDate(Calendar date) {
        return DateTimeUtil.DATE_FORMAT_MMDDE.format(date.getTime());
    }

    @Override
    public String interpretTime(int hour) {
        return hour + ":00";
    }

    @Override
    public void onResume() {

        super.onResume();

        int startHour = sharedPreferences.getInt(getString(R.string.preferences_key_display_start_time), 9);
        int endHour = sharedPreferences.getInt(getString(R.string.preferences_key_display_end_time), 21);
        lessonView.setLimitTime(startHour, endHour);

        Log.d(TAG, "set limit time start: " + startHour + " end: " + endHour);


        timetableList.clear();

        timetableDisposable = timetableService.getListAll().subscribe(new Consumer<Timetable>() {
            @Override
            public void accept(@NonNull Timetable timetable) throws Exception {
                timetableList.add(TimetableDto.copy(timetable));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                /* no description */
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                lessonView.setLessonTableList(timetableList);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timetableDisposable.dispose();
    }

    /**
     *
     * Change Visible Days
     *
     * <p>
     * Overview:<br>
     * Change the number of days displayed in the schedule
     * to the number of days set in the argument.
     * </p>
     *
     * @param days Visible days
     */
    public void changeVisibleDays(int days) {
        lessonView.setNumberOfVisibleDays(days);
    }

    /**
     *
     * Go to Today
     *
     * <p>
     * Overview:<br>
     * Scroll the screen to show today's schedule.
     * </p>
     */
    public void goToday() {
        lessonView.goToToday();
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }


}
