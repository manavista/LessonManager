package jp.manavista.lessonmanager.model.dto;

import android.support.annotation.ColorInt;

import java.io.Serializable;

import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import lombok.Data;

/**
 *
 * MemberLesson Data Transfer Object
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
@Data
public final class MemberLessonDto implements Serializable {

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

    public static MemberLessonDto copy(MemberLesson entity) {

        final MemberLessonDto dto = new MemberLessonDto();
        final MemberDto memberDto = MemberDto.copy(entity.member.get());

        dto.setId(entity.id);
        dto.setMemberId(entity.memberId);
        dto.setMemberName(memberDto.getDisplayName());
        dto.setName(entity.name);
        dto.setAbbr(entity.abbr);
        dto.setType(entity.type);
        dto.setLocation(entity.location);
        dto.setPresenter(entity.presenter);

        dto.setDayOfWeek(entity.dayOfWeeks);
        dto.setStartTime(DateTimeUtil.TIME_FORMAT_HHMM.format(entity.startTime));
        dto.setEndTime(DateTimeUtil.TIME_FORMAT_HHMM.format(entity.endTime));

        dto.setTextColor(entity.textColor);
        dto.setBackgroundColor(entity.backgroundColor);

        return dto;
    }
}
