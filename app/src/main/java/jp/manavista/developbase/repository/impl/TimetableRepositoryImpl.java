package jp.manavista.developbase.repository.impl;

import jp.manavista.developbase.entity.OrmaDatabase;
import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.entity.Timetable_Deleter;
import jp.manavista.developbase.entity.Timetable_Relation;
import jp.manavista.developbase.entity.Timetable_Selector;
import jp.manavista.developbase.repository.TimeTableRepository;

/**
 *
 * Timetable repository implements
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class TimetableRepositoryImpl extends RepositoryBaseImpl implements TimeTableRepository {

    /** constructor */
    public TimetableRepositoryImpl(OrmaDatabase database) {
        super(database);
    }

    @Override
    public Timetable_Selector getSelector() {
        return database.selectFromTimetable();
    }

    @Override
    public Timetable_Relation getRelation() {
        return database.relationOfTimetable();
    }

    @Override
    public Timetable_Deleter getDeleter() {
        return database.deleteFromTimetable();
    }

}
