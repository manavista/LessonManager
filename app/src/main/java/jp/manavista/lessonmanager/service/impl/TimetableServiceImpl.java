package jp.manavista.lessonmanager.service.impl;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.lessonmanager.model.entity.Timetable;
import jp.manavista.lessonmanager.repository.TimeTableRepository;
import jp.manavista.lessonmanager.service.TimetableService;
import jp.manavista.lessonmanager.util.DateTimeUtil;

/**
 *
 * Timetable Service Implements
 *
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
    public Observable<Timetable> getListAll() {
        return repository.getSelector()
                .executeAsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Timetable> add() {

        Timetable timetable = new Timetable();
        timetable.lessonNo = 0; // TODO: 2017/08/24 get max lessonNo in database
        timetable.startTime = DateTimeUtil.parseTime(12,0);
        timetable.endTime = DateTimeUtil.parseTime(13,0);

        return save(timetable)
                .flatMapObservable(new Function<Timetable, ObservableSource<? extends Timetable>>() {
                    @Override
                    public ObservableSource<? extends Timetable> apply(@NonNull Timetable timetable) throws Exception {
                        return getListAll();
                    }
                });
    }

    @Override
    public Observable<Timetable> delete(int id) {
        return deleteById(id)
                .flatMapObservable(new Function<Integer, ObservableSource<? extends Timetable>>() {
                    @Override
                    public ObservableSource<? extends Timetable> apply(@NonNull Integer integer) throws Exception {
                        return getListAll();
                    }
                });
    }

    @Override
    public Observable<Timetable> update(Timetable timetable) {
        return save(timetable)
                .flatMapObservable(new Function<Timetable, ObservableSource<? extends Timetable>>() {
                    @Override
                    public ObservableSource<? extends Timetable> apply(@NonNull Timetable timetable) throws Exception {
                        return getListAll();
                    }
                });
    }

    @Override
    public Single<Timetable> save(final Timetable timetable) {
        return repository.getRelation()
                .upsertAsSingle(timetable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Integer> deleteById(int id) {
        return repository.getDeleter()
                .idEq(id)
                .executeAsSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void deleteAll() {
        repository.getDeleter()
                .executeAsSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
