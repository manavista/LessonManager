package jp.manavista.developbase.repository;

import jp.manavista.developbase.model.entity.Member_Deleter;
import jp.manavista.developbase.model.entity.Member_Relation;
import jp.manavista.developbase.model.entity.Member_Selector;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public interface MemberRepository {

    Member_Selector getSelector();

    Member_Relation getRelation();

    Member_Deleter getDeleter();
}
