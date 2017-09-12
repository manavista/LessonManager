package jp.manavista.lessonmanager.facade.impl;

import java.util.Set;

import io.reactivex.Single;
import jp.manavista.lessonmanager.facade.SettingDeleteFacade;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;
import jp.manavista.lessonmanager.service.MemberService;
import jp.manavista.lessonmanager.service.TimetableService;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public class SettingDeleteFacadeImpl implements SettingDeleteFacade {

    private final TimetableService timetableService;
    private final MemberService memberService;
    private final MemberLessonService memberLessonService;
    private final MemberLessonScheduleService memberLessonScheduleService;

    /** Constructor */
    public SettingDeleteFacadeImpl (
            TimetableService timetableService,
            MemberService memberService,
            MemberLessonService memberLessonService,
            MemberLessonScheduleService memberLessonScheduleService) {
        this.timetableService = timetableService;
        this.memberService = memberService;
        this.memberLessonService = memberLessonService;
        this.memberLessonScheduleService = memberLessonScheduleService;
    }

    @Override
    public Single<Integer> delete(Set<String> targetSet) {

        Integer transaction = 0;

        // TODO: 2017/09/12 implement dynamic transaction items

        return null;
    }
}
