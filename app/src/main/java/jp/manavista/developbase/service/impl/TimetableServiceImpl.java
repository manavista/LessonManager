package jp.manavista.developbase.service.impl;

import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.entity.Timetable_Selector;
import jp.manavista.developbase.repository.TimeTableRepository;
import jp.manavista.developbase.service.TimetableService;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public class TimetableServiceImpl implements TimetableService {

    /** Timetable Repository */
    private final TimeTableRepository repository;

    /** Constructor */
    public TimetableServiceImpl(TimeTableRepository timeTableRepository) {
        this.repository = timeTableRepository;
    }

    @Override
    public Timetable_Selector getTimetablesAll() {
        return repository.getAll();
    }

    @Override
    public void save(final Timetable timetable) {
        repository.save(timetable);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
