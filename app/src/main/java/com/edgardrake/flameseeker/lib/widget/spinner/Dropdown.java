package com.edgardrake.flameseeker.lib.widget.spinner;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Edgar Drake on 29-Jul-16.
 */
public class Dropdown extends AppCompatSpinner {
    AdapterView.OnItemSelectedListener listener;

    public Dropdown(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(android.R.color.transparent);
    }

    public Dropdown(Context context) {
        super(context);
        setBackgroundResource(android.R.color.transparent);
    }

    public Dropdown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(android.R.color.transparent);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            listener.onItemSelected(this, getSelectedView(), position, 0);
    }

    public void setOnForcedItemSelectedListener(
        AdapterView.OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public ArrayAdapter getAdapter() {
        return (ArrayAdapter) super.getAdapter();
    }

    public void setAdapter(ArrayAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * Attach an anonymous adapter to this instance of Dropdown view based on the provided list of
     * objects.
     * @param collection A list, source of dataset to be shown in form of dropdown.
     * @param idleView Layout resource ID of the dropdown when idle, not in focus, not expanding.
     * @param dropdownView Layout resource ID of the dropdown when showing the dropdown.
     * @param onItemSelected Method to be called when 1 item is selected.
     */
    public void attachAdapter(List collection, @LayoutRes int idleView, @LayoutRes int dropdownView,
                              AdapterView.OnItemSelectedListener onItemSelected) {
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), idleView, collection);
        adapter.setDropDownViewResource(dropdownView);
        this.setAdapter(adapter);
        this.setOnForcedItemSelectedListener(onItemSelected);
    }
}