package jp.manavista.developbase.service;

import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.entity.Timetable_Selector;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public interface TimetableService {

    Timetable_Selector getTimetablesAll();

    void save(Timetable timetable);

    void deleteAll();
}
