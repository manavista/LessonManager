package jp.manavista.developbase.service.impl;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
    public Single<List<Timetable>> getListAll() {
        return repository.getAll()
                .executeAsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .toList();
    }

    @Override
    public Disposable save(final Timetable timetable) {
        return repository.getRelation()
                .upsertAsSingle(timetable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe();
    }

    @Override
    public void deleteAll() {
        repository.getDeleter()
                .executeAsSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe();
    }
}
