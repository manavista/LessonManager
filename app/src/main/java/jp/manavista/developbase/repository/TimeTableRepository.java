package jp.manavista.developbase.repository;

import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.entity.Timetable_Deleter;
import jp.manavista.developbase.entity.Timetable_Relation;
import jp.manavista.developbase.entity.Timetable_Selector;

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
     * get all
     *
     * <p>
     * Overview:<br>
     * Get all Timetable data
     * </p>
     *
     * @return Timetable Selector
     */
    Timetable_Selector getAll();

    Timetable_Relation getRelation();

    Timetable_Deleter getDeleter();

    void save(Timetable timetable);

    void deleteAll();
}
