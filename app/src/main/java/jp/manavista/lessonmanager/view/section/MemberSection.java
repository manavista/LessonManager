package jp.manavista.lessonmanager.view.section;

import android.content.Context;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.dto.MemberLessonDto;
import jp.manavista.lessonmanager.util.ArrayUtil;
import jp.manavista.lessonmanager.view.holder.MemberLessonHolder;
import jp.manavista.lessonmanager.view.holder.SectionTitleHolder;
import jp.manavista.lessonmanager.view.operation.MemberLessonOperation;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * Member Section
 *
 * <p>
 * Overview:<br>
 * Define the internal section class of adapter used in section recycler view.<br>
 * </p>
 *
 * @see io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
 */
public class MemberSection extends StatelessSection {

    /** Section Title */
    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    @SuppressWarnings(value = "MismatchedQueryAndUpdateOfCollection")
    private List<MemberLessonDto> list;

    /** Context */
    private final Context context;
    /** Operation */
    private final MemberLessonOperation operation;

    private MemberSection(Context context, MemberLessonOperation operation) {
        super(new SectionParameters.Builder(R.layout.container_item_member_lesson)
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
     * @return new {@code MemberSection} instance
     */
    public static MemberSection newInstance(Context context, MemberLessonOperation operation) {
        return new MemberSection(context, operation);
    }

    @Override
    public int getContentItemsTotal() {
        return this.list == null ? 0 : this.list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new MemberLessonHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new SectionTitleHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        if( list == null ) {
            return;
        }

        MemberLessonHolder itemHolder = (MemberLessonHolder) holder;

        final MemberLessonDto dto = list.get(position);

        itemHolder.lessonName.setText(dto.getName());
        itemHolder.lessonType.setText(dto.getType());
        itemHolder.timetable.setText(dto.getStartTime() + " - " + dto.getEndTime());
        itemHolder.dayOfWeek.setText(buildDayOfWeek(dto.getDayOfWeek()));

        itemHolder.lessonIconImage.setColorFilter(dto.getTextColor());
        DrawableCompat.setTint(itemHolder.lessonIconImage.getBackground(), dto.getBackgroundColor());

        prepareObjectLister(itemHolder, position);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final SectionTitleHolder itemHolder = (SectionTitleHolder) holder;
        itemHolder.sectionTitle.setText(title);
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

                Log.d("getAdapterPosition()", String.valueOf(holder.getAdapterPosition()) );
//                final MemberLessonDto dto = list.get(holder.getAdapterPosition());
                final MemberLessonDto dto = list.get(position);
                operation.delete(dto.getId(), position);
            }
        });

        holder.viewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final MemberLessonDto dto = list.get(holder.getAdapterPosition());
                final MemberLessonDto dto = list.get(position);
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
