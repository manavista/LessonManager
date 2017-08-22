package jp.manavista.lessonmanager.view.adapter;

import android.content.Context;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.dto.MemberLessonDto;
import jp.manavista.lessonmanager.util.ArrayUtil;
import jp.manavista.lessonmanager.view.holder.MemberLessonHolder;
import jp.manavista.lessonmanager.view.operation.MemberLessonOperation;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * Member Lesson Adapter
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public class MemberLessonAdapter extends RecyclerView.Adapter<MemberLessonHolder> {

    /** Logger tag */
    private static final String TAG = MemberLessonAdapter.class.getSimpleName();

    /** Context */
    private final Context context;
    /** Operation */
    private final MemberLessonOperation operation;

    @Getter
    @Setter
    private List<MemberLessonDto> list;

    private MemberLessonAdapter(Context context, MemberLessonOperation operation) {
        this.context = context;
        this.operation = operation;
    }

    /**
     *
     * New Instance
     *
     * @param context Context
     * @param operation entity operation implementation
     * @return new {@code MemberLessonAdapter} instance
     */
    public static MemberLessonAdapter newInstance(Context context, MemberLessonOperation operation) {
        return new MemberLessonAdapter(context, operation);
    }

    @Override
    public MemberLessonHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_item_member_lesson, parent, false);
        return new MemberLessonHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberLessonHolder holder, final int position) {

        if( list == null ) {
            return;
        }

        final MemberLessonDto dto = list.get(position);

//        holder.memberName.setText(dto.getMemberName());
        holder.lessonName.setText(dto.getName());
        holder.lessonType.setText(dto.getType());
//        holder.presenter.setText(dto.getPresenter());
        holder.timetable.setText(dto.getStartTime() + " - " + dto.getEndTime());
//        holder.accentBorder.setBackgroundColor(dto.getBackgroundColor());
        holder.dayOfWeek.setText(buildDayOfWeek(dto.getDayOfWeek()));

        holder.lessonIconImage.setColorFilter(dto.getTextColor());
        DrawableCompat.setTint(holder.lessonIconImage.getBackground(), dto.getBackgroundColor());

        prepareObjectLister(holder, position);
    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }

    /**
     *
     * Build Day of Week String
     *
     * @param dayOfWeek saved string (e.g. "3,5")
     * @return converted display string (e.g. "Tu, Th")
     */
    private String buildDayOfWeek(final String dayOfWeek) {

        /* short name: Su, Mo, Tu, We... */
        final String[] days = context.getResources().getStringArray(R.array.entries_day_of_week_short_name);
        /* day decimal string value: 1, 2, 3... */
        final String[] dayValues = context.getResources().getStringArray(R.array.entry_values_day_of_week);
        final boolean[] index = ArrayUtil.convertIndexFromArray(dayOfWeek, dayValues, ",");

        /* All Day of week true is Everyday */
        if( !ArrayUtils.contains(index, false) ) {
            return "Everyday";
        }

        return ArrayUtil.concatIndexOfArray(days, index, ",");
    }

    private void prepareObjectLister(final MemberLessonHolder holder, final int position) {

        holder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MemberLessonDto dto = list.get(holder.getAdapterPosition());
                operation.delete(dto.getId(), position);
            }
        });

        holder.viewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MemberLessonDto dto = list.get(holder.getAdapterPosition());
                operation.edit(dto, position);
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
