/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.injector.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jp.manavista.lessonmanager.model.entity.OrmaDatabase;
import jp.manavista.lessonmanager.repository.EventRepository;
import jp.manavista.lessonmanager.repository.MemberLessonRepository;
import jp.manavista.lessonmanager.repository.MemberLessonScheduleRepository;
import jp.manavista.lessonmanager.repository.MemberRepository;
import jp.manavista.lessonmanager.repository.TimeTableRepository;
import jp.manavista.lessonmanager.repository.impl.EventRepositoryImpl;
import jp.manavista.lessonmanager.repository.impl.MemberLessonRepositoryImpl;
import jp.manavista.lessonmanager.repository.impl.MemberLessonScheduleRepositoryImpl;
import jp.manavista.lessonmanager.repository.impl.MemberRepositoryImpl;
import jp.manavista.lessonmanager.repository.impl.TimetableRepositoryImpl;

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
    private static final String DB_NAME = "toolbar_lesson_view.db";

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

    @Provides
    @Singleton
    MemberLessonScheduleRepository provideMemberLessonScheduleRepository(OrmaDatabase database) {
        return new MemberLessonScheduleRepositoryImpl(database);
    }

    @Provides
    @Singleton
    EventRepository provideEventRepository(OrmaDatabase database) {
        return new EventRepositoryImpl(database);
    }
}
