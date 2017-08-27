package jp.manavista.lessonmanager.view.holder;

import android.view.View;

import jp.manavista.lessonmanager.R;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public abstract class SwipeEditHolder extends SwipeDeleteHolder {

    public View viewEdit;

    public SwipeEditHolder(View itemView) {
        super(itemView);
        this.viewEdit = itemView.findViewById(R.id.view_list_repo_action_edit);
    }
}
