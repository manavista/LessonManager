/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.manavista.lessonmanager.model.entity.Member;
import jp.manavista.lessonmanager.model.vo.MemberVo;

/**
 *
 * Member Service interface
 *
 * <p>
 * Overview:<br>
 * For the data of {@link Member}, define actions related to acquisition and processing.
 * </p>
 */
public interface MemberService {

    /**
     *
     * Get
     *
     * <p>
     * Overview:<br>
     * Get a Member entity by id.
     * </p>
     *
     * @param id Member id
     * @return {@link Member} entity
     */
    Single<Member> getById(final long id);

    /**
     *
     * Get All Value Object List
     *
     * <p>
     * Overview:<br>
     * Get All {@link MemberVo} of Observable object.<br>
     * Set the screen display name by the code specified in the argument.
     * </p>
     *
     * @param displayNameCode Code to select name display item
     * @return All {@link MemberVo} of Observable
     */
    Observable<MemberVo> getVoListAll(int displayNameCode);

    /**
     *
     * Save
     *
     * <p>
     * Overview:<br>
     * Save member entity.
     * </p>
     *
     * @param member target {@link Member} entity
     * @return {@link Single} observable object
     */
    Single<Member> save(Member member);

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
     * Delete All rows in {@code Member}
     * </p>
     *
     * @return {@link Single} observable object in deleted rows count
     */
    Single<Integer> deleteAll();

    /**
     *
     * Get Display Name
     *
     * <p>
     * Overview:<br>
     * Member Display Name (User selectable by displayCode).
     * </p>
     *
     * @param entity {@link Member} entity
     * @param displayCode Display Type Code
     * @param builder For concat string builder
     * @return Display Name
     */
    String getDisplayName(final Member entity, final int displayCode, final StringBuilder builder);
}
