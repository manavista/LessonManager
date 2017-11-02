/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.facade;

import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleListCriteria;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;

/**
 *
 * MemberLessonSchedule List Facade Interface
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public interface MemberLessonScheduleListFacade {

    /**
     *
     * Get Lesson and Schedule List
     *
     * <p>
     * Overview:<br>
     * Acquire matching lesson and schedule data from the criteria object
     * and update the screen. If there is data, a list is displayed,
     * and if not, a screen showing that it is empty is displayed.
     * </p>
     *
     * @param criteria list condition object
     * @return disposable resource
     */
    Disposable getListData(MemberLessonScheduleListCriteria criteria);

    /**
     *
     * Delete Lesson
     *
     * <p>
     * Overview:<br>
     * Delete lesson and schedule corresponding to the memberId and
     * lessonId specified in the argument. After that, we get the schedule list of memberId.
     * </p>
     *
     * @param memberId memberId
     * @param lessonId member lessonId
     * @param displayStatusSet schedule display target status set
     * @return MemberLessonScheduleVo in disposable resource
     */
    Observable<MemberLessonScheduleVo> deleteLessonByLessonId(long memberId, long lessonId, Set<String> displayStatusSet);
}
