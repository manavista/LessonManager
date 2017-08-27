package jp.manavista.lessonmanager.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule;

/**
 *
 * MemberLessonSchedule Service Interface
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public interface MemberLessonScheduleService {

    /**
     *
     * Get
     *
     * <p>
     * Overview:<br>
     * Get a entity by id.
     * </p>
     *
     * @param id Member id
     * @return {@link MemberLessonSchedule} entity
     */
    Single<MemberLessonSchedule> getById(final long id);

    /**
     *
     * Get List By Id
     *
     * <p>
     * Overview:<br>
     * Acquire {@code MemberLessonSchedule} entity list
     * associated with the lesson id specified in the argument.
     * </p>
     *
     * @param lessonId target lesson id
     * @return {@code MemberLesson} entity of Observable
     */
    Observable<MemberLessonSchedule> getListByLessonId(final long lessonId);

    /**
     *
     * Get List By Id
     *
     * <p>
     * Overview:<br>
     * Acquire {@code MemberLessonSchedule} entity list
     * associated with the member id specified in the argument.
     * </p>
     *
     * @param memberId target lesson id
     * @return {@code MemberLesson} entity of Observable
     */
    Observable<MemberLessonSchedule> getListByMemberId(final long memberId);

    /**
     *
     * Save
     *
     * <p>
     * Overview:<br>
     * Save a entity.
     * </p>
     *
     * @param entity target {@link MemberLessonSchedule} entity
     * @return {@link Single} observable object
     */
    Single<MemberLessonSchedule> save(MemberLessonSchedule entity);

    /**
     *
     * Create By Lesson
     *
     * <p>
     * Overview:<br>
     * Creates a series of schedule by {@code MemberLesson} entity.
     * </p>
     *
     * @param lesson A key of {@code MemberLesson}
     * @return {@link Single} observable object in create entity rows.
     */
    Single<Long> createByLesson(MemberLesson lesson);

    /**
     *
     * Delete
     *
     * <p>
     * Overview:<br>
     * Delete by entity row id.
     * </p>
     *
     * @param id row id
     * @return transaction row count
     */
    Single<Integer> deleteById(long id);

    /**
     *
     * Delete All
     *
     * <p>
     * Overview:<br>
     * Delete All rows in {@code MemberLessonSchedule}
     * </p>
     *
     * @return {@link Single} observable object in deleted rows count
     */
    Single<Integer> deleteAll();
}
