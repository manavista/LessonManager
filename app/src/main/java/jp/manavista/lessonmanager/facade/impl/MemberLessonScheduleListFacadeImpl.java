/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.facade.impl;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import jp.manavista.lessonmanager.facade.MemberLessonScheduleListFacade;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleListCriteria;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;
import lombok.val;

/**
 *
 * MemberLessonSchedule Facade Implementation
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class MemberLessonScheduleListFacadeImpl implements MemberLessonScheduleListFacade {

    private static final String TAG = MemberLessonScheduleListFacadeImpl.class.getSimpleName();

    private final MemberLessonService lessonService;
    private final MemberLessonScheduleService scheduleService;

    /** Constructor */
    public MemberLessonScheduleListFacadeImpl (
            MemberLessonService lessonService,
            MemberLessonScheduleService scheduleService) {
        this.lessonService = lessonService;
        this.scheduleService = scheduleService;
    }

    @Override
    public Disposable getListData(final MemberLessonScheduleListCriteria criteria) {

        Log.d(TAG, criteria.toString());

        val memberId = criteria.getMemberId();

        final List<MemberLessonVo> lessonVoList = new ArrayList<>();
        final List<MemberLessonScheduleVo> scheduleVoList = new ArrayList<>();

        val view = criteria.getView();
        val emptyState = criteria.getEmptyState();

        return lessonService.getSingleVoListByMemberId(memberId, criteria.getContainPastLesson())
                .flatMapObservable(list -> {
                    lessonVoList.addAll(list);
                    return scheduleService.getVoListByMemberId(memberId, criteria.getScheduleStatusIntegerSet());
                }).subscribe(scheduleVoList::add, Throwable::printStackTrace, () -> {

                    criteria.getLessonSection().setList(lessonVoList);
                    criteria.getScheduleSection().setList(scheduleVoList);
                    criteria.getSectionAdapter().notifyDataSetChanged();
                    if( lessonVoList.isEmpty() && scheduleVoList.isEmpty() ) {
                        Log.d(TAG, "List is empty");
                        view.setVisibility(View.GONE);
                        emptyState.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public Observable<MemberLessonScheduleVo> deleteLessonByLessonId(final long memberId, final long lessonId, final Set<String> displayStatusSet) {

        final List<Observable<Integer>> targetList = new ArrayList<>();
        targetList.add(scheduleService.deleteByLessonId(lessonId).toObservable());
        targetList.add(lessonService.deleteById(lessonId).toObservable());

        final val criteria = MemberLessonScheduleListCriteria.builder()
                .scheduleStatusSet(displayStatusSet)
                .build();

        return Observable.concat(targetList)
                .reduce(0, (sum, rows) -> sum + rows).flatMapObservable(integer -> scheduleService.getVoListByMemberId(memberId, criteria.getScheduleStatusIntegerSet()));
    }
}
