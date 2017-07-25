package jp.manavista.developbase.view.adapter;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

/**
 *
 * Timetable list item touch helper extension
 *
 * <p>
 * Overview:<br>
 * Define a callback event when Timetable item is moved.
 * </p>
 */
public class TimetableTouchHelperCallback extends ItemTouchHelperExtension.Callback {

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        /* no description */
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if( dY != 0 && dX == 0 ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        TimetableHolder holder = (TimetableHolder) viewHolder;

        // if no spring swipe, remove comment.
//        if( dX < -holder.viewContainer.getWidth() ) {
//            dX = -holder.viewContainer.getWidth();
//        }

        holder.view.setTranslationX(dX);
    }
}
