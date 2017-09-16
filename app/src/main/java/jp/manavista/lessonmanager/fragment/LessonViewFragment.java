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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.activity.EventActivity;
import jp.manavista.lessonmanager.activity.MemberLessonScheduleActivity;
import jp.manavista.lessonmanager.constants.MemberLessonScheduleStatus;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
import jp.manavista.lessonmanager.model.entity.Timetable;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.service.EventService;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.TimetableService;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import jp.manavista.lessonmanager.view.week.DateTimeInterpreter;
import jp.manavista.lessonmanager.view.week.LessonView;
import jp.manavista.lessonmanager.view.week.MonthLoader;
import jp.manavista.lessonmanager.view.week.WeekView;
import jp.manavista.lessonmanager.view.week.WeekViewEvent;
import lombok.Getter;
import lombok.val;

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
    /** Lesson List */
    private List<WeekViewEvent> lessonList;
    /** Event List */
    private List<WeekViewEvent> eventList;

    /** Activity Contents */
    private Activity contents;

    /** Shared preferences */
    @Inject
    SharedPreferences sharedPreferences;
    /** Timetable service */
    @Inject
    TimetableService timetableService;
    @Inject
    EventService eventService;
    @Inject
    MemberLessonScheduleService memberLessonScheduleService;

    /** disposable */
    private Disposable timetableDisposable;
    private Disposable scheduleDisposable;
    private Disposable eventDisposable;


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

        timetableDisposable = Disposables.empty();
        scheduleDisposable = Disposables.empty();
        eventDisposable = Disposables.empty();
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
        lessonList = new ArrayList<>();
        eventList = new ArrayList<>();
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        final List<WeekViewEvent> events = new ArrayList<>();

        for( val lesson : lessonList ) {
            if( isMatched(lesson.getStartTime(), newYear, newMonth) ) {
                events.add(lesson);
            }
        }

        for( val event : eventList ) {
            if(  isMatched(event.getStartTime(), newYear, newMonth)) {
                events.add(event);
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
        return DateTimeUtil.DATE_FORMAT_MMDDE.format(date.getTime());
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


        timetableList.clear();

        // TODO: 2017/09/16 If timetable delete, remain except draw hour. add If 0 row

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
                buildEvents();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timetableDisposable.dispose();
        scheduleDisposable.dispose();
        eventDisposable.dispose();
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
        final Calendar calendar = lessonView.getFirstVisibleDay();
        lessonView.setNumberOfVisibleDays(days);
        lessonView.goToDate(calendar);
    }

    private boolean isMatched(Calendar calendar, final int year, final int month) {
        return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month;
    }

    /**
     *
     * Build Events
     *
     * <p>
     * Overview:<br>
     * Reads the lesson schedule from the database,
     * and creates event objects and lists to be displayed on the screen.
     * </p>
     * <p><b>Type of schedule to display:</b></p>
     * <ul>
     * <li>SCHEDULED - Normal display</li>
     * <li>ABSENT - Display with special mark</li>
     * <li>SUSPENDED - Don't display</li>
     * <li>DONE - Display with high transparency</li>
     * </ul>
     */
    private void buildEvents() {

        lessonList.clear();

        /* Do not create a new instance inside the loop. */
        final StringBuilder builder = new StringBuilder();

        scheduleDisposable = memberLessonScheduleService.getVoListByExcludeStatus(MemberLessonScheduleStatus.SUSPENDED.getId()).map(new Function<MemberLessonScheduleVo, WeekViewEvent>() {
            @Override
            public WeekViewEvent apply(@NonNull MemberLessonScheduleVo vo) throws Exception {

                final WeekViewEvent lesson = new WeekViewEvent(
                        vo.getId(),
                        buildEventName(vo, builder),
                        vo.getLocation(),
                        vo.getLessonStartCalendar(),
                        vo.getLessonEndCalendar());
                lesson.setColor(vo.getLessonViewColor());

                return lesson;
            }
        }).subscribe(new Consumer<WeekViewEvent>() {
            @Override
            public void accept(WeekViewEvent weekViewEvent) throws Exception {
                lessonList.add(weekViewEvent);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throw new RuntimeException(throwable);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                lessonView.notifyDatasetChanged();
            }
        });


        eventList.clear();

        eventDisposable = eventService.getEventListAll().subscribe(new Consumer<WeekViewEvent>() {
            @Override
            public void accept(WeekViewEvent weekViewEvent) throws Exception {
                eventList.add(weekViewEvent);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                lessonView.notifyDatasetChanged();
            }
        });
    }

    private String buildEventName(MemberLessonScheduleVo vo, StringBuilder builder) {

        builder.setLength(0);

        /* If the lesson start time is the same as timetable, display is omitted. */
        boolean addStartTime = true;
        for( val timetable : timetableList ) {
            if( timetable.getStartTimeFormatted().equals(vo.getLessonStartTimeFormatted()) ) {
                addStartTime = false;
                break;
            }
        }

        builder.append(addStartTime ? vo.getLessonStartTimeFormatted() : StringUtils.EMPTY)
                .append(addStartTime ? " " : StringUtils.EMPTY)
                .append(StringUtils.isEmpty(vo.getAbbr()) ? vo.getName() : vo.getAbbr())
                .append("/")
                .append(StringUtils.isEmpty(vo.getMember().nickName)
                        ? vo.getMember().givenName
                        : vo.getMember().nickName);

        return builder.toString();
    }
}
