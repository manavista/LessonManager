package jp.manavista.lessonmanager.view.section;

import android.content.Context;
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
import jp.manavista.lessonmanager.model.dto.MemberDto;
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
public class MemberSection extends StatelessSection implements FilterableSection {

    /** Logger tag string */
    private static final String TAG = MemberSection.class.getSimpleName();

    private List<MemberDto> list;
    private List<MemberDto> filteredList;

    /** Context */
    private final Context context;
    /** Operation */
    private final MemberOperation operation;

    /** Constructor(private) */
    private MemberSection(Context context, MemberOperation operation) {

        /*
         * No Header Section
         * Use Header, .headerResourceId(R.layout.section_item_header) addition Builder.
         */
        super(new SectionParameters.Builder(R.layout.container_item_member).build());

        this.context = context;
        this.operation = operation;
    }

    public static MemberSection newInstance(Context context, MemberOperation operation) {
        return new MemberSection(context, operation);
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

        itemHolder.displayName.setText(filteredList.get(position).getDisplayName());

        itemHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click position: " + itemHolder.getAdapterPosition());
                MemberDto dto = filteredList.get(itemHolder.getAdapterPosition());
                Log.d(TAG, dto.toString());
                operation.lessonList(dto.getId(), itemHolder.getAdapterPosition());
            }
        });

        itemHolder.viewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberDto dto = filteredList.get(itemHolder.getAdapterPosition());
                operation.edit(dto.getId(), itemHolder.getAdapterPosition());
            }
        });

        itemHolder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberDto dto = filteredList.get(itemHolder.getAdapterPosition());
                operation.delete(dto.getId(), itemHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public void filter(String query) {
        if( StringUtils.isEmpty(query) ) {
            filteredList = new ArrayList<>(list);
            setVisible(true);
        } else {
            filteredList.clear();
            for( MemberDto dto : list ) {
                final String name = StringUtils.lowerCase(dto.getDisplayName(), Locale.getDefault());
                if( name.contains(query.toLowerCase(Locale.getDefault())) ) {
                    filteredList.add(dto);
                }
            }
            setVisible(!filteredList.isEmpty());
        }
    }

    public List<MemberDto> getList() {
        return this.filteredList;
    }

    public void setList(List<MemberDto> list) {
        this.list = list;
        this.filteredList = new ArrayList<>(list);
    }
}
