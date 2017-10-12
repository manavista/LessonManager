/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.service.impl;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
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
    public Observable<TimetableDto> getDtoListAll() {
        return repository.getSelector()
                .executeAsObservable()
                .map(new Function<Timetable, TimetableDto>() {
                    @Override
                    public TimetableDto apply(@NonNull Timetable timetable) throws Exception {
                        return TimetableDto.copy(timetable);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TimetableDto> addDtoList() {

        final Timetable timetable = new Timetable();
        timetable.lessonNo = 0;
        timetable.startTime = DateTimeUtil.parseTime(12, 0);
        timetable.endTime = DateTimeUtil.parseTime(13, 0);

        return repository.getSelector().orderByLessonNoDesc().executeAsObservable().take(1)
                .switchIfEmpty(Observable.just(timetable))
                .flatMap(new Function<Timetable, ObservableSource<Timetable>>() {
                    @Override
                    public ObservableSource<Timetable> apply(@NonNull Timetable entity) throws Exception {

                        if( entity.id > 0 ) {
                            /* A New Row */
                            entity.id = 0;
                            entity.lessonNo += 1;
                            int spanMinutes = DateTimeUtil.calculateMinuteSpan(entity.startTime, entity.endTime);
                            entity.startTime = DateTimeUtil.addMinutes(entity.endTime, 10);
                            entity.endTime = DateTimeUtil.addMinutes(entity.endTime, spanMinutes + 10);
                        }
                        return Observable.just(entity);
                    }
                })
                .flatMapSingle(new Function<Timetable, SingleSource<Timetable>>() {
                    @Override
                    public SingleSource<Timetable> apply(@NonNull Timetable timetable) throws Exception {
                        return repository.getRelation().upsertAsSingle(timetable);
                    }
                })
                .flatMap(new Function<Timetable, ObservableSource<Timetable>>() {
                    @Override
                    public ObservableSource<Timetable> apply(@NonNull Timetable timetable) throws Exception {
                        return getListAll();
                    }
                })
                .map(new Function<Timetable, TimetableDto>() {
                    @Override
                    public TimetableDto apply(@NonNull Timetable timetable) throws Exception {
                        return TimetableDto.copy(timetable);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
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
    public Observable<TimetableDto> updateDtoList(final Timetable timetable) {
        return repository.getRelation()
                .upsertAsSingle(timetable)
                .flatMapObservable(new Function<Timetable, ObservableSource<? extends Timetable>>() {
                    @Override
                    public ObservableSource<? extends Timetable> apply(@NonNull Timetable timetable) throws Exception {
                        return repository.getSelector().executeAsObservable();
                    }
                })
                .map(new Function<Timetable, TimetableDto>() {
                    @Override
                    public TimetableDto apply(@NonNull Timetable timetable) throws Exception {
                        return TimetableDto.copy(timetable);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
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
    public Single<Integer> deleteAll() {
        return repository.getDeleter()
                .executeAsSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
