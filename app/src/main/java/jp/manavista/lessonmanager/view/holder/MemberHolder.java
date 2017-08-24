package jp.manavista.lessonmanager.view.holder;

import android.view.View;
import android.widget.TextView;

import com.loopeer.itemtouchhelperextension.Extension;

import jp.manavista.lessonmanager.R;

/**
 *
 * Member Holder
 *
 * <p>
 * Overview:<br>
 * Recycle view holder for Member list<br>
 * Adding extension library to interface to define behavior at swipe.
 * </p>
 *
 * @see Extension extension library interface
 */
public class MemberHolder extends SwipeDeleteHolder {

    public View viewEdit;
    public TextView displayName;

    public MemberHolder(View itemView) {

        super(itemView);

        super.view = itemView.findViewById(R.id.item_member);
        this.viewEdit = itemView.findViewById(R.id.view_list_repo_action_edit);

        this.displayName = itemView.findViewById(R.id.member_display_name);
    }

}
