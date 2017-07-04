package jp.manavista.developbase.entity;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

import java.sql.Time;

/**
 * <p>
 * Overview:<br>
 *
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
}
