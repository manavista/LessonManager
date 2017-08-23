package jp.manavista.lessonmanager.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import jp.manavista.lessonmanager.R;

/**
 *
 * Section Title Holder
 *
 * <p>
 * Overview:<br>
 * For Extended Section RecyclerView Holder.<br>
 * This holder class holds the section title of the list.
 * </p>
 */
public class SectionTitleHolder extends RecyclerView.ViewHolder {

    public final TextView sectionTitle;

    public SectionTitleHolder(View itemView) {
        super(itemView);
        sectionTitle = itemView.findViewById(R.id.section_title);
    }
}
