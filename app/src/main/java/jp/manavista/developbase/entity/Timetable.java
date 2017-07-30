package jp.manavista.developbase.entity;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import java.sql.Time;

import jp.manavista.developbase.dto.TimetableDto;

/**
 *
 * Timetable entity
 *
 * <p>
 * Overview:<br>
 * Timetable database table.<br>
 * And, define some data type conversion methods.
 * </p>
 */
@Table
public class Timetable {

    @PrimaryKey(autoincrement = true)
    public int id;
    @Column
    public int lessonNo;
    @Column
    public Time startTime;
    @Column
    public Time endTime;


    public static Timetable convert(TimetableDto dto) {
        Timetable entity = new Timetable();
        entity.id = dto.getId();
        entity.lessonNo = dto.getLessonNo();
        entity.startTime = dto.getStartTime();
        entity.endTime = dto.getEndTime();
        return entity;
    }
}
