package jp.manavista.lessonmanager.injector.module;

import dagger.Module;
import dagger.Provides;
import jp.manavista.lessonmanager.repository.EventRepository;
import jp.manavista.lessonmanager.repository.MemberLessonRepository;
import jp.manavista.lessonmanager.repository.MemberLessonScheduleRepository;
import jp.manavista.lessonmanager.repository.MemberRepository;
import jp.manavista.lessonmanager.repository.TimeTableRepository;
import jp.manavista.lessonmanager.service.EventService;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;
import jp.manavista.lessonmanager.service.MemberService;
import jp.manavista.lessonmanager.service.TimetableService;
import jp.manavista.lessonmanager.service.impl.EventServiceImpl;
import jp.manavista.lessonmanager.service.impl.MemberLessonScheduleServiceImpl;
import jp.manavista.lessonmanager.service.impl.MemberLessonServiceImpl;
import jp.manavista.lessonmanager.service.impl.MemberServiceImpl;
import jp.manavista.lessonmanager.service.impl.TimetableServiceImpl;

/**
 *
 * Service Module
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
@Module
public class ServiceModule {

    @Provides
    TimetableService providerTimetableService(TimeTableRepository timeTableRepository) {
        return new TimetableServiceImpl(timeTableRepository);
    }

    @Provides
    MemberService providerMemberService(MemberRepository memberRepository) {
        return new MemberServiceImpl(memberRepository);
    }

    @Provides
    MemberLessonService provideMemberLessonService(MemberLessonRepository memberLessonRepository) {
        return new MemberLessonServiceImpl(memberLessonRepository);
    }

    @Provides
    MemberLessonScheduleService provideMemberLessonScheduleService(MemberLessonScheduleRepository memberLessonScheduleRepository) {
        return new MemberLessonScheduleServiceImpl(memberLessonScheduleRepository);
    }

    @Provides
    EventService provideEventService(EventRepository eventRepository) {
        return new EventServiceImpl(eventRepository);
    }
}
