package com.edgardrake.flameseeker.lib.widget.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Edgar Drake on 26-Mar-18.
 */

public abstract class PagingRecyclerViewAdapter<T> extends RecyclerView.Adapter {

    protected List<T> dataset;
    protected boolean isStop = false;

    public PagingRecyclerViewAdapter(List<T> source) {
        dataset = source;
    }

    /**
     * Wrapper method to attach an instance of {@link DraggableRecyclerViewAdapter}'s subclass to
     * the fragment/activity's RecyclerView.
     * @param view RecyclerView to be attached with this adapter
     */
    public void attachToRecyclerView(RecyclerView view) {
        view.setAdapter(this);
        if (view.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridManager = (GridLayoutManager) view.getLayoutManager();
            gridManager.setSpanSizeLookup(getSpanSize());
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size() + (isStop? 1 : 0);
    }

    /**
     *
     * @return
     */
    public GridLayoutManager.SpanSizeLookup getSpanSize() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        };
    }

    /**
     * Stop load more
     */
    public void stopLoadMore() {
        isStop = true;
        notifyItemRemoved(getItemCount());
    }

    /**
     * Method to be called to fully change the dataset in the RecyclerView adapter. Also possible
     * to be called when data definition happen later.
     * @param source New dataset to override the old dataset
     */
    public void setDataset(List<T> source) {
        if (dataset != source) {
            dataset = source;
            notifyDataSetChanged();
        }
    }

    /**
     * Public method to return all observed dataset inside the adapter.
     * @return
     */
    public List<T> getDataset() {
        return dataset;
    }
}
