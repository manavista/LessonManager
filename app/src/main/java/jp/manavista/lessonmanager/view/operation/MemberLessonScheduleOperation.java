/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.operation;

/**
 *
 * MemberLessonSchedule Operation
 *
 * <p>
 * Overview:<br>
 * Operation <code>MemberLessonSchedule</code> entity.<br>
 * Interface for executing listeners defined by adapters with fragment.
 * </p>
 */
public interface MemberLessonScheduleOperation {

    /**
     *
     * Edit
     *
     * <p>
     * Overview:<br>
     * Edit MemberLessonSchedule row
     * </p>
     *
     * @param id target row id
     * @param position target adapter position
     */
    void edit(final long id, final int position);

    /**
     *
     * Delete
     *
     * <p>
     * Overview:<br>
     * Delete MemberLessonSchedule row.
     * </p>
     *
     * @param position target adapter position
     */
    void delete(final int position);
}
