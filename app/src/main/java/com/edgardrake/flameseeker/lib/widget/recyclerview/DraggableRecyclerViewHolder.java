package com.edgardrake.flameseeker.lib.widget.recyclerview;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.utilities.NumberUtils;


/**
 * Created by Edgar Drake on 31-Jul-17.
 */

public abstract class DraggableRecyclerViewHolder extends RecyclerView.ViewHolder {

    private float defaultElevation;

    public DraggableRecyclerViewHolder(View view) {
        super(view);
        defaultElevation = ViewCompat.getElevation(view);
    }

    public DraggableRecyclerViewHolder(ViewGroup parent, @LayoutRes int resID) {
        super(LayoutInflater.from(parent.getContext()).inflate(resID, parent, false));
    }

    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);
        ViewCompat.setElevation(itemView, NumberUtils.pxToDp(16));
    }

    /**
     * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
     * state should be cleared.
     */
    public void onItemReleased() {
        itemView.setBackgroundColor(Color.WHITE);
        ViewCompat.setElevation(itemView, defaultElevation);
    }
}
