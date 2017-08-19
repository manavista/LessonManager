package jp.manavista.lessonmanager.model.dto;

import java.sql.Time;
import java.text.DateFormat;

import jp.manavista.lessonmanager.model.entity.Timetable;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import lombok.Data;

/**
 *
 * Timetable data transfer object
 *
 * <p>
 * Overview:<br>
 * Definition of objects used to input and output data on the screen
 * </p>
 */
@Data
public class TimetableDto {

    /** ID */
    private int id;
    /** LessonNo */
    private int lessonNo;
    /** StartTime */
    private Time startTime;
    /** EndTime */
    private Time endTime;

    /**
     *
     * Copy Object
     *
     * <p>
     * Overview:<br>
     * Copy the entity object according to the dto object.
     * </p>
     *
     * @param entity Timetable entity object
     * @return Timetable data transfer object
     */
    public static TimetableDto copy(Timetable entity) {
        TimetableDto dto = new TimetableDto();
        dto.setId(entity.id);
        dto.setLessonNo(entity.lessonNo);
        dto.setStartTime(entity.startTime);
        dto.setEndTime(entity.endTime);
        return dto;
    }

    public String getStartTimeFormatted() {
        return DateTimeUtil.TIME_FORMAT_HHMM.format(startTime);
    }

    public String getEndTimeFormatted() {
        return DateTimeUtil.TIME_FORMAT_HHMM.format(endTime);
    }

    public int getStartTime(int timeField) {

        if( timeField == DateFormat.HOUR_OF_DAY0_FIELD ) {
            return Integer.valueOf(DateTimeUtil.TIME_FORMAT_H.format(startTime));
        } else if (  timeField == DateFormat.MINUTE_FIELD ) {
            return Integer.valueOf(DateTimeUtil.TIME_FORMAT_m.format(startTime));
        } else {
            throw new RuntimeException("undefined field argument.");
        }
    }

    public int getEndTime(int timeField) {

        if( timeField == DateFormat.HOUR_OF_DAY0_FIELD ) {
            return Integer.valueOf(DateTimeUtil.TIME_FORMAT_H.format(endTime));
        } else if (  timeField == DateFormat.MINUTE_FIELD ) {
            return Integer.valueOf(DateTimeUtil.TIME_FORMAT_m.format(endTime));
        } else {
            throw new RuntimeException("undefined field argument.");
        }
    }
}
