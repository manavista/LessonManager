package jp.manavista.lessonmanager.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.view.holder.MemberLessonScheduleHolder;
import jp.manavista.lessonmanager.view.operation.MemberLessonScheduleOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

/**
 *
 *
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public class MemberLessonScheduleAdapter extends RecyclerView.Adapter<MemberLessonScheduleHolder> {

    /** Context */
    private final Context context;
    /** Operation */
    private final MemberLessonScheduleOperation operation;

    /** MemberLessonSchedule Value Object list */
    @Getter
    @Setter
    @SuppressWarnings(value = "MismatchedQueryAndUpdateOfCollection")
    private List<MemberLessonScheduleVo> list;

    private MemberLessonScheduleAdapter(Context context, MemberLessonScheduleOperation operation) {
        this.context = context;
        this.operation = operation;
    }

    /**
     *
     * New Instance
     *
     * @param context Context Object
     * @param operation Operation interface
     * @return new {@code MemberLessonScheduleAdapter} object
     */
    public static MemberLessonScheduleAdapter newInstance(Context context, MemberLessonScheduleOperation operation) {
        return new MemberLessonScheduleAdapter(context, operation);
    }

    @Override
    public MemberLessonScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_item_member_lesson_schedule, parent, false);
        return new MemberLessonScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberLessonScheduleHolder holder, int position) {

        val vo = list.get(position);

        holder.scheduleDate.setText(vo.getLessonDate());
        holder.scheduleTime.setText(vo.getLessonDisplayTime());
        holder.scheduleName.setText(vo.getLessonDisplayName());
        holder.scheduleType.setText(vo.getType());
    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }
}
