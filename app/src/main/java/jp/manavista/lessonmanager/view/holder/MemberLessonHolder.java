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

    public ImageView expandStatusImage;
    public ImageButton filterImageButton;

    public TextView lessonId;

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

        expandStatusImage = itemView.findViewById(R.id.expand_icon_image);
        filterImageButton = itemView.findViewById(R.id.filter_icon_image);
    }
}
