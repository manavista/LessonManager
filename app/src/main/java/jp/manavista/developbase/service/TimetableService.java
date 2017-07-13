package jp.manavista.developbase.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import jp.manavista.developbase.entity.Timetable;

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

    Observable<Timetable> getListAll();

    Disposable save(Timetable timetable);

    Single<Integer> deleteById(int id);

    void deleteAll();
}
