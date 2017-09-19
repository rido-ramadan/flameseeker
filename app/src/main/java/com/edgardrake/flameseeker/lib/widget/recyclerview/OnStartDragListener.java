package com.edgardrake.flameseeker.lib.widget.recyclerview;

/**
 * Created by Edgar Drake on 31-Jul-17.
 */

import android.support.v7.widget.RecyclerView;

/**
 * Listener for manual initiation of a drag.
 */
public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.*
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);

}
