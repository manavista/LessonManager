package jp.manavista.lessonmanager.model.vo;

import java.io.Serializable;

import jp.manavista.lessonmanager.model.entity.Event;
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
}
