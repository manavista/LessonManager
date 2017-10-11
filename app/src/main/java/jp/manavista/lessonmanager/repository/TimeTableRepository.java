/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.repository;

import jp.manavista.lessonmanager.model.entity.Timetable_Deleter;
import jp.manavista.lessonmanager.model.entity.Timetable_Relation;
import jp.manavista.lessonmanager.model.entity.Timetable_Selector;

/**
 *
 * Timetable Repository
 *
 * <p>
 * Overview:<br>
 * Manipulate repository on Timetable data.
 * </p>
 */
public interface TimeTableRepository {

    /**
     *
     * Get selector
     *
     * <p>
     * Overview:<br>
     * Get Timetable selector
     * </p>
     *
     * @return Timetable Selector
     */
    Timetable_Selector getSelector();

    /**
     *
     * Get relation
     *
     * <p>
     * Overview:<br>
     * Get Timetable relation object.
     * </p>
     *
     * @return Timetable relation
     */
    Timetable_Relation getRelation();

    /**
     *
     * Get deleter
     *
     * <p>
     * Overview:<br>
     * Get Timetable deleter object.
     * </p>
     *
     * @return Timetable deleter
     */
    Timetable_Deleter getDeleter();

}
