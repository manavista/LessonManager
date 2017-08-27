package jp.manavista.lessonmanager.view.holder;

import android.view.View;
import android.widget.TextView;

import jp.manavista.lessonmanager.R;

/**
 *
 * Timetable list item view holder
 *
 * <p>
 * Overview:<br>
 * Recycle view holder for Timetable list<br>
 * Adding extension library to interface to define behavior at swipe.
 * </p>
 */
public final class TimetableHolder extends SwipeDeleteHolder {

    public TextView lessonNo;
    public TextView startTime;
    public TextView endTime;

    public TimetableHolder(View itemView) {

        super(itemView);

        super.view = itemView.findViewById(R.id.timetable_item);

        this.lessonNo = itemView.findViewById(R.id.lesson_no);
        this.startTime = itemView.findViewById(R.id.start_time);
        this.endTime = itemView.findViewById(R.id.end_time);
    }

}
