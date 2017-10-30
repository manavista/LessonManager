/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.section;

import android.content.Context;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;
import jp.manavista.lessonmanager.util.ArrayUtil;
import jp.manavista.lessonmanager.view.holder.MemberLessonHolder;
import jp.manavista.lessonmanager.view.holder.SectionTitleHolder;
import jp.manavista.lessonmanager.view.layout.expandable.ExpandableLayout;
import jp.manavista.lessonmanager.view.operation.MemberLessonOperation;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * MemberLesson Section
 *
 * <p>
 * Overview:<br>
 * Define the internal section class of adapter used in section recycler view.<br>
 * </p>
 *
 * @see io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
 */
public final class MemberLessonSection extends StatelessSection {

    private static final String TAG = MemberLessonSection.class.getSimpleName();

    /** Section Title */
    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    @SuppressWarnings(value = "MismatchedQueryAndUpdateOfCollection")
    private List<MemberLessonVo> list;

    /** Context */
    private final Context context;
    /** Operation */
    private final MemberLessonOperation operation;

    /** Constructor */
    private MemberLessonSection(Context context, MemberLessonOperation operation) {
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
     * @return new {@code MemberLessonSection} instance
     */
    public static MemberLessonSection newInstance(Context context, MemberLessonOperation operation) {
        return new MemberLessonSection(context, operation);
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

        final MemberLessonHolder itemHolder = (MemberLessonHolder) holder;

        final MemberLessonVo vo = list.get(position);

        itemHolder.lessonId.setText(String.valueOf(vo.getId()));
        itemHolder.lessonName.setText(vo.getName());
        itemHolder.lessonType.setText(vo.getType());
        itemHolder.timetable.setText(String.format("%1$s -", vo.getStartTime()));
        itemHolder.dayOfWeek.setText(buildDayOfWeek(vo.getDayOfWeek()));

        itemHolder.lessonIconImage.setColorFilter(vo.getTextColor());
        DrawableCompat.setTint(itemHolder.lessonIconImage.getBackground(), vo.getBackgroundColor());

        itemHolder.location.setText(StringUtils.defaultIfEmpty(vo.getLocation(), "-"));
        itemHolder.presenter.setText(StringUtils.defaultIfEmpty(vo.getPresenter(), "-"));
        itemHolder.period.setText(String.format(Locale.getDefault(), "%s - %s", vo.getPeriodFrom(), vo.getPeriodTo()));

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
            return context.getResources().getString(R.string.label_member_lesson_list_day_of_week_everyday);
        }

        return ArrayUtil.concatIndexOfArray(days, index, ",");
    }

    private void prepareObjectLister(final MemberLessonHolder holder, final int position) {

        holder.filterImageButton.setOnClickListener(view -> {

            final MemberLessonVo vo = list.get(holder.getAdapterPosition()-1);

            if( view.isSelected() ) {
                operation.clearFilter();
            } else {
                operation.filter(vo.getId());
            }

            view.setSelected(!view.isSelected());
        });

        holder.viewDelete.setOnClickListener(view -> {

            Log.d(TAG, "position " + position);
            Log.d(TAG, "getAdapterPosition() " + holder.getAdapterPosition() );
            final MemberLessonVo vo = list.get(holder.getAdapterPosition()-1);
            operation.delete(vo.getId(), holder.getAdapterPosition()-1);
        });

        holder.viewEdit.setOnClickListener(view -> {
            final MemberLessonVo vo = list.get(position);
            operation.edit(vo, position);
        });

        holder.view.setOnClickListener(view -> {

            if( view instanceof ExpandableLayout) {

                /* force close for other expanded row if exists */
                operation.close();
                ((ExpandableLayout) view).toggle();
            }
        });

        final ExpandableLayout expandableLayout = (ExpandableLayout) holder.view;
        expandableLayout.setOnExpandListener(expanded -> {

            final int resource = expanded
                    ? R.drawable.ic_expand_less_black
                    : R.drawable.ic_expand_more_black;

            final ImageView icon = expandableLayout.findViewById(R.id.expand_icon_image);
            icon.setImageResource(resource);
        });
    }
}
