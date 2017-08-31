package jp.manavista.lessonmanager.repository.impl;

import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule_Deleter;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule_Relation;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule_Schema;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule_Selector;
import jp.manavista.lessonmanager.model.entity.OrmaDatabase;
import jp.manavista.lessonmanager.repository.MemberLessonScheduleRepository;

/**
 *
 * MemberLessonSchedule Repository Implement
 *
 * <p>
 * Overview:<br>
 * Define {@code MemberLessonSchedule} Repository implementation.
 * </p>
 */
public class MemberLessonScheduleRepositoryImpl extends RepositoryBaseImpl implements MemberLessonScheduleRepository {

    /** Constructor */
    public MemberLessonScheduleRepositoryImpl(OrmaDatabase database) {
        super(database);
    }

    @Override
    public MemberLessonSchedule_Schema getSchema() {
        return getRelation().getSchema();
    }

    @Override
    public MemberLessonSchedule_Selector getSelector() {
        return database.selectFromMemberLessonSchedule();
    }

    @Override
    public MemberLessonSchedule_Relation getRelation() {
        return database.relationOfMemberLessonSchedule();
    }

    @Override
    public MemberLessonSchedule_Deleter getDeleter() {
        return database.deleteFromMemberLessonSchedule();
    }
}
