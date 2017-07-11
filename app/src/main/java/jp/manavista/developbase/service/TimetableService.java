package jp.manavista.developbase.service;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.entity.Timetable_Selector;

/**
 *
 * Timetable Service
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public interface TimetableService {

    Single<List<Timetable>> getListAll();

    Disposable save(Timetable timetable);

    void deleteAll();
}
