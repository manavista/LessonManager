package jp.manavista.developbase.injector.module;

import dagger.Module;
import dagger.Provides;
import jp.manavista.developbase.repository.TimeTableRepository;
import jp.manavista.developbase.service.TimetableService;
import jp.manavista.developbase.service.impl.TimetableServiceImpl;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
@Module
public class ServiceModule {

    @Provides
    TimetableService providerTimetableService(TimeTableRepository timeTableRepository) {
        return new TimetableServiceImpl(timeTableRepository);
    }
}
