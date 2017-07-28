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

    private ItemTouchHelper draggableHelper;

    public DraggableRecyclerViewAdapter(List<T> dataset) {
        this.dataset = dataset;
        draggableHelper = new ItemTouchHelper(new DraggableRecyclerViewCallback(this));
    }

    public static void attachToRecyclerView(DraggableRecyclerViewAdapter adapter, RecyclerView view) {
        view.setAdapter(adapter);
        adapter.draggableHelper.attachToRecyclerView(view);
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
}
