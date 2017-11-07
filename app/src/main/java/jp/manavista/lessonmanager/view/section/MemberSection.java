/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.section;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.vo.MemberVo;
import jp.manavista.lessonmanager.view.holder.MemberHolder;
import jp.manavista.lessonmanager.view.operation.MemberOperation;

/**
 *
 * MemberSection
 *
 * <p>
 * Overview:<br>
 * Define the internal section class of adapter used in section recycler view.<br>
 * </p>
 */
public final class MemberSection extends StatelessSection implements FilterableSection {

    /** Logger tag string */
    private static final String TAG = MemberSection.class.getSimpleName();

    private List<MemberVo> list;
    private List<MemberVo> filteredList;

    /** Operation */
    private final MemberOperation operation;

    /** Constructor(private) */
    private MemberSection(MemberOperation operation) {

        /*
         * No Header Section
         * Use Header, .headerResourceId(R.layout.section_item_header) addition Builder.
         */
        super(new SectionParameters.Builder(R.layout.container_item_member).build());

        this.operation = operation;
    }

    public static MemberSection newInstance(MemberOperation operation) {
        return new MemberSection(operation);
    }

    @Override
    public int getContentItemsTotal() {
        return this.filteredList == null ? 0 : this.filteredList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new MemberHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        final MemberHolder itemHolder = (MemberHolder) holder;
        final MemberVo vo = filteredList.get(position);

        itemHolder.displayName.setText(vo.getDisplayName());

        if( vo.getPhoto() != null ) {
            itemHolder.photo.setImageBitmap(vo.getPhoto());
            itemHolder.photo.setAlpha(1.0f);
        } else {
            itemHolder.photo.setImageResource(R.drawable.ic_person_black);
            itemHolder.photo.setAlpha(0.6f);
        }

        prepareObjectLister(itemHolder, position);

    }

    @Override
    public void filter(String query) {
        if( StringUtils.isEmpty(query) ) {
            filteredList = new ArrayList<>(list);
            setVisible(true);
        } else {
            filteredList.clear();
            for( MemberVo vo : list ) {
                final String name = StringUtils.lowerCase(vo.getDisplayName(), Locale.getDefault());
                if( name.contains(query.toLowerCase(Locale.getDefault())) ) {
                    filteredList.add(vo);
                }
            }
            setVisible(!filteredList.isEmpty());
        }
    }

    public List<MemberVo> getList() {
        return this.filteredList;
    }

    public void setList(List<MemberVo> list) {
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
     * @param itemHolder list holder model
     * @param position list position
     */
    private void prepareObjectLister(final MemberHolder itemHolder, final int position) {

        itemHolder.view.setOnClickListener(view -> {
            Log.d(TAG, "click position: " + itemHolder.getAdapterPosition());
            MemberVo vo = filteredList.get(itemHolder.getAdapterPosition());
            operation.lessonList(vo, itemHolder.getAdapterPosition());
        });

        itemHolder.viewEdit.setOnClickListener(view -> {
            MemberVo vo = filteredList.get(itemHolder.getAdapterPosition());
            operation.edit(vo.getId(), itemHolder.getAdapterPosition());
        });

        itemHolder.viewDelete.setOnClickListener(view -> {
            MemberVo vo = filteredList.get(itemHolder.getAdapterPosition());
            operation.delete(vo.getId(), itemHolder.getAdapterPosition());
        });

    }
}
