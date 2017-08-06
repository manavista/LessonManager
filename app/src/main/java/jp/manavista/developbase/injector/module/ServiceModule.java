package jp.manavista.developbase.injector.module;

import dagger.Module;
import dagger.Provides;
import jp.manavista.developbase.repository.MemberRepository;
import jp.manavista.developbase.repository.TimeTableRepository;
import jp.manavista.developbase.service.MemberService;
import jp.manavista.developbase.service.TimetableService;
import jp.manavista.developbase.service.impl.MemberServiceImpl;
import jp.manavista.developbase.service.impl.TimetableServiceImpl;

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
}
