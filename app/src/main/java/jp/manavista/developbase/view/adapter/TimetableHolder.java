package jp.manavista.developbase.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopeer.itemtouchhelperextension.Extension;

import jp.manavista.developbase.R;
import lombok.Value;

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
public class TimetableHolder extends RecyclerView.ViewHolder implements Extension {

    View view;
    View viewContainer;
    View viewDelete;

    Spinner lessonNo;
    TextView startTime;
    TextView endTime;

    public TimetableHolder(View itemView) {

        super(itemView);

        this.view = itemView.findViewById(R.id.timetable_item);
        this.viewContainer = itemView.findViewById(R.id.view_list_repo_action_container);
        this.viewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);

        this.lessonNo = itemView.findViewById(R.id.lesson_no);
        this.startTime = itemView.findViewById(R.id.start_time);
        this.endTime = itemView.findViewById(R.id.end_time);
    }

    @Override
    public float getActionWidth() {
        return viewContainer.getWidth();
    }
}
