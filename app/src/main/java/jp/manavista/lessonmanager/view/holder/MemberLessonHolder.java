/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import jp.manavista.lessonmanager.R;

/**
 *
 * Member Lesson Holder
 *
 * <p>
 * Overview:<br>
 * Recycle view holder for {@code MemberLesson} list<br>
 * Adding extension library to interface to define behavior at swipe.
 * </p>
 */
public final class MemberLessonHolder extends SwipeDeleteHolder {

    public View viewEdit;

    public ImageView lessonIconImage;
    public TextView dayOfWeek;
    public TextView timetable;
    public TextView lessonName;
    public TextView lessonType;

    public ImageButton filterImageButton;

    public TextView lessonId;

    /* expand view area */

    public TextView location;
    public TextView presenter;
    public TextView period;

    /** Constructor */
    public MemberLessonHolder(View itemView) {

        super(itemView);

        super.view = itemView.findViewById(R.id.item_member_lesson);
        viewEdit = itemView.findViewById(R.id.view_list_repo_action_edit);

        lessonId = itemView.findViewById(R.id.lesson_id_invisible);

        lessonIconImage = itemView.findViewById(R.id.lesson_icon_image);
        dayOfWeek = itemView.findViewById(R.id.day_of_week);
        timetable = itemView.findViewById(R.id.timetable);
        lessonName = itemView.findViewById(R.id.lesson_name);
        lessonType = itemView.findViewById(R.id.lesson_type);

        filterImageButton = itemView.findViewById(R.id.filter_icon_image);

        location = itemView.findViewById(R.id.location);
        presenter = itemView.findViewById(R.id.presenter);
        period = itemView.findViewById(R.id.period_from_to);
    }
}
