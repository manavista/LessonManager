/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.holder;

import android.view.View;
import android.widget.TextView;

import com.loopeer.itemtouchhelperextension.Extension;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.view.image.CircleImageView;

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
public final class MemberHolder extends SwipeEditHolder {

    public TextView displayName;
    public CircleImageView photo;

    public MemberHolder(View itemView) {

        super(itemView);

        super.view = itemView.findViewById(R.id.item_member);
        displayName = itemView.findViewById(R.id.member_display_name);
        photo = itemView.findViewById(R.id.member_icon);
    }

}
