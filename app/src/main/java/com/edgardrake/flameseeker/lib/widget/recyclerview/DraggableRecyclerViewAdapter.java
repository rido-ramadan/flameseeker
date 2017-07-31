package com.edgardrake.flameseeker.lib.widget.recyclerview;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import java.util.Collections;
import java.util.List;

/**
 * Created by Edgar Drake on 28-Jul-17.
 */

public abstract class DraggableRecyclerViewAdapter<T, VH extends DraggableRecyclerViewHolder>
    extends RecyclerView.Adapter<VH> implements IDraggableRecyclerViewAdapter, OnStartDragListener {

    protected List<T> dataset;
    private ItemTouchHelper onDragListener;

    public DraggableRecyclerViewAdapter(List<T> dataset) {
        this.dataset = dataset;
        onDragListener = new ItemTouchHelper(new Callback(this));
    }

    /**
     * Wrapper method to attach an instance of {@link DraggableRecyclerViewAdapter}'s subclass to
     * the fragment/activity's RecyclerView.
     * @param view RecyclerView to be attached with this adapter
     */
    public void attachToRecyclerView(RecyclerView view) {
        view.setAdapter(this);
        onDragListener.attachToRecyclerView(view);
    }

    /**
     * Method to be called to fully change the dataset in the RecyclerView adapter. Also possible
     * to be called when data definition happen later.
     * @param source New dataset to override the old dataset
     */
    public void setDataset(List<T> source) {
        this.dataset.clear();
        this.dataset.addAll(source);
        notifyDataSetChanged();
    }

    /**
     * Public method to return all observed dataset inside the adapter.
     * @return
     */
    public List<T> getDataset() {
        return dataset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemDismissed(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dataset, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dataset, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        onDragListener.startDrag(viewHolder);
    }

    /**
     * Default function to start dragging mode when the {@link ViewHolder}'s reorder handle icon is
     * touched
     * @param handleView A view inside {@link ViewHolder} that is used as point of initiation for
     *                   dragging.
     * @param viewHolder ViewHolder of the {@link RecyclerView} being touched.
     */
    protected void setOnDragHandle(View handleView, final RecyclerView.ViewHolder viewHolder) {
        handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDrag(viewHolder);
                }
                return false;
            }
        });
    }

    /**
     * Callback class that implements what function should the {@link RecyclerView} does when being
     * dragged or swiped
     */
    public class Callback extends ItemTouchHelper.Callback {

        private final DraggableRecyclerViewAdapter adapter;

        public Callback(DraggableRecyclerViewAdapter adapter) {
            this.adapter = adapter;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            int dragFlags;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            } else {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
            adapter.onItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
            adapter.onItemDismissed(viewHolder.getAdapterPosition());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
            // We only want the active item
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof DraggableRecyclerViewHolder) {
                    DraggableRecyclerViewHolder draggableHolder =
                        (DraggableRecyclerViewHolder) viewHolder;
                    draggableHolder.onItemSelected();
                }
            }

            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            if (viewHolder instanceof DraggableRecyclerViewHolder) {
                DraggableRecyclerViewHolder draggableHolder =
                    (DraggableRecyclerViewHolder) viewHolder;
                draggableHolder.onItemReleased();
            }
        }
    }
}
