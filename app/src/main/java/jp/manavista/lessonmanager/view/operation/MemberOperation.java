package jp.manavista.lessonmanager.view.operation;

/**
 *
 * Member Operation
 *
 * <p>
 * Overview:<br>
 * Operation Member entity.<br>
 * Interface for executing listeners defined by adapters with fragment.
 * </p>
 *
 * @see jp.manavista.lessonmanager.view.adapter.MemberAdapter MemberAdapter
 * @see jp.manavista.lessonmanager.fragment.MemberFragment MemberFragment
 */
public interface MemberOperation {

    /**
     *
     * Edit
     *
     * <p>
     * Overview:<br>
     * Edit Member row
     * </p>
     *
     * @param id target row id
     * @param position target adapter position
     */
    void edit(final int id, final int position);

    /**
     *
     * Delete
     *
     * <p>
     * Overview:<br>
     * Delete Member row.
     * </p>
     *
     * @param id target row id
     * @param position target adapter position
     */
    void delete(final int id, final int position);
}
