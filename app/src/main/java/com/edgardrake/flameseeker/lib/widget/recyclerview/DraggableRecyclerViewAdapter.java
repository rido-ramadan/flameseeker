package com.edgardrake.flameseeker.lib.widget.recyclerview;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by Edgar Drake on 28-Jul-17.
 */

public abstract class DraggableRecyclerViewAdapter<T>
    extends RecyclerView.Adapter<DraggableRecyclerViewHolder>
    implements IDraggableRecyclerViewAdapter, OnStartDragListener {

    protected List<T> dataset;
    private ItemTouchHelper onDragListener;

    public DraggableRecyclerViewAdapter(List<T> dataset) {
        this.dataset = dataset;
        onDragListener = new ItemTouchHelper(new Callback(this));
    }

    /**
     * Wrapper method to attach an instance of {@link DraggableRecyclerViewAdapter}'s subclass to
     * the fragment/activity's RecyclerView.
     * @param adapter RecyclerView's adapter instance of {@link DraggableRecyclerViewAdapter}
     * @param view RecyclerView to be attached with adapter from the first arguments
     */
    public static void attachToRecyclerView(DraggableRecyclerViewAdapter adapter, RecyclerView view) {
        view.setAdapter(adapter);
        adapter.onDragListener.attachToRecyclerView(view);
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
    public void onItemDismiss(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
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
     * Default function to start dragging mode when the viewholder's reorder handle icon is touched
     * @param viewHolder Viewholder of the recyclerview being touched.
     * @param event Touch event. In this case, this method only care about being touched,
     *              see{@link MotionEvent#ACTION_DOWN}.
     * @return False, don't know why
     */
    protected boolean onDragHandleTouched(RecyclerView.ViewHolder viewHolder, MotionEvent event) {
        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
            onStartDrag(viewHolder);
        }
        return false;
    }

    /**
     * Callback class that implements what function should the recycler view does when being dragged
     * or swiped
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
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
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
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.onItemDismiss(viewHolder.getAdapterPosition());
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
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
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
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            if (viewHolder instanceof DraggableRecyclerViewHolder) {
                DraggableRecyclerViewHolder draggableHolder =
                    (DraggableRecyclerViewHolder) viewHolder;
                draggableHolder.onItemReleased();
            }
        }
    }
}
