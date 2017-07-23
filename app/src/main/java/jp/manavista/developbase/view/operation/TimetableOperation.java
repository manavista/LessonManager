package jp.manavista.developbase.view.operation;

import jp.manavista.developbase.entity.Timetable;

/**
 *
 * Timetable Operation interface
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public interface TimetableOperation {

    /**
     *
     * Delete
     *
     * <p>
     * Overview:<br>
     * Delete Timetable row.
     * </p>
     *
     * @param id target row id
     */
    void delete(int id);

    /**
     *
     * Update
     *
     * <p>
     * Overview:<br>
     * Update Timetable row.
     * </p>
     *
     * @param timetable target Timetable entity
     */
    void update(Timetable timetable);
}
