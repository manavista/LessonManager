package jp.manavista.developbase.dto;

import java.sql.Time;

import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.util.DateTimeUtil;
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
}
