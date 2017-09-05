package jp.manavista.lessonmanager.model.vo;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;

import jp.manavista.lessonmanager.model.entity.Member;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import lombok.Data;
import lombok.val;

import static jp.manavista.lessonmanager.util.DateTimeUtil.DATE_PATTERN_YYYYMMDD;

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
    private int status;
    private int textColor;
    private int backgroundColor;
    private String memo;

    private Member member;

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
        vo.setStatus(entity.status);
        vo.setTextColor(entity.textColor);
        vo.setBackgroundColor(entity.backgroundColor);
        vo.setMemo(entity.memo);

        vo.setMember(entity.member);

        return vo;
    }

    public String getLessonStartTimeFormatted() {
        return DateTimeUtil.TIME_FORMAT_HHMM.format(lessonStartTime);
    }

    public String getLessonEndTimeFormatted() {
        return DateTimeUtil.TIME_FORMAT_HHMM.format(lessonEndTime);
    }

    public Calendar getLessonStartCalendar() {
        return DateTimeUtil.parseCalendar(lessonDate, DATE_PATTERN_YYYYMMDD, lessonStartTime);
    }

    public Calendar getLessonEndCalendar() {
        return DateTimeUtil.parseCalendar(lessonDate, DATE_PATTERN_YYYYMMDD, lessonEndTime);
    }

    public String getLessonDisplayName() {
        // TODO: 2017/08/27 if exits abbr, concat "(abbr)"
        return getName();
    }

    public String getLessonDisplayTime() {
        return getLessonStartTimeFormatted() + " - " + getLessonEndTimeFormatted();
    }

//    public boolean isMatched(final int year, final int month) {
//        final Calendar calendar = DateTimeUtil.parserCalendar(lessonDate, DATE_PATTERN_YYYYMMDD);
//        return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month;
//    }
}
