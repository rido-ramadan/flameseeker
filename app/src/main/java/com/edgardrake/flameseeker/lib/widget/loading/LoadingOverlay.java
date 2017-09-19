package com.edgardrake.flameseeker.lib.widget.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.edgardrake.flameseeker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Edgar Drake on 01-Aug-17.
 */

public class LoadingOverlay extends RelativeLayout {

    @BindView(R.id.loading_bar)
    ProgressBar mProgressBar;

    public LoadingOverlay(Context context) {
        super(context);
        initializeView(context);
    }

    public LoadingOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public LoadingOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context);
    }

    private void initializeView(Context context) {
        inflate(context, R.layout.loading_overlay, this);
        ButterKnife.bind(this);
    }

    /**
     * Show the full screen loading overlay, when put in the frontmost of the screen, effectively
     * hiding all the view behind it.
     */
    public void show() {
        setVisibility(VISIBLE);
    }

    /**
     * Hide the full screen loading overlay, use it to uncover the views behind it.
     */
    public void dismiss() {
        setVisibility(GONE);
    }
}
