/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.service.impl;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.model.entity.MemberLesson_Selector;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;
import jp.manavista.lessonmanager.repository.MemberLessonRepository;
import jp.manavista.lessonmanager.service.MemberLessonService;
import jp.manavista.lessonmanager.util.DateTimeUtil;

/**
 *
 * Member Lesson Service Implement
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class MemberLessonServiceImpl implements MemberLessonService {

    /** MemberLesson Repository */
    private final MemberLessonRepository repository;

    /** Constructor */
    public MemberLessonServiceImpl(MemberLessonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<MemberLesson> getById(long id) {
        return repository.getRelation()
                .idEq(id)
                .getAsSingle(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<MemberLessonVo>> getSingleVoListByMemberId(long memberId, boolean containPast) {

        MemberLesson_Selector selector = repository.getSelector();

        if( !containPast ) {
            final Date today = DateTimeUtil.today().getTime();
            selector = selector.periodToGe(DateTimeUtil.DATE_FORMAT_YYYYMMDD.format(today));
        }

        return selector
                .memberIdEq(memberId)
                .executeAsObservable()
                .map(MemberLessonVo::copy)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MemberLesson> save(MemberLesson memberLesson) {
        return repository.getRelation()
                .upsertAsSingle(memberLesson)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Integer> deleteById(long id) {
        return repository.getDeleter()
                .idEq(id)
                .executeAsSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Integer> deleteByMemberId(long memberId) {
        return repository.getDeleter()
                .memberIdEq(memberId)
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
