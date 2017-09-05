package jp.manavista.lessonmanager.fragment;

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
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
import jp.manavista.lessonmanager.model.entity.Timetable;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.TimetableService;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import jp.manavista.lessonmanager.view.week.DateTimeInterpreter;
import jp.manavista.lessonmanager.view.week.LessonView;
import jp.manavista.lessonmanager.view.week.MonthLoader;
import jp.manavista.lessonmanager.view.week.WeekView;
import jp.manavista.lessonmanager.view.week.WeekViewEvent;
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

    /** LessonView */
    private LessonView lessonView;
    /** RootView */
    private View rootView;
    /** Timetable DTO List */
    private List<TimetableDto> timetableList;
    /** eekViewEvent List */
    private List<WeekViewEvent> weekViewEventList;

    /** Shared preferences */
    @Inject
    SharedPreferences sharedPreferences;
    /** Timetable service */
    @Inject
    TimetableService timetableService;
    @Inject
    MemberLessonScheduleService memberLessonScheduleService;

    /** Timetable categoriesList disposable */
    private Disposable timetableDisposable;


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timetableDisposable = Disposables.empty();
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
        weekViewEventList = new ArrayList<>();
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        Log.d(TAG, "newYear: " + newYear + " newMonth: " + newMonth);

        final List<WeekViewEvent> events = new ArrayList<>();

        for( val event : weekViewEventList ) {
            if( isMatched(event.getStartTime(), newYear, newMonth) ) {
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        // TODO: 2017/09/05 implement activity lesson schedule edit
        Log.d(TAG, event.toString());
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

    private boolean isMatched(Calendar calendar, final int year, final int month) {
        return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month;
    }

    private void buildEvents() {

        weekViewEventList.clear();

        /* Do not create a new instance inside the loop. */
        final StringBuilder builder = new StringBuilder();

        memberLessonScheduleService.getVoListAll().map(new Function<MemberLessonScheduleVo, WeekViewEvent>() {
            @Override
            public WeekViewEvent apply(@NonNull MemberLessonScheduleVo vo) throws Exception {

                final WeekViewEvent event = new WeekViewEvent(
                        vo.getId(),
                        buildEventName(vo, builder),
                        vo.getLocation(),
                        vo.getLessonStartCalendar(),
                        vo.getLessonEndCalendar());
                event.setColor(vo.getBackgroundColor());

                return event;
            }
        }).subscribe(new Consumer<WeekViewEvent>() {
            @Override
            public void accept(WeekViewEvent weekViewEvent) throws Exception {
                weekViewEventList.add(weekViewEvent);
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
