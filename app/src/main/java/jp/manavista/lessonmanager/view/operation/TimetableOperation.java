/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.operation;

import android.view.View;

import jp.manavista.lessonmanager.model.entity.Timetable;

/**
 *
 * Timetable Operation interface
 *
 * <p>
 * Overview:<br>
 * Operation timetable entity.<br>
 * Interface for executing listeners defined by adapters with fragment.
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

    /**
     *
     * Input LessonNo
     *
     * <p>
     * Overview:<br>
     * Input Timetable Lesson No.
     * </p>
     *
     * @param view LessonNo View Object
     * @param position Holder Adapter position
     */
    void inputLessonNo(final View view, final int position);
}
