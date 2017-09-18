package com.edgardrake.flameseeker.lib.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Edgar Drake on 18-Sep-17.
 */

public class SquareImageView extends AppCompatImageView {

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dimension = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(dimension, dimension);
    }
}
