package jp.manavista.lessonmanager.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import jp.manavista.lessonmanager.R;

/**
 *
 * Event Holder
 *
 * <p>
 * Overview:<br>
 * Recycle view holder for {@code Event} list<br>
 * Adding extension library to interface to define behavior at swipe.
 * </p>
 */
public final class EventHolder extends SwipeDeleteHolder {

    public ImageView eventIconImage;
    public TextView name;
    public TextView date;
    public TextView memo;

    public EventHolder(View itemView) {
        super(itemView);
        super.view = itemView.findViewById(R.id.item_event);
        eventIconImage = itemView.findViewById(R.id.event_icon_image);
        name = itemView.findViewById(R.id.event_name);
        date = itemView.findViewById(R.id.event_date_text);
        memo = itemView.findViewById(R.id.event_memo);
    }
}
