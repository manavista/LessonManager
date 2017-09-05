package jp.manavista.lessonmanager.service;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.manavista.lessonmanager.model.entity.Member;
import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;

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
     * Get List
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
     * Get List By MemberId
     *
     * <p>
     * Overview:<br>
     * Acquire {@code MemberLessonSchedule} entity list
     * associated with the member id specified in the argument.
     * </p>
     *
     * @param memberId target member id
     * @return {@code MemberLesson} entity of Observable
     */
    Observable<MemberLessonSchedule> getListByMemberId(final long memberId);

    /**
     *
     * Get Value Object List By MemberId
     *
     * <p>
     * Overview:<br>
     * Convert {@code MemberLessonSchedule} to {@code MemberLessonScheduleVo} list
     * associated with the member id specified in the argument.
     * </p>
     *
     * @param memberId target member id
     * @return {@code MemberLessonScheduleVo} of Observable
     */
    Observable<MemberLessonScheduleVo> getVoListByMemberId(final long memberId);


    Single<List<MemberLessonScheduleVo>> getSingleVoListByMemberId(final long memberId);

    Observable<MemberLessonScheduleVo> getVoListAll();

    Observable<MemberLessonScheduleVo> getVoListByMonth(final int year, final int month);

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
     * @param member A key of {@code Member}
     * @param lesson A key of {@code MemberLesson}
     * @return {@link Single} observable object in create entity rows.
     */
    Single<Long> createByLesson(Member member, MemberLesson lesson);

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
