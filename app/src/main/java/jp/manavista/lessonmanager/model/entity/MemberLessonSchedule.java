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
    public SingleAssociation<Member> member;

    @Column(indexed = true)
    public SingleAssociation<MemberLesson> memberLesson;

    public long memberId;
    public long lessonId;

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
    public int textColor;
    @Column
    public int backgroundColor;

    @Column
    public boolean absent;

    @Column
    @Nullable
    public String memo;


    /**
     *
     * New Instance
     *
     * @param lesson {@code MemberLesson} entity
     * @param member {@code Member} entity
     * @param scheduleDate Schedule Date
     * @return {@code MemberLessonSchedule} entity
     */
    public static MemberLessonSchedule newInstance(MemberLesson lesson, Member member, String scheduleDate) {

        final MemberLessonSchedule schedule = new MemberLessonSchedule();

        schedule.memberId = member.id;
        schedule.lessonId = lesson.id;

        schedule.lessonDate = scheduleDate;
        schedule.lessonStartTime = lesson.startTime;
        schedule.lessonEndTime = lesson.endTime;
        schedule.name = lesson.name;
        schedule.abbr = lesson.abbr;
        schedule.type = lesson.type;
        schedule.location = lesson.location;
        schedule.presenter = lesson.presenter;

        schedule.textColor = lesson.textColor;
        schedule.backgroundColor = lesson.backgroundColor;

        schedule.member = SingleAssociation.just(member);
        schedule.memberLesson = SingleAssociation.just(lesson);

        return schedule;
    }
}
