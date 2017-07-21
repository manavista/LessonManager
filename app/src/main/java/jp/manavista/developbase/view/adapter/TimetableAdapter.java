package jp.manavista.developbase.view.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import jp.manavista.developbase.R;
import jp.manavista.developbase.dto.TimetableDto;
import jp.manavista.developbase.util.DateTimeUtil;

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

    private static final String TAG = TimetableAdapter.class.getSimpleName();

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

        holder.lessonNo.setText(String.valueOf(list.get(position).getLessonNo()), TextView.BufferType.NORMAL);
        holder.startTime.setText(list.get(position).getStartTimeFormatted());
        holder.endTime.setText(list.get(position).getEndTimeFormatted());

        holder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(holder.getAdapterPosition());
            }
        });

        holder.startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final TextView textView = (TextView) view;
                String[] times = String.valueOf(textView.getText()).split(DateTimeUtil.COLON);

                TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        Time time = DateTimeUtil.parseTime(hourOfDay, minute);
                        textView.setText(DateTimeUtil.TIME_FORMAT_HHMM.format(time));
                    }
                }, Integer.valueOf(times[0]), Integer.valueOf(times[1]), false);
                dialog.show();
            }
        });

        holder.endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final TextView textView = (TextView) view;
                String[] times = String.valueOf(textView.getText()).split(DateTimeUtil.COLON);

                TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        Time time = DateTimeUtil.parseTime(hourOfDay, minute);
                        textView.setText(DateTimeUtil.TIME_FORMAT_HHMM.format(time));
                    }
                }, Integer.valueOf(times[0]), Integer.valueOf(times[1]), false);
                dialog.show();
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
