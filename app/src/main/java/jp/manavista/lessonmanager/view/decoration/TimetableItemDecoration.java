/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public class TimetableItemDecoration extends RecyclerView.ItemDecoration {

//    private final Context context;
    private Drawable divider;

    public TimetableItemDecoration(Context context) {

//        this.context = context;

        int[] attrs = new int[] {android.R.attr.listDivider};

        TypedArray array = context.obtainStyledAttributes(attrs);
        divider = array.getDrawable(0);
        array.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, divider.getIntrinsicHeight());
    }

    private void drawVertical(Canvas c, RecyclerView parent) {

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();

        for ( int i = 0 ; i < childCount ; i++ ) {

            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
