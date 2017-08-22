package jp.manavista.lessonmanager.view.holder;

import android.view.View;
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

    public View viewOther;
    public View viewEdit;

    public ImageView lessonIconImage;
//    public TextView accentBorder;
//    public TextView memberName;
    public TextView dayOfWeek;
    public TextView timetable;
    public TextView lessonName;
    public TextView lessonType;
//    public TextView presenter;
//    public ImageView infoIcon;

    /** Constructor */
    public MemberLessonHolder(View itemView) {

        super(itemView);

        super.view = itemView.findViewById(R.id.item_member_lesson);
        viewEdit = itemView.findViewById(R.id.view_list_repo_action_edit);

        lessonIconImage = itemView.findViewById(R.id.lesson_icon_image);
//        accentBorder = itemView.findViewById(R.id.accent_border);
//        memberName = itemView.findViewById(R.id.member_name);
        dayOfWeek = itemView.findViewById(R.id.day_of_week);
        timetable = itemView.findViewById(R.id.timetable);
        lessonName = itemView.findViewById(R.id.lesson_name);
        lessonType = itemView.findViewById(R.id.lesson_type);
//        presenter = itemView.findViewById(R.id.presenter);
//        infoIcon = itemView.findViewById(R.id.info_icon_image);
    }
}
