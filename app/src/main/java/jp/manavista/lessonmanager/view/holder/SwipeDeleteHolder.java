/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.loopeer.itemtouchhelperextension.Extension;

import jp.manavista.lessonmanager.R;

/**
 *
 * Swipe Delete Holder
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public abstract class SwipeDeleteHolder extends RecyclerView.ViewHolder implements Extension {

    public View view;
    public View viewContainer;
    public View viewDelete;

    SwipeDeleteHolder(View itemView) {
        super(itemView);
        this.viewContainer = itemView.findViewById(R.id.view_list_repo_action_container);
        this.viewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
    }

    @Override
    public float getActionWidth() {
        return viewContainer.getWidth();
    }
}
