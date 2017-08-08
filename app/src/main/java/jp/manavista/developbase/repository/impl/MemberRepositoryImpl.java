package jp.manavista.developbase.repository.impl;

import jp.manavista.developbase.model.entity.Member_Deleter;
import jp.manavista.developbase.model.entity.Member_Relation;
import jp.manavista.developbase.model.entity.Member_Selector;
import jp.manavista.developbase.model.entity.OrmaDatabase;
import jp.manavista.developbase.repository.MemberRepository;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public class MemberRepositoryImpl extends RepositoryBaseImpl implements MemberRepository {

    /** Constructor */
    public MemberRepositoryImpl(OrmaDatabase database) {
        super(database);
    }

    @Override
    public Member_Selector getSelector() {
        return database.selectFromMember();
    }

    @Override
    public Member_Relation getRelation() {
        return database.relationOfMember();
    }

    @Override
    public Member_Deleter getDeleter() {
        return database.deleteFromMember();
    }
}
