/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.holder;

import android.view.View;
import android.widget.TextView;

import jp.manavista.lessonmanager.R;

/**
 *
 * MemberLessonSchedule Holder
 *
 * <p>
 * Overview:<br>
 * Recycle view holder for {@code MemberLessonSchedule} list<br>
 * Adding extension library to interface to define behavior at swipe.
 * </p>
 */
public final class MemberLessonScheduleHolder extends SwipeEditHolder {

    public TextView scheduleDate;
    public TextView scheduleTime;
    public TextView scheduleName;
    public TextView scheduleType;

    public MemberLessonScheduleHolder(View itemView) {

        super(itemView);
        super.view = itemView.findViewById(R.id.item_member_lesson_schedule);

        scheduleDate = itemView.findViewById(R.id.schedule_date);
        scheduleTime = itemView.findViewById(R.id.schedule_time);
        scheduleName = itemView.findViewById(R.id.schedule_name);
        scheduleType = itemView.findViewById(R.id.schedule_type);
    }
}
