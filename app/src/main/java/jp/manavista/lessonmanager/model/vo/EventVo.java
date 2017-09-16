package jp.manavista.lessonmanager.model.vo;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;

import jp.manavista.lessonmanager.model.entity.Event;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import lombok.Data;

/**
 *
 * Event Value Object
 *
 * <p>
 * Overview:<br>
 * Definition of objects used to output data on the screen.
 * </p>
 */
@Data
public final class EventVo implements Serializable {

    private long id;
    private String name;
    private String date;
    private String displayDate;
    private int textColor;
    private int backgroundColor;
    private String memo;

    public static EventVo newInstance(Event entity) {

        EventVo vo = new EventVo();
        vo.id = entity.id;
        vo.name = entity.name;
        vo.date = entity.date;
        vo.displayDate = entity.date;
        vo.textColor = entity.textColor;
        vo.backgroundColor = entity.backgroundColor;
        vo.memo = entity.memo;
        return vo;
    }

    public Calendar getStartDateCalendar() throws ParseException {
        final Calendar start = DateTimeUtil.today();
        start.setTime(DateTimeUtil.DATE_FORMAT_YYYYMMDD.parse(date));
        return start;
    }

    public Calendar getEndDateCalendar() throws ParseException {
        final Calendar end = DateTimeUtil.today();
        end.setTime(DateTimeUtil.DATE_FORMAT_YYYYMMDD.parse(date));
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        return end;
    }
}
