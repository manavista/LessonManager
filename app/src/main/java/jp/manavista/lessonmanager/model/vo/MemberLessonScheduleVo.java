package jp.manavista.lessonmanager.model.vo;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.sql.Time;

import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import lombok.Data;
import lombok.val;

/**
 *
 * MemberLessonSchedule Value Object
 *
 * <p>
 * Overview:<br>
 * 
 * </p>
 */
@Data
public final class MemberLessonScheduleVo implements Serializable {

    private long id;
    private String name;
    private String abbr;
    private String type;
    private String location;
    private String presenter;
    private String lessonDate;
    private Time lessonStartTime;
    private Time lessonEndTime;
    private boolean absent;
    private String memo;

    public static MemberLessonScheduleVo copy(@NonNull MemberLessonSchedule entity) {

        val vo = new MemberLessonScheduleVo();

        vo.setId(entity.id);

        vo.setName(entity.name);
        vo.setAbbr(entity.abbr);
        vo.setType(entity.type);
        vo.setLocation(entity.location);
        vo.setPresenter(entity.presenter);
        vo.setLessonStartTime(entity.lessonStartTime);
        vo.setLessonEndTime(entity.lessonEndTime);

        vo.setLessonDate(entity.lessonDate);
        vo.setAbsent(entity.absent);
        vo.setMemo(entity.memo);

        return vo;
    }

    public String getLessonStartTimeFormatted() {
        return DateTimeUtil.TIME_FORMAT_HHMM.format(lessonStartTime);
    }

    public String getLessonEndTimeFormatted() {
        return DateTimeUtil.TIME_FORMAT_HHMM.format(lessonEndTime);
    }

    public String getLessonDisplayName() {
        // TODO: 2017/08/27 if exits abbr, concat "(abbr)"
        return getName();
    }

    public String getLessonDisplayTime() {
        return getLessonStartTimeFormatted() + " - " + getLessonEndTimeFormatted();
    }
}
