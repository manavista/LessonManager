package jp.manavista.lessonmanager.view.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.List;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
import jp.manavista.lessonmanager.model.entity.Timetable;
import jp.manavista.lessonmanager.util.DateTimeUtil;
import jp.manavista.lessonmanager.view.holder.TimetableHolder;
import jp.manavista.lessonmanager.view.operation.TimetableOperation;
import lombok.Getter;
import lombok.Setter;

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

    /** Logger tag */
    private static final String TAG = TimetableAdapter.class.getSimpleName();

    /** Context */
    private final Context context;
    /** Operation Timetable interface */
    private final TimetableOperation timetableOperation;
    /** Timetable data list */
    @Getter
    @Setter
    @SuppressWarnings(value = "MismatchedQueryAndUpdateOfCollection")
    private List<TimetableDto> list;

    /** Private Constructor */
    private TimetableAdapter(Context context, TimetableOperation timetableOperation) {
        this.context = context;
        this.timetableOperation = timetableOperation;
    }

    public static TimetableAdapter newInstance(Context context, TimetableOperation timetableOperation) {
        return new TimetableAdapter(context, timetableOperation);
    }

    @Override
    public TimetableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_item_timetable, parent, false);
        return new TimetableHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimetableHolder holder, final int position) {

        holder.lessonNo.setText(String.valueOf(list.get(position).getLessonNo()));
        holder.startTime.setText(list.get(position).getStartTimeFormatted());
        holder.endTime.setText(list.get(position).getEndTimeFormatted());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                 * no description
                 * If there is no such definition, deletion processing is executed when clicking
                 * (viewDelete OnClickListener)
                 */
            }
        });

        holder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TimetableDto dto = list.get(holder.getAdapterPosition());
                timetableOperation.delete(dto.getId());
            }
        });

        holder.lessonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timetableOperation.inputLessonNo(view, holder.getAdapterPosition());
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
                        TimetableDto dto = list.get(holder.getAdapterPosition());
                        dto.setStartTime(time);

                        Log.d(TAG, "changed row: " + holder.getAdapterPosition() + "changed dto: " + dto.toString());

                        timetableOperation.update(Timetable.convert(dto));
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
                        TimetableDto dto = list.get(holder.getAdapterPosition());
                        dto.setEndTime(time);

                        Log.d(TAG, "changed row: " + holder.getAdapterPosition() + "changed dto: " + dto.toString());

                        timetableOperation.update(Timetable.convert(dto));

                    }
                }, Integer.valueOf(times[0]), Integer.valueOf(times[1]), false);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }

}
