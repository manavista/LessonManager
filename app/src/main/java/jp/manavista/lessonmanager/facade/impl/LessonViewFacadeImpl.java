/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.facade.impl;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import jp.manavista.lessonmanager.facade.LessonViewFacade;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.service.EventService;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.TimetableService;
import jp.manavista.lessonmanager.view.week.LessonView;
import jp.manavista.lessonmanager.view.week.WeekViewEvent;
import lombok.val;

/**
 *
 * LessonViewFacade Implementation
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class LessonViewFacadeImpl implements LessonViewFacade {

    private final TimetableService timetableService;
    private final EventService eventService;
    private final MemberLessonScheduleService memberLessonScheduleService;


    /** Constructor */
    public LessonViewFacadeImpl(
            TimetableService timetableService,
            EventService eventService,
            MemberLessonScheduleService memberLessonScheduleService) {

        this.timetableService = timetableService;
        this.eventService = eventService;
        this.memberLessonScheduleService = memberLessonScheduleService;

    }

    @Override
    public Disposable getViewData(final LessonView lessonView,
                                  final List<TimetableDto> timetableList,
                                  final List<WeekViewEvent> scheduleList,
                                  final Set<Integer> statusSet) {

        timetableList.clear();
        scheduleList.clear();

        /* Do not create a new instance inside the loop. */
        final StringBuilder builder = new StringBuilder();

        return timetableService.getDtoListAll().toList()
                .flatMapObservable(new Function<List<TimetableDto>, ObservableSource<MemberLessonScheduleVo>>() {
            @Override
            public ObservableSource<MemberLessonScheduleVo> apply(@NonNull List<TimetableDto> dtoList) throws Exception {

                timetableList.addAll(dtoList);
                lessonView.setLessonTableList(dtoList);
                return memberLessonScheduleService.getVoListByStatus(statusSet);
            }
        }).map(new Function<MemberLessonScheduleVo, WeekViewEvent>() {
            @Override
            public WeekViewEvent apply(@NonNull MemberLessonScheduleVo vo) throws Exception {
                final WeekViewEvent lesson = new WeekViewEvent(
                        vo.getId(),
                        buildEventName(vo, timetableList, builder),
                        vo.getLocation(),
                        vo.getLessonStartCalendar(),
                        vo.getLessonEndCalendar());
                lesson.setColor(vo.getLessonViewColor());

                return lesson;
            }
        }).toList()
        .flatMapObservable(new Function<List<WeekViewEvent>, ObservableSource<WeekViewEvent>>() {
            @Override
            public ObservableSource<WeekViewEvent> apply(@NonNull List<WeekViewEvent> list) throws Exception {
                scheduleList.addAll(list);
                return eventService.getEventListAll();
            }
        }).toList()
        .subscribe(new Consumer<List<WeekViewEvent>>() {
            @Override
            public void accept(List<WeekViewEvent> list) throws Exception {
                scheduleList.addAll(list);
                lessonView.notifyDatasetChanged();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    private String buildEventName(MemberLessonScheduleVo vo, List<TimetableDto> timetableList, StringBuilder builder) {

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
