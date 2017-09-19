package jp.manavista.lessonmanager.injector.module;

import dagger.Module;
import dagger.Provides;
import jp.manavista.lessonmanager.facade.LessonViewFacade;
import jp.manavista.lessonmanager.facade.MemberLessonFacade;
import jp.manavista.lessonmanager.facade.MemberLessonScheduleListFacade;
import jp.manavista.lessonmanager.facade.SettingDeleteFacade;
import jp.manavista.lessonmanager.facade.impl.LessonViewFacadeImpl;
import jp.manavista.lessonmanager.facade.impl.MemberLessonFacadeImpl;
import jp.manavista.lessonmanager.facade.impl.MemberLessonScheduleListFacadeImpl;
import jp.manavista.lessonmanager.facade.impl.SettingDeleteFacadeImpl;
import jp.manavista.lessonmanager.service.EventService;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;
import jp.manavista.lessonmanager.service.MemberService;
import jp.manavista.lessonmanager.service.TimetableService;

/**
 *
 * Facade Module
 *
 * <p>
 * Overview:<br>
 * </p>
 */
@Module
public class FacadeModule {

    @Provides
    MemberLessonScheduleListFacade providerMemberLessonScheduleListFacade(
            MemberLessonService memberLessonService,
            MemberLessonScheduleService memberLessonScheduleService
    ) {
        return new MemberLessonScheduleListFacadeImpl(memberLessonService,memberLessonScheduleService);
    }

    @Provides
    SettingDeleteFacade provideSettingDeleteFacade (
            TimetableService timetableService,
            MemberService memberService,
            MemberLessonService memberLessonService,
            MemberLessonScheduleService memberLessonScheduleService,
            EventService eventService

    ) {
        return new SettingDeleteFacadeImpl(
                timetableService,
                memberService,
                memberLessonService,
                memberLessonScheduleService,
                eventService);
    }

    @Provides
    LessonViewFacade provideLessonViewFacade(
            TimetableService timetableService,
            EventService eventService,
            MemberLessonScheduleService memberLessonScheduleService
    ) {
        return new LessonViewFacadeImpl(
                timetableService,
                eventService,
                memberLessonScheduleService);
    }

    @Provides
    MemberLessonFacade provideMemberLessonFacade(
            MemberService memberService,
            MemberLessonService memberLessonService,
            MemberLessonScheduleService memberLessonScheduleService
    ) {
        return new MemberLessonFacadeImpl(
                memberService,
                memberLessonService,
                memberLessonScheduleService);
    }
}
