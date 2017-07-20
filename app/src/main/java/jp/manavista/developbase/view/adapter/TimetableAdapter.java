package jp.manavista.developbase.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.List;

import jp.manavista.developbase.R;
import jp.manavista.developbase.dto.TimetableDto;

/**
 *
 * Timetable view adapter
 *
 * <p>
 * Overview:<br>
 * Timetable list view adapter.
 * </p>
 */
public class TimetableAdapter extends RecyclerView.Adapter<TimetableHolder> {

    /** Context */
    private final Context context;
    /** Timetable data list */
    private List<TimetableDto> list;

    /** Constructor */
    private TimetableAdapter(Context context) {
        this.context = context;
    }

    public static TimetableAdapter newInstance(Context context) {
        return new TimetableAdapter(context);
    }

    public void setList(List<TimetableDto> list) {
        this.list = list;
    }

    @Override
    public TimetableHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timetable_item_container, parent, false);
        return new TimetableHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimetableHolder holder, final int position) {

        holder.lessonNo.setText(String.valueOf(list.get(position).getLessonNo()));
        holder.startTime.setText(list.get(position).getStartTimeFormatted());
        holder.endTime.setText(list.get(position).getEndTimeFormatted());

        holder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    private void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
}
