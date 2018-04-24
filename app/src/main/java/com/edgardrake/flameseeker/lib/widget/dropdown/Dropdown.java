package com.edgardrake.flameseeker.lib.widget.dropdown;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Edgar Drake on 29-Jul-16.
 */
public class Dropdown extends AppCompatSpinner {

    private OnItemSelectedListener listener;

    public Dropdown(Context context) {
        super(context);
        setBackgroundResource(android.R.color.transparent);
    }

    public Dropdown(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    @Override
    public void setOnItemSelectedListener(@Nullable OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public ArrayAdapter getAdapter() {
        return (ArrayAdapter) super.getAdapter();
    }

    public void setAdapter(ArrayAdapter adapter) {
        super.setAdapter(adapter);
    }

    public <T> void setAdapter(List<T> collection,
                               @LayoutRes int idleView,
                               @LayoutRes int dropdownView,
                               OnItemSelectedListener onItemSelected) {
        final ArrayAdapter<T> adapter = new ArrayAdapter<>(getContext(), idleView, collection);
        adapter.setDropDownViewResource(dropdownView);
        setAdapter(adapter);
        setOnItemSelectedListener(onItemSelected);
    }
}