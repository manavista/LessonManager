package jp.manavista.developbase.injector.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jp.manavista.developbase.model.entity.OrmaDatabase;
import jp.manavista.developbase.repository.MemberLessonRepository;
import jp.manavista.developbase.repository.MemberRepository;
import jp.manavista.developbase.repository.TimeTableRepository;
import jp.manavista.developbase.repository.impl.MemberLessonRepositoryImpl;
import jp.manavista.developbase.repository.impl.MemberRepositoryImpl;
import jp.manavista.developbase.repository.impl.TimetableRepositoryImpl;

/**
 *
 * Repository Module
 *
 * <p>
 * Overview:<br>
 * </p>
 */
@Module
public class RepositoryModule {

    /** Database Name */
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

    @Provides
    @Singleton
    MemberRepository provideMemberRepository(OrmaDatabase database) {
        return new MemberRepositoryImpl(database);
    }

    @Provides
    @Singleton
    MemberLessonRepository provideMemberLessonRepository(OrmaDatabase database) {
        return new MemberLessonRepositoryImpl(database);
    }
}
