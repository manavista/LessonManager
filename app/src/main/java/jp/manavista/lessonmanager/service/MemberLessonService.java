/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.service;

import java.util.List;

import io.reactivex.Single;
import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;

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
     * Get
     *
     * <p>
     * Overview:<br>
     * Get a MemberLesson entity by id.
     * </p>
     *
     * @param id Member id
     * @return {@link MemberLesson} entity
     */
    Single<MemberLesson> getById(final long id);

    /**
     *
     * Get Value Object List By MemberId
     *
     * <p>
     * Overview:<br>
     * Convert {@code MemberLesson} to {@code MemberLessonVo} list
     * associated with the member id specified in the argument.<br>
     * It is judged whether or not past data is also included
     * in the {@code containPast} flag
     * </p>
     *
     * @param memberId target member id
     * @param containPast whether or not past data
     * @return {@code MemberLessonVo} List of Single
     */
    Single<List<MemberLessonVo>> getSingleVoListByMemberId(long memberId, boolean containPast);

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

    Single<Integer> deleteByMemberId(long memberId);

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
