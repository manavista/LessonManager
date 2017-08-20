package jp.manavista.lessonmanager.view.operation;

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
     * Delete MemberLesson row.
     * </p>
     *
     * @param id target row id
     * @param position target adapter position
     */
    void delete(final long id, final int position);
}
