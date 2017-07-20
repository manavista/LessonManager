package jp.manavista.developbase.dto;

import java.sql.Time;

import jp.manavista.developbase.util.DateUtil;
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

    private int lessonNo;
    private Time startTime;
    private Time endTime;


    public String getStartTimeFormatted() {
        return DateUtil.TIME_FORMAT_HHMM.format(startTime);
    }

    public String getEndTimeFormatted() {
        return DateUtil.TIME_FORMAT_HHMM.format(endTime);
    }
}
