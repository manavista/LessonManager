package jp.manavista.lessonmanager.model.vo;

import android.support.annotation.ColorInt;

import java.io.Serializable;

import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import lombok.Data;

/**
 *
 * MemberLesson Value Object
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
@Data
public final class MemberLessonVo implements Serializable {

    private long memberId;
    private String memberName;

    private long id;
    private String name;
    private String abbr;
    private String type;
    private String location;
    private String presenter;

    private String dayOfWeek;
    private String startTime;
    private String endTime;

    @ColorInt
    private int textColor;
    @ColorInt
    private int backgroundColor;

    public static MemberLessonVo copy(MemberLesson entity) {

        final MemberLessonVo vo = new MemberLessonVo();
        final MemberVo memberVo = MemberVo.copy(entity.member);

        vo.setId(entity.id);
        vo.setMemberId(entity.memberId);
//        vo.setMemberName(memberVo.getDisplayName());
        vo.setName(entity.name);
        vo.setAbbr(entity.abbr);
        vo.setType(entity.type);
        vo.setLocation(entity.location);
        vo.setPresenter(entity.presenter);

        vo.setDayOfWeek(entity.dayOfWeeks);
        vo.setStartTime(DateTimeUtil.TIME_FORMAT_HHMM.format(entity.startTime));
        vo.setEndTime(DateTimeUtil.TIME_FORMAT_HHMM.format(entity.endTime));

        vo.setTextColor(entity.textColor);
        vo.setBackgroundColor(entity.backgroundColor);

        return vo;
    }
}
