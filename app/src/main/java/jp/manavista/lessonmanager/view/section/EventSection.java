package jp.manavista.lessonmanager.view.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.model.vo.EventVo;
import jp.manavista.lessonmanager.view.holder.EventHolder;
import jp.manavista.lessonmanager.view.operation.EventOperation;
import lombok.val;

/**
 *
 * Event Section
 *
 * <p>
 * Overview:<br>
 * Define the internal section class of adapter used in section recycler view.
 * </p>
 */
public final class EventSection extends StatelessSection implements FilterableSection {

    private List<EventVo> list;
    private List<EventVo> filteredList;

    /** Operation */
    private final EventOperation operation;

    /** Constructor(private) */
    private EventSection(EventOperation operation) {
        super(new SectionParameters.Builder(R.layout.container_event).build());
        this.operation = operation;
    }

    public static EventSection newInstance(EventOperation operation) {
        return new EventSection(operation);
    }

    @Override
    public int getContentItemsTotal() {
        return this.filteredList == null ? 0 : this.filteredList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new EventHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        final EventHolder itemHolder = (EventHolder) holder;
        final EventVo vo = filteredList.get(position);

        itemHolder.name.setText(vo.getName());
        itemHolder.date.setText(vo.getDisplayDate());
        itemHolder.memo.setText(vo.getMemo());

        itemHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final val vo = filteredList.get(itemHolder.getAdapterPosition());
                operation.edit(vo.getId(), itemHolder.getAdapterPosition());
            }
        });

        itemHolder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final val vo = filteredList.get(itemHolder.getAdapterPosition());
                operation.delete(vo.getId(), itemHolder.getAdapterPosition());
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
            for( EventVo vo : list ) {
                final String name = StringUtils.lowerCase(vo.getName(), Locale.getDefault());
                if( name.contains(query.toLowerCase(Locale.getDefault())) ) {
                    filteredList.add(vo);
                }
            }
            setVisible(!filteredList.isEmpty());
        }
    }

    public List<EventVo> getList() {
        return this.filteredList;
    }

    public void setList(List<EventVo> list) {
        this.list = list;
        this.filteredList = new ArrayList<>(list);
    }
}
