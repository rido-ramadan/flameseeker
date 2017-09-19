package com.edgardrake.flameseeker.lib.widget.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edgardrake.flameseeker.lib.utilities.NumberUtils;

import butterknife.ButterKnife;


/**
 * Created by Edgar Drake on 31-Jul-17.
 */

public abstract class DraggableRecyclerViewHolder extends RecyclerView.ViewHolder {

    /**
     * Default elevation defined in layout XML. Useful for getting to default state after being
     * pressed upon.
     */
    private float defaultElevation;

    public DraggableRecyclerViewHolder(View view) {
        super(view);
        defaultElevation = ViewCompat.getElevation(view);
        ButterKnife.bind(this, view);
    }

    public DraggableRecyclerViewHolder(@LayoutRes int resID, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(resID, parent, false));
        ButterKnife.bind(this, itemView);
    }

    /**
     * Get the owning context of this view. Usually view has only context pointing to the activity.
     * @return The owning activity which has inflate this fragment.
     */
    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    protected void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);
        ViewCompat.setElevation(itemView, NumberUtils.dpToPx(16));
    }

    /**
     * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
     * state should be cleared.
     */
    protected void onItemReleased() {
        itemView.setBackgroundColor(Color.WHITE);
        ViewCompat.setElevation(itemView, defaultElevation);
    }
}
