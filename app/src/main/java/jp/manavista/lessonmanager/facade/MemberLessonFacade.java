package jp.manavista.lessonmanager.facade;

import io.reactivex.Single;
import jp.manavista.lessonmanager.model.entity.MemberLesson;

/**
 *
 * MemberLesson Facade
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public interface MemberLessonFacade {

    Single<Long> save(long memberId, MemberLesson entity, boolean addSchedule);

    /**
     *
     * Is Empty Schedule
     *
     * <p>
     * Overview:<br>
     * It judges whether the schedule based on the lessonId specified in the argument is empty.
     * </p>
     *
     * @param lessonId target LessonId
     * @return true if empty
     */
    Single<Boolean> isEmptySchedule(long lessonId);
}
