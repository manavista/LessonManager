/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.model.vo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;

import jp.manavista.lessonmanager.constants.MemberLessonScheduleStatus;
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

    private final static String TAG = MemberLessonScheduleVo.class.getSimpleName();

    private long id;
    private long lessonId;
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
        vo.setLessonId(entity.lessonId);

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

    @SuppressWarnings("WeakerAccess")
    public String getLessonEndTimeFormatted() {
        return DateTimeUtil.TIME_FORMAT_HHMM.format(lessonEndTime);
    }

    public Calendar getLessonStartCalendar() {
        return DateTimeUtil.parseCalendar(lessonDate, DATE_PATTERN_YYYYMMDD, lessonStartTime);
    }

    public Calendar getLessonEndCalendar() {
        return DateTimeUtil.parseCalendar(lessonDate, DATE_PATTERN_YYYYMMDD, lessonEndTime);
    }

    /**
     *
     * Lesson Display Date
     *
     * <p>
     * Overview:<br>
     * Display scheduled date for 'YYYY/MM/DD (EEE)' formatted.
     * </p>
     *
     * @return formatted date string
     */
    public String getLessonDisplayDate() {
        return DateTimeUtil.DATE_FORMAT_YYYYMMDDEEE.format(getLessonStartCalendar().getTime());
    }

    public String getLessonDisplayName() {
        return StringUtils.isEmpty(getAbbr())
                ? getName()
                : String.format("%1$s (%2$s)", getName(), getAbbr());
    }

    public String getLessonDisplayTime() {
        return getLessonStartTimeFormatted() + " - " + getLessonEndTimeFormatted();
    }

    /**
     *
     * Get LessonView Color
     *
     * <p>
     * Overview:<br>
     * Get the schedule color to be displayed on the LessonView screen.<br>
     * Roles the display according to the schedule status.
     * </p>
     *
     * @return Color(int)
     */
    public int getLessonViewColor() {

        final int base = getBackgroundColor();

        switch (MemberLessonScheduleStatus.statusSparseArray().get(getStatus())) {

            case SCHEDULED:
                return base;
            case DONE:
                /* alpha range: 0(invisible) - 255(clearly) */
                return Color.argb(64, Color.red(base), Color.green(base), Color.blue(base));
            case ABSENT:
            case SUSPENDED:
                /* @color/grey_300 */
                return Color.parseColor("#E0E0E0");
            default:
                Log.w(TAG, "undefined lesson schedule status!");
                return base;
        }
    }
}
