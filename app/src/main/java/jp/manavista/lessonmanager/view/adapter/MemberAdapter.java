package jp.manavista.lessonmanager.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.dto.MemberDto;
import jp.manavista.lessonmanager.view.holder.MemberHolder;
import jp.manavista.lessonmanager.view.operation.MemberOperation;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * Member RecycleView Adapter
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberHolder> {

    /** Logger tag */
    private static final String TAG = MemberAdapter.class.getSimpleName();

    /** Context */
    private final Context context;
    /** Member Operation Interface */
    private final MemberOperation operation;
    @Getter
    @Setter
    private List<MemberDto> list;

    /** Constructor(private) */
    private MemberAdapter(Context context, MemberOperation memberOperation) {
        this.context = context;
        this.operation = memberOperation;
    }

    /**
     *
     * New Instance
     *
     * <p>
     * Overview:<br>
     *
     * </p>
     *
     * @param context context object
     * @param memberOperation {@link MemberOperation} implement
     * @return created {@link MemberAdapter} instance.
     */
    public static MemberAdapter newInstance(Context context, MemberOperation memberOperation) {
        return new MemberAdapter(context, memberOperation);
    }

    @Override
    public MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_item_member, parent, false);
        return new MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberHolder holder, final int position) {

        holder.displayName.setText(list.get(position).getDisplayName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click position: " + holder.getAdapterPosition());
                MemberDto dto = list.get(holder.getAdapterPosition());
                Log.d(TAG, dto.toString());
                operation.edit(dto.getId(), holder.getAdapterPosition());
            }
        });

        holder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberDto dto = list.get(holder.getAdapterPosition());
                operation.delete(dto.getId(), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }
}
