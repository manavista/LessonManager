package jp.manavista.lessonmanager.facade;

import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;

/**
 *
 * MemberLessonSchedule List Facade
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public interface MemberLessonScheduleListFacade {

    Disposable getListData(long memberId,
                           boolean containPast,
                           List<MemberLessonVo> lessonVoList,
                           List<MemberLessonScheduleVo> scheduleVoList,
                           RecyclerView view,
                           FrameLayout emptyState,
                           SectionedRecyclerViewAdapter adapter);

    Observable<MemberLessonScheduleVo> deleteLessonByLessonId(long memberId, long lessonId);
}
