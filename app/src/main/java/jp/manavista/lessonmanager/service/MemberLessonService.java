package jp.manavista.lessonmanager.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.manavista.lessonmanager.model.entity.MemberLesson;

/**
 *
 * MemberLesson Service Interface
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public interface MemberLessonService {

    /**
     *
     * Get All List
     *
     * <p>
     * Overview:<br>
     * Get All {@code MemberLesson} entity of Observable object.
     * </p>
     *
     * @return All {@code MemberLesson} entity of Observable
     */
    Observable<MemberLesson> getListAll();

    /**
     *
     * Get List By Id
     *
     * <p>
     * Overview:<br>
     * Acquire {@code MemberLesson} entity list
     * associated with the member id specified in the argument.
     * </p>
     *
     * @param memberId target member id
     * @return {@code MemberLesson} entity of Observable
     */
    Observable<MemberLesson> getListByMemberId(long memberId);


    /**
     *
     * Save
     *
     * <p>
     * Overview:<br>
     * Save member entity.
     * </p>
     *
     * @param memberLesson target {@link MemberLesson} entity
     * @return {@link Single} observable object
     */
    Single<MemberLesson> save(MemberLesson memberLesson);

    /**
     *
     * Delete All
     *
     * <p>
     * Overview:<br>
     * Delete All rows in {@code MemberLesson}
     * </p>
     *
     * @return {@link Single} observable object in deleted rows count
     */
    Single<Integer> deleteAll();
}
