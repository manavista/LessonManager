package jp.manavista.lessonmanager.model.entity;

import android.support.annotation.Nullable;

import com.github.gfx.android.orma.SingleAssociation;
import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import java.sql.Time;

import lombok.ToString;

/**
 *
 * Member Lesson Schedule
 *
 * <p>
 * Overview:<br>
 * Define the lesson schedule data of member.
 * </p>
 */
@Table
@ToString
public class MemberLessonSchedule {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(indexed = true)
    public SingleAssociation<MemberLesson> memberLesson;

    /*
     * Schedule individual property
     *
     * If you do not use the regular name of Lesson and change it temporarily,
     * set the following properties and display it on the screen.
     */
    @Column
    @Nullable
    public String name;
    @Column
    @Nullable
    public String abbr;
    @Column
    @Nullable
    public String type;
    @Column
    @Nullable
    public String location;
    @Column
    @Nullable
    public String presenter;

    @Column
    public String lessonDate;
    @Column
    @Nullable
    public Time lessonStartTime;
    @Column
    @Nullable
    public Time lessonEndTime;

    @Column
    public boolean absent;

    @Column
    @Nullable
    public String memo;
}
