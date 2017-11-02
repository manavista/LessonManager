/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.repository.impl;

import jp.manavista.lessonmanager.model.entity.MemberLesson_Deleter;
import jp.manavista.lessonmanager.model.entity.MemberLesson_Relation;
import jp.manavista.lessonmanager.model.entity.MemberLesson_Selector;
import jp.manavista.lessonmanager.model.entity.OrmaDatabase;
import jp.manavista.lessonmanager.repository.MemberLessonRepository;

/**
 *
 * MemberLesson Repository Implement
 *
 * <p>
 * Overview:<br>
 *
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
