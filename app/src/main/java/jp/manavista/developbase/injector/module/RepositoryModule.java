package jp.manavista.developbase.injector.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jp.manavista.developbase.model.entity.OrmaDatabase;
import jp.manavista.developbase.repository.TimeTableRepository;
import jp.manavista.developbase.repository.impl.TimetableRepositoryImpl;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
@Module
public class RepositoryModule {

    private static final String DB_NAME = "main.db";

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    OrmaDatabase provideOrmaDatabase(Application application) {
        return OrmaDatabase.builder(application)
                .name(DB_NAME)
                .build();
    }

    @Provides
    @Singleton
    TimeTableRepository provideTimetableRepository(OrmaDatabase database) {
        return new TimetableRepositoryImpl(database);
    }
}
