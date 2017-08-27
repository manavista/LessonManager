package jp.manavista.lessonmanager.model.vo;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Time;

import jp.manavista.lessonmanager.model.entity.MemberLesson;
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

        /*
         * Schedule uses lesson's properties.
         * However, if schedule-specific values are set, give priority to them.
         */
        final MemberLesson lesson = entity.memberLesson.get();
        vo.setName(StringUtils.isEmpty(entity.name) ? lesson.name : entity.name);
        vo.setAbbr(StringUtils.isEmpty(entity.abbr) ? lesson.abbr : entity.abbr);
        vo.setType(StringUtils.isEmpty(entity.type) ? lesson.type : entity.type);
        vo.setLocation(StringUtils.isEmpty(entity.location) ? lesson.location: entity.location);
        vo.setPresenter(StringUtils.isEmpty(entity.presenter) ? lesson.presenter : entity.presenter);
        vo.setLessonStartTime(entity.lessonStartTime == null ? lesson.startTime: entity.lessonStartTime);
        vo.setLessonEndTime(entity.lessonEndTime == null ? lesson.endTime: entity.lessonEndTime);

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
