package jp.manavista.lessonmanager.facade;

import java.util.Set;

import io.reactivex.Single;

/**
 *
 * Setting Delete Facade Interface
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public interface SettingDeleteFacade {

    String TIMETABLE = "Timetable";
    String MEMBER = "Member";
    String LESSON = "Lesson";
    String SCHEDULE = "Schedule";


    Single<Integer> delete(Set<String> targetSet);
}
