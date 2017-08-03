package jp.manavista.developbase.repository;

import jp.manavista.developbase.model.entity.Timetable_Deleter;
import jp.manavista.developbase.model.entity.Timetable_Relation;
import jp.manavista.developbase.model.entity.Timetable_Selector;

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

    Timetable_Relation getRelation();

    Timetable_Deleter getDeleter();

}
