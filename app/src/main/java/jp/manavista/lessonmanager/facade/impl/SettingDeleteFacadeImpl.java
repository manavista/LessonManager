/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.manavista.lessonmanager.facade.SettingDeleteFacade;
import jp.manavista.lessonmanager.service.EventService;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;
import jp.manavista.lessonmanager.service.MemberService;
import jp.manavista.lessonmanager.service.TimetableService;

/**
 *
 * Setting Delete Facade Implementation
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public class SettingDeleteFacadeImpl implements SettingDeleteFacade {

    private final TimetableService timetableService;
    private final MemberService memberService;
    private final MemberLessonService memberLessonService;
    private final MemberLessonScheduleService memberLessonScheduleService;
    private final EventService eventService;

    /** Constructor */
    public SettingDeleteFacadeImpl (
            TimetableService timetableService,
            MemberService memberService,
            MemberLessonService memberLessonService,
            MemberLessonScheduleService memberLessonScheduleService,
            EventService eventService) {
        this.timetableService = timetableService;
        this.memberService = memberService;
        this.memberLessonService = memberLessonService;
        this.memberLessonScheduleService = memberLessonScheduleService;
        this.eventService = eventService;
    }

    @Override
    public Single<Integer> delete(Set<String> targetSet) {

        if( targetSet.isEmpty() ) {
            return Single.just(0);
        }

        List<Observable<Integer>> targetList = new ArrayList<>();

        if( targetSet.contains(TIMETABLE) ) {
            targetList.add(timetableService.deleteAll().toObservable());
        }
        if( targetSet.contains(EVENT) ) {
            targetList.add(eventService.deleteAll().toObservable());
        }

        if( targetSet.contains(MEMBER) ) {
            targetList.add(memberLessonScheduleService.deleteAll().toObservable());
            targetList.add(memberLessonService.deleteAll().toObservable());
            targetList.add(memberService.deleteAll().toObservable());
        } else if( targetSet.contains(LESSON) ) {
            targetList.add(memberLessonScheduleService.deleteAll().toObservable());
            targetList.add(memberLessonService.deleteAll().toObservable());
        } else if( targetSet.contains(SCHEDULE) ) {
            targetList.add(memberLessonScheduleService.deleteAll().toObservable());
        }

        return Observable
                .concat(targetList)
                .reduce(0, (sum, rows) -> sum + rows);
    }
}
