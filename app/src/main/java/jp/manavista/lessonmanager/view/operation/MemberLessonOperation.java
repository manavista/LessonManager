/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.operation;

import jp.manavista.lessonmanager.model.vo.MemberLessonVo;

/**
 *
 * Member Lesson Operation
 *
 * <p>
 * Overview:<br>
 * Operation MemberLesson entity.<br>
 * Interface for executing listeners defined by adapters with fragment.
 * </p>
 */
public interface MemberLessonOperation {

    /**
     *
     * Edit
     *
     * <p>
     * Overview:<br>
     * Edit MemberLesson row
     * </p>
     *
     * @param dto target row dto
     * @param position target adapter position
     */
    void edit(final MemberLessonVo dto, final int position);

    /**
     *
     * Delete
     *
     * <p>
     * Overview:<br>
     * Delete MemberLesson row.
     * </p>
     *
     * @param id target row id
     * @param position target adapter position
     */
    void delete(final long id, final int position);

    /**
     *
     * Close
     *
     * <p>
     * Overview:<br>
     * Close to Expanded Lesson row.<br>
     * Switch Show/Hide to detail information in Lesson.
     * </p>
     */
    void close();

    /**
     *
     * Filter
     *
     * <p>
     * Overview:<br>
     * Switch to screen display of only lessonId specified as argument.
     * </p>
     *
     * @param lessonId target lesson id
     */
    void filter(final long lessonId);

    /**
     *
     * Clear Filter
     *
     * <p>
     * Overview:<br>
     * Clear Lesson's filter
     * </p>
     */
    void clearFilter();
}
