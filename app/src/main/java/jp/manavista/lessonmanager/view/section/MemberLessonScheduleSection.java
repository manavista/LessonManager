package jp.manavista.lessonmanager.view.section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.view.holder.MemberLessonScheduleHolder;
import jp.manavista.lessonmanager.view.holder.SectionTitleHolder;
import jp.manavista.lessonmanager.view.operation.MemberLessonScheduleOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

/**
 *
 * Member Lesson Schedule Section
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public final class MemberLessonScheduleSection extends StatelessSection implements FilterableSection {

    private final static String TAG = MemberLessonScheduleSection.class.getSimpleName();

    /** Section Title */
    @Getter
    @Setter
    private String title;

    private List<MemberLessonScheduleVo> list;
    private List<MemberLessonScheduleVo> filteredList;

    /** Context */
    private final Context context;
    /** Operation */
    private final MemberLessonScheduleOperation operation;

    /** Constructor(private) */
    private MemberLessonScheduleSection(Context context, MemberLessonScheduleOperation operation) {
        super(new SectionParameters.Builder(R.layout.container_item_member_lesson_schedule)
                .headerResourceId(R.layout.section_item_header).build());
        this.context = context;
        this.operation = operation;
    }

    /**
     *
     * New Instance
     *
     * @param context Context
     * @param operation entity operation implementation
     * @return new {@code MemberLessonScheduleSection} instance
     */
    public static MemberLessonScheduleSection newInstance(Context context, MemberLessonScheduleOperation operation) {
        return new MemberLessonScheduleSection(context, operation);
    }

    @Override
    public int getContentItemsTotal() {
        return this.filteredList == null ? 0 : this.filteredList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new MemberLessonScheduleHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new SectionTitleHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        if( filteredList == null ) {
            return;
        }

        final MemberLessonScheduleHolder itemHolder = (MemberLessonScheduleHolder) holder;
        final MemberLessonScheduleVo vo = filteredList.get(position);

        itemHolder.scheduleDate.setText(vo.getLessonDisplayDate());
        itemHolder.scheduleTime.setText(vo.getLessonDisplayTime());
        itemHolder.scheduleName.setText(vo.getLessonDisplayName());
        itemHolder.scheduleType.setText(vo.getType());

        prepareObjectLister(itemHolder, position);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final SectionTitleHolder itemHolder = (SectionTitleHolder) holder;
        itemHolder.sectionTitle.setText(title);
    }

    @Override
    public void filter(String query) {

    }

    public void filterByLessonId(final long lessonId) {

        if( lessonId <= 0 ) {
            Log.w(TAG, "invalid lessonId by filter!");
            return;
        }

        filteredList.clear();

        for( val vo : list ) {
            if( vo.getLessonId() == lessonId ) {
                filteredList.add(vo);
            }
        }
        setVisible(!filteredList.isEmpty());
    }

    public void clearFilter() {
        filteredList.clear();
        filteredList = new ArrayList<>(list);
        setVisible(true);
    }

    public List<MemberLessonScheduleVo> getList() {
        return this.filteredList;
    }

    public void setList(List<MemberLessonScheduleVo> list) {
        this.list = list;
        this.filteredList = new ArrayList<>(list);
    }


    /**
     *
     * Prepare View Lister
     *
     * <p>
     * Overview:
     * Set View lister for schedule list item operation.
     * </p>
     *
     * @param holder list holder model
     * @param position list position
     */
    private void prepareObjectLister(final MemberLessonScheduleHolder holder, final int position) {

        holder.viewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MemberLessonScheduleVo vo = filteredList.get(position);
                operation.edit(vo.getId(), position);
            }
        });
        
        holder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "position: " + position);
                Log.d(TAG, "getAdapterPosition(): " + holder.getAdapterPosition());

                operation.delete(holder.getAdapterPosition());
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MemberLessonScheduleVo vo = filteredList.get(position);
                operation.edit(vo.getId(), position);
            }
        });
    }
}
