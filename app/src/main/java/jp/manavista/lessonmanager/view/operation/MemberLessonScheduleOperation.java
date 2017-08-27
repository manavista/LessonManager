package jp.manavista.lessonmanager.view.operation;

/**
 * <p>
 * Overview:<br>
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
     * @param id target row id
     * @param position target adapter position
     */
    void delete(final long id, final int position);
}
