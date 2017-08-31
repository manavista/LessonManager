package jp.manavista.lessonmanager.injector.module;

import dagger.Module;
import dagger.Provides;
import jp.manavista.lessonmanager.facade.MemberLessonScheduleListFacade;
import jp.manavista.lessonmanager.facade.impl.MemberLessonScheduleListFacadeImpl;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;

/**
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
}
