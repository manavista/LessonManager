package jp.manavista.lessonmanager.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.dto.MemberLessonDto;
import jp.manavista.lessonmanager.view.holder.MemberLessonHolder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public class MemberLessonAdapter extends RecyclerView.Adapter<MemberLessonHolder> {

    /** Logger tag */
    private static final String TAG = MemberLessonAdapter.class.getSimpleName();

    @Getter
    @Setter
    private List<MemberLessonDto> list;

    private MemberLessonAdapter() {

    }

    public static MemberLessonAdapter newInstance() {
        return new MemberLessonAdapter();
    }

    @Override
    public MemberLessonHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_item_member_lesson, parent, false);
        return new MemberLessonHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberLessonHolder holder, int position) {

        holder.memberName.setText(list.get(position).getMemberName());
        holder.lessonName.setText(list.get(position).getName());
        holder.lessonType.setText(list.get(position).getType());
        holder.presenter.setText(list.get(position).getPresenter());

    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }
}
