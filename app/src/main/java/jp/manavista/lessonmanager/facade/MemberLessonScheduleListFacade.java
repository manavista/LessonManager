package jp.manavista.lessonmanager.facade;

import android.util.Pair;

import java.util.List;

import io.reactivex.Single;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public interface MemberLessonScheduleListFacade {

    Single<Pair<List<MemberLessonVo>, List<MemberLessonScheduleVo>>> getListPair(long memberId);

    void createListPair(long memberId, Pair<List<MemberLessonVo>, List<MemberLessonScheduleVo>> listPair);
}
