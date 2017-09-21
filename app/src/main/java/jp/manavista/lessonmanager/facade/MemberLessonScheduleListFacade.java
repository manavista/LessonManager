package jp.manavista.lessonmanager.facade;

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

    Disposable getListData(MemberLessonScheduleListCriteria criteria);

    Observable<MemberLessonScheduleVo> deleteLessonByLessonId(long memberId, long lessonId);
}
