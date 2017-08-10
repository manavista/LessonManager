package jp.manavista.developbase.view.operation;

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
 * @see jp.manavista.developbase.view.adapter.MemberAdapter MemberAdapter
 * @see jp.manavista.developbase.fragment.MemberFragment MemberFragment
 */
public interface MemberOperation {

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
