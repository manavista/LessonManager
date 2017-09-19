package jp.manavista.lessonmanager.facade.impl;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import jp.manavista.lessonmanager.facade.MemberLessonScheduleListFacade;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;

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

    private final MemberLessonService memberLessonService;
    private final MemberLessonScheduleService memberLessonScheduleService;

    /** Constructor */
    public MemberLessonScheduleListFacadeImpl (
            MemberLessonService memberLessonService,
            MemberLessonScheduleService memberLessonScheduleService) {
        this.memberLessonService = memberLessonService;
        this.memberLessonScheduleService = memberLessonScheduleService;
    }

    @Override
    public Disposable getListData(final long memberId,
                                  final @NonNull List<MemberLessonVo> lessonVoList,
                                  final @NonNull List<MemberLessonScheduleVo> scheduleVoList,
                                  final RecyclerView view,
                                  final TextView emptyState,
                                  final SectionedRecyclerViewAdapter adapter) {

        lessonVoList.clear();
        scheduleVoList.clear();

        return memberLessonService.getSingleVoListByMemberId(memberId)
                .flatMap(new Function<List<MemberLessonVo>, SingleSource<List<MemberLessonScheduleVo>>> () {
                    @Override
                    public SingleSource<List<MemberLessonScheduleVo>> apply(@io.reactivex.annotations.NonNull List<MemberLessonVo> list) throws Exception {
                        lessonVoList.addAll(list);
                        return memberLessonScheduleService.getSingleVoListByMemberId(memberId);
                    }
                }).subscribe(new Consumer<List<MemberLessonScheduleVo>>() {
                    @Override
                    public void accept(List<MemberLessonScheduleVo> list) throws Exception {

                        scheduleVoList.addAll(list);
                        adapter.notifyDataSetChanged();

                        if( lessonVoList.isEmpty() && scheduleVoList.isEmpty() ) {
                            Log.d(TAG, "List is empty");
                            view.setVisibility(View.GONE);
                            emptyState.setVisibility(View.VISIBLE);
                        } else {
                            view.setVisibility(View.VISIBLE);
                            emptyState.setVisibility(View.GONE);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public Observable<MemberLessonScheduleVo> deleteLessonByLessonId(final long memberId, final long lessonId) {

        final List<Observable<Integer>> targetList = new ArrayList<>();
        targetList.add(memberLessonScheduleService.deleteByLessonId(lessonId).toObservable());
        targetList.add(memberLessonService.deleteById(lessonId).toObservable());

        return Observable.concat(targetList)
                .reduce(0, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer sum, @NonNull Integer rows) throws Exception {
                        return sum + rows;
                    }
                }).flatMapObservable(new Function<Integer, ObservableSource<MemberLessonScheduleVo>>() {
                    @Override
                    public ObservableSource<MemberLessonScheduleVo> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                        return memberLessonScheduleService.getVoListByMemberId(memberId);
                    }
                });
    }
}
