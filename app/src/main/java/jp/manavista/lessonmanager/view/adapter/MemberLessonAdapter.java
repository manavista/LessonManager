package jp.manavista.lessonmanager.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.dto.MemberLessonDto;
import jp.manavista.lessonmanager.util.ArrayUtil;
import jp.manavista.lessonmanager.view.holder.MemberLessonHolder;
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

    @Getter
    @Setter
    private List<MemberLessonDto> list;

    private MemberLessonAdapter(Context context) {
        this.context = context;
    }

    public static MemberLessonAdapter newInstance(Context context) {
        return new MemberLessonAdapter(context);
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

        holder.memberName.setText(dto.getMemberName());
        holder.lessonName.setText(dto.getName());
        holder.lessonType.setText(dto.getType());
        holder.presenter.setText(dto.getPresenter());
        holder.timetable.setText(dto.getStartTime() + " - " + dto.getEndTime());
        holder.accentBorder.setBackgroundColor(dto.getAccentColor());

        /* short name: Sun, Mon, Tue, Wed... */
        final String[] days = context.getResources().getStringArray(R.array.entries_day_of_week);
        /* day decimal string value: 1, 2, 3... */
        final String[] dayValues = context.getResources().getStringArray(R.array.entry_values_day_of_week);

        final boolean[] index = ArrayUtil.convertIndexFromArray(dto.getDayOfWeek(), dayValues, ",");
        holder.dayOfWeek.setText(ArrayUtil.concatIndexOfArray(days, index, ", "));
    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }
}
