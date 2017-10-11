/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.repository;

import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule_Deleter;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule_Relation;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule_Schema;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule_Selector;

/**
 *
 * MemberLessonSchedule Repository
 *
 * <p>
 * Overview:<br>
 * Manipulate repository on {@link jp.manavista.lessonmanager.model.entity.MemberLessonSchedule} data.
 * </p>
 */
public interface MemberLessonScheduleRepository {

    /**
     *
     * Get schema
     *
     * <p>
     * Overview:<br>
     * Get MemberLessonSchedule schema
     * </p>
     *
     * @return {@code MemberLessonSchedule} Schema
     */
    MemberLessonSchedule_Schema getSchema();

    /**
     *
     * Get selector
     *
     * <p>
     * Overview:<br>
     * Get MemberLessonSchedule selector
     * </p>
     *
     * @return {@code MemberLessonSchedule} Selector
     */
    MemberLessonSchedule_Selector getSelector();

    /**
     *
     * Get relation
     *
     * <p>
     * Overview:<br>
     * Get MemberLessonSchedule relation object.
     * </p>
     *
     * @return {@code MemberLessonSchedule} relation
     */
    MemberLessonSchedule_Relation getRelation();

    /**
     *
     * Get deleter
     *
     * <p>
     * Overview:<br>
     * Get MemberLessonSchedule deleter object.
     * </p>
     *
     * @return {@code MemberLessonSchedule} deleter
     */
    MemberLessonSchedule_Deleter getDeleter();
}
