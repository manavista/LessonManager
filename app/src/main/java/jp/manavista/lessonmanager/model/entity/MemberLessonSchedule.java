/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.model.entity;

import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import java.sql.Time;

import lombok.ToString;
import lombok.val;

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
    public long memberId;
    @Column(indexed = true)
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

    @Column(indexed = true)
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

    @Column(indexed = true)
    public int status;

    @Column
    @Nullable
    public String memo;

    @Column(indexed = true)
    public Member member;


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

        final val entity = new MemberLessonSchedule();

        entity.memberId = member.id;
        entity.lessonId = lesson.id;

        entity.lessonDate = scheduleDate;
        entity.lessonStartTime = lesson.startTime;
        entity.lessonEndTime = lesson.endTime;
        entity.name = lesson.name;
        entity.abbr = lesson.abbr;
        entity.type = lesson.type;
        entity.location = lesson.location;
        entity.presenter = lesson.presenter;

        entity.textColor = lesson.textColor;
        entity.backgroundColor = lesson.backgroundColor;

        entity.member = member;

        return entity;
    }
}
