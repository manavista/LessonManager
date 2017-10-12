/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.constants;

import android.util.SparseArray;

/**
 *
 * Member Lesson Schedule Status
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public enum MemberLessonScheduleStatus {

    SCHEDULED(0),
    SUSPENDED(3),
    ABSENT(8),
    DONE(9),
    UNDEFINED(-1);

    private final int id;

    MemberLessonScheduleStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static SparseArray<MemberLessonScheduleStatus> statusSparseArray() {

        SparseArray<MemberLessonScheduleStatus> resultMap = new SparseArray<>();
        for( MemberLessonScheduleStatus status : values() ) {
            resultMap.put(status.getId(), status);
        }
        return resultMap;
    }
}
