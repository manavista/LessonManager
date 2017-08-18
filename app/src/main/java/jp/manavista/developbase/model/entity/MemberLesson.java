package jp.manavista.developbase.model.entity;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import java.sql.Time;

import lombok.ToString;

/**
 *
 * MemberLesson
 *
 * <p>
 * Overview:<br>
 * Define the regular lesson data of member.
 * </p>
 */
@Table
@ToString
public class MemberLesson {

    @PrimaryKey(autoincrement = true)
    public int id;
    @Column
    public int memberId;
    @Column
    public String name;
    @Column
    public String abbr;
    @Column
    public String type;
    @Column
    public String location;
    @Column
    public String presenter;
    @Column
    public int textColor;
    @Column
    public int backgroundColor;
    @Column
    public Time startTime;
    @Column
    public Time endTime;
    @Column
    public String dayOfWeeks;
    @Column
    public String periodFrom;
    @Column
    public String periodTo;
}
