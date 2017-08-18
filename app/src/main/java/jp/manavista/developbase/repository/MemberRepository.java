package jp.manavista.developbase.repository;

import jp.manavista.developbase.model.entity.Member_Deleter;
import jp.manavista.developbase.model.entity.Member_Relation;
import jp.manavista.developbase.model.entity.Member_Selector;

/**
 *
 * Member Repository
 *
 * <p>
 * Overview:<br>
 * Manipulate repository on Member data.
 * </p>
 */
public interface MemberRepository {

    /**
     *
     * Get selector
     *
     * <p>
     * Overview:<br>
     * Get Member selector
     * </p>
     *
     * @return Member Selector
     */
    Member_Selector getSelector();

    /**
     *
     * Get relation
     *
     * <p>
     * Overview:<br>
     * Get Member relation object.
     * </p>
     *
     * @return Member relation
     */
    Member_Relation getRelation();

    /**
     *
     * Get deleter
     *
     * <p>
     * Overview:<br>
     * Get Member deleter object.
     * </p>
     *
     * @return Member deleter
     */
    Member_Deleter getDeleter();
}
