/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.layout.expandable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import jp.manavista.lessonmanager.R;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public class ExpandableLayout extends LinearLayout {

    private Settings mSettings;
    private int mExpandState;
    private ValueAnimator mExpandAnimator;
    private ValueAnimator mParentAnimator;
    private AnimatorSet mExpandScrollAnimatorSet;
    private int mExpandedViewHeight;
    private boolean mIsInit = true;

    private ScrolledParent mScrolledParent;

    private OnExpandListener mOnExpandListener;

    public ExpandableLayout(Context context) {
        super(context);
        init(null);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs) {
        setClickable(true);
        setOrientation(VERTICAL);
        this.setClipChildren(false);
        this.setClipToPadding(false);
        mExpandState = ExpandState.PRE_INIT;
        mSettings = new Settings();
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout);
            mSettings.expandDuration = typedArray.getInt(R.styleable.ExpandableLayout_expDuration, Settings.EXPAND_DURATION);
            mSettings.expandWithParentScroll = typedArray.getBoolean(R.styleable.ExpandableLayout_expWithParentScroll, false);
            mSettings.expandScrollTogether = typedArray.getBoolean(R.styleable.ExpandableLayout_expExpandScrollTogether, true);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new IllegalStateException("ExpandableLayout must has two child view !");
        }
        if (mIsInit) {
            ((MarginLayoutParams) getChildAt(0).getLayoutParams()).bottomMargin = 0;
            MarginLayoutParams marginLayoutParams = ((MarginLayoutParams) getChildAt(1).getLayoutParams());
            marginLayoutParams.bottomMargin = 0;
            marginLayoutParams.topMargin = 0;
            marginLayoutParams.height = 0;
            mExpandedViewHeight = getChildAt(1).getMeasuredHeight();
            mIsInit = false;
            mExpandState = ExpandState.CLOSED;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mSettings.expandWithParentScroll) {
            mScrolledParent = Utils.getScrolledParent(this);
        }
    }

    private int getParentScrollDistance() {
        int distance = 0;

        if (mScrolledParent == null) {
            return distance;
        }

        distance = (int) (getY() + getMeasuredHeight() + mExpandedViewHeight
                - mScrolledParent.scrolledView.getMeasuredHeight());
        for (int index = 0; index < mScrolledParent.childBetweenParentCount; index++) {
            ViewGroup parent = (ViewGroup) getParent();
            distance += parent.getY();
        }

        return distance;
    }


    private void verticalAnimate(final int startHeight, final int endHeight) {
        int distance = getParentScrollDistance();

        final View target = getChildAt(1);
        mExpandAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        mExpandAnimator.addUpdateListener(animation -> {
            target.getLayoutParams().height = (int) animation.getAnimatedValue();
            target.requestLayout();
        });

        mExpandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (endHeight - startHeight < 0) {
                    mExpandState = ExpandState.CLOSED;
                    if (mOnExpandListener != null) {
                        mOnExpandListener.onExpand(false);
                    }
                } else {
                    mExpandState = ExpandState.EXPANDED;
                    if (mOnExpandListener != null) {
                        mOnExpandListener.onExpand(true);
                    }
                }
            }
        });

        mExpandState = mExpandState == ExpandState.EXPANDED ? ExpandState.CLOSING : ExpandState.EXPANDING;
        mExpandAnimator.setDuration(mSettings.expandDuration);
        if (mExpandState == ExpandState.EXPANDING && mSettings.expandWithParentScroll && distance > 0) {

            mParentAnimator = Utils.createParentAnimator(mScrolledParent.scrolledView, distance, mSettings.expandDuration);

            mExpandScrollAnimatorSet = new AnimatorSet();

            if (mSettings.expandScrollTogether) {
                mExpandScrollAnimatorSet.playTogether(mExpandAnimator, mParentAnimator);
            } else {
                mExpandScrollAnimatorSet.playSequentially(mExpandAnimator, mParentAnimator);
            }
            mExpandScrollAnimatorSet.start();

        } else {
            mExpandAnimator.start();
        }
    }

    public void setExpand(boolean expand) {
        if (mExpandState == ExpandState.PRE_INIT) {
            return;
        }
        getChildAt(1).getLayoutParams().height = expand ? mExpandedViewHeight : 0;
        requestLayout();
        mExpandState = expand ? ExpandState.EXPANDED : ExpandState.CLOSED;
    }

    public boolean isExpanded() {
        return mExpandState == ExpandState.EXPANDED;
    }

    public void toggle() {
        if (mExpandState == ExpandState.EXPANDED) {
            close();
        } else if (mExpandState == ExpandState.CLOSED) {
            expand();
        }
    }

    public void expand() {
        verticalAnimate(0, mExpandedViewHeight);
    }

    public void close() {
        verticalAnimate(mExpandedViewHeight, 0);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public interface OnExpandListener {
        void onExpand(boolean expanded);
    }

    public void setOnExpandListener(OnExpandListener onExpandListener) {
        this.mOnExpandListener = onExpandListener;
    }


    public void setExpandScrollTogether(boolean expandScrollTogether) {
        this.mSettings.expandScrollTogether = expandScrollTogether;
    }

    public void setExpandWithParentScroll(boolean expandWithParentScroll) {
        this.mSettings.expandWithParentScroll = expandWithParentScroll;
    }

    public void setExpandDuration(int expandDuration) {
        this.mSettings.expandDuration = expandDuration;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mExpandAnimator != null && mExpandAnimator.isRunning()) {
            mExpandAnimator.cancel();
            mExpandAnimator.removeAllUpdateListeners();
        }
        if (mParentAnimator != null && mParentAnimator.isRunning()) {
            mParentAnimator.cancel();
            mParentAnimator.removeAllUpdateListeners();
        }
        if (mExpandScrollAnimatorSet != null) {
            mExpandScrollAnimatorSet.cancel();
        }
    }

    private class ExpandState {
        static final int PRE_INIT = -1;
        static final int CLOSED = 0;
        static final int EXPANDED = 1;
        static final int EXPANDING = 2;
        static final int CLOSING = 3;
    }

    private static class ScrolledParent {
        ViewGroup scrolledView;
        int childBetweenParentCount;
    }

    private class Settings {
        static final int EXPAND_DURATION = 300;
        int expandDuration = EXPAND_DURATION;
        boolean expandWithParentScroll;
        boolean expandScrollTogether;
    }

    private static class Utils {

        static ScrolledParent getScrolledParent(ViewGroup child) {

            ViewParent parent = child.getParent();
            int childBetweenParentCount = 0;
            while (parent != null) {
                if ((parent instanceof RecyclerView || parent instanceof AbsListView)) {
                    ScrolledParent scrolledParent = new ScrolledParent();
                    scrolledParent.scrolledView = (ViewGroup) parent;
                    scrolledParent.childBetweenParentCount = childBetweenParentCount;
                    return scrolledParent;
                }
                childBetweenParentCount++;
                parent = parent.getParent();
            }
            return null;
        }

        static ValueAnimator createParentAnimator(final View parent, int distance, long duration) {

            ValueAnimator parentAnimator = ValueAnimator.ofInt(0, distance);

            parentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                int lastDy;
                int dy;

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    dy = (int) animation.getAnimatedValue() - lastDy;
                    lastDy = (int) animation.getAnimatedValue();
                    parent.scrollBy(0, dy);
                }
            });
            parentAnimator.setDuration(duration);

            return parentAnimator;
        }
    }
}
