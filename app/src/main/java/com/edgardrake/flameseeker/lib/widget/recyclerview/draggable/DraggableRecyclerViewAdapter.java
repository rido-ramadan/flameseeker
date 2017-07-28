package com.edgardrake.flameseeker.lib.widget.recyclerview.draggable;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;
import java.util.List;

/**
 * Created by Edgar Drake on 28-Jul-17.
 */

public abstract class DraggableRecyclerViewAdapter<T>
    extends RecyclerView.Adapter
    implements IDraggableRecyclerViewAdapter {

    private List<T> dataset;
    private ItemTouchHelper onDragListener;

    public DraggableRecyclerViewAdapter(List<T> dataset) {
        this.dataset = dataset;
        onDragListener = new ItemTouchHelper(new Callback(this));
    }

    public static void attachToRecyclerView(DraggableRecyclerViewAdapter adapter, RecyclerView view) {
        view.setAdapter(adapter);
        adapter.onDragListener.attachToRecyclerView(view);
    }

    public void setDataset(List<T> dataset) {
        this.dataset.clear();
        this.dataset.addAll(dataset);
        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

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
     *
     */
    public class Callback extends ItemTouchHelper.Callback {

        private final DraggableRecyclerViewAdapter adapter;

        public Callback(DraggableRecyclerViewAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, 0/*swipeFlags*/);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }
    }
}
