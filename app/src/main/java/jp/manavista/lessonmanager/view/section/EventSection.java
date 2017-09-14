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

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public final class EventSection extends StatelessSection implements FilterableSection {

    private List<EventVo> list;
    private List<EventVo> filteredList;

    /** Constructor(private) */
    private EventSection() {
        super(new SectionParameters.Builder(R.layout.container_event).build());
    }

    public static EventSection newInstance() {
        return new EventSection();
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
        itemHolder.memo.setText(vo.getMemo());


        itemHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        itemHolder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
