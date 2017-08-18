package jp.manavista.developbase.repository.impl;

import jp.manavista.developbase.model.entity.MemberLesson_Deleter;
import jp.manavista.developbase.model.entity.MemberLesson_Relation;
import jp.manavista.developbase.model.entity.MemberLesson_Selector;
import jp.manavista.developbase.model.entity.OrmaDatabase;
import jp.manavista.developbase.repository.MemberLessonRepository;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public class MemberLessonRepositoryImpl extends RepositoryBaseImpl implements MemberLessonRepository {

    /** Constructor */
    public MemberLessonRepositoryImpl(OrmaDatabase database) {
        super(database);
    }

    @Override
    public MemberLesson_Selector getSelector() {
        return database.selectFromMemberLesson();
    }

    @Override
    public MemberLesson_Relation getRelation() {
        return database.relationOfMemberLesson();
    }

    @Override
    public MemberLesson_Deleter getDeleter() {
        return database.deleteFromMemberLesson();
    }
}
