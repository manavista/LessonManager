package jp.manavista.developbase.repository.impl;

import jp.manavista.developbase.entity.OrmaDatabase;
import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.entity.Timetable_Relation;
import jp.manavista.developbase.entity.Timetable_Selector;
import jp.manavista.developbase.repository.TimeTableRepository;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public class TimetableRepositoryImpl extends RepositoryBaseImpl implements TimeTableRepository {

    /** constructor */
    public TimetableRepositoryImpl(OrmaDatabase database) {
        super(database);
    }

    @Override
    public Timetable_Selector getAll() {
        return database.selectFromTimetable();
    }

    @Override
    public void save(Timetable timetable) {
        database.insertIntoTimetable(timetable);
    }

    @Override
    public void deleteAll() {
        Timetable_Relation relation = database.relationOfTimetable();
        relation.deleter().execute();
    }
}
