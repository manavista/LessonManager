package jp.manavista.developbase.view.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import jp.manavista.developbase.R;
import jp.manavista.developbase.dto.TimetableDto;
import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.util.DateTimeUtil;
import jp.manavista.developbase.view.operation.TimetableOperation;
import lombok.val;

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
    private List<TimetableDto> list;
    /** Lesson No select list */
    private final List<Integer> lessonNoList;

    /** Private Constructor */
    private TimetableAdapter(Context context, TimetableOperation timetableOperation) {

        this.context = context;
        this.timetableOperation = timetableOperation;

        this.lessonNoList = new ArrayList<>();
        for( int i = 1 ; i <= 10 ; i++ ) {
            this.lessonNoList.add(i);
        }
    }

    public static TimetableAdapter newInstance(Context context, TimetableOperation timetableOperation) {
        return new TimetableAdapter(context, timetableOperation);
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

        val adapter = new ArrayAdapter<Integer>(context, R.layout.timetable_spinner_item, lessonNoList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.lessonNo.setAdapter(adapter);
        holder.lessonNo.setPrompt(context.getString(R.string.timetable_label_lesson_no_select));

        final int lessonPosition = adapter.getPosition(list.get(position).getLessonNo());
        holder.lessonNo.setSelection(lessonPosition);
        holder.lessonNo.setTag(lessonPosition);

        holder.startTime.setText(list.get(position).getStartTimeFormatted());
        holder.endTime.setText(list.get(position).getEndTimeFormatted());

        holder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimetableDto dto = list.get(holder.getAdapterPosition());
                Log.d(TAG, "getAdapterPosition: " + holder.getAdapterPosition() + " TimetableDto id: " + dto.getId());

                timetableOperation.delete(dto.getId());
            }
        });

        holder.lessonNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if( (int) holder.lessonNo.getTag() == i ) {
                    return;
                }
                holder.lessonNo.setTag(i);

                Spinner spinner = (Spinner) adapterView;
                Toast.makeText(context, "selected lesson no is " + spinner.getSelectedItem(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                /* no description */
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
