/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
                .map(TimetableDto::copy)
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
                .flatMap(entity -> {

                    if( entity.id > 0 ) {
                        /* A New Row */
                        entity.id = 0;
                        entity.lessonNo += 1;
                        int spanMinutes = DateTimeUtil.calculateMinuteSpan(entity.startTime, entity.endTime);
                        entity.startTime = DateTimeUtil.addMinutes(entity.endTime, 10);
                        entity.endTime = DateTimeUtil.addMinutes(entity.endTime, spanMinutes + 10);
                    }
                    return Observable.just(entity);
                })
                .flatMapSingle(timetable13 -> repository.getRelation().upsertAsSingle(timetable13))
                .flatMap(timetable12 -> getListAll())
                .map(TimetableDto::copy)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Timetable> delete(int id) {
        return deleteById(id)
                .flatMapObservable(integer -> getListAll());
    }

    @Override
    public Observable<Timetable> update(Timetable timetable) {
        return save(timetable)
                .flatMapObservable(timetable1 -> getListAll());
    }

    @Override
    public Observable<TimetableDto> updateDtoList(final Timetable timetable) {
        return repository.getRelation()
                .upsertAsSingle(timetable)
                .flatMapObservable(timetable12 -> repository.getSelector().executeAsObservable())
                .map(TimetableDto::copy)
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
