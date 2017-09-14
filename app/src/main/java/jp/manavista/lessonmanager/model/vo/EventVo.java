package jp.manavista.lessonmanager.model.vo;

import java.io.Serializable;

import jp.manavista.lessonmanager.model.entity.Event;
import lombok.Data;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
@Data
public final class EventVo implements Serializable {

    private long id;
    private String name;
    public String date;
    public int textColor;
    public int backgroundColor;
    public String memo;

    public static EventVo newInstance(Event entity) {

        EventVo vo = new EventVo();
        vo.id = entity.id;
        vo.name = entity.name;
        vo.date = entity.date;
        vo.textColor = entity.textColor;
        vo.backgroundColor = entity.backgroundColor;
        vo.memo = entity.memo;
        return vo;
    }
}
