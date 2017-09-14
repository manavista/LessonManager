package jp.manavista.lessonmanager.view.holder;

import android.view.View;
import android.widget.TextView;

import jp.manavista.lessonmanager.R;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public final class EventHolder extends SwipeDeleteHolder {

    public TextView name;
//    public TextView date;
    public TextView memo;

    public EventHolder(View itemView) {
        super(itemView);
        super.view = itemView.findViewById(R.id.item_event);
        this.name = itemView.findViewById(R.id.event_name);
//        this.date = itemView.findViewById(R.id.);
        this.memo = itemView.findViewById(R.id.event_memo);
    }
}
