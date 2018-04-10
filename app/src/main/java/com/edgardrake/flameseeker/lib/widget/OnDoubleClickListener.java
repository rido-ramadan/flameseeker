package com.edgardrake.flameseeker.lib.widget;

import android.view.View;

/**
 * Created by Edgar Drake on 08-Jan-18.
 */

public abstract class OnDoubleClickListener implements View.OnClickListener {

    private static final long DOUBLE_CLICK_TIME_WINDOW = 300; // Double tap with interval < 300ms

    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime < DOUBLE_CLICK_TIME_WINDOW) {
            // Click happen for each click is still between the 300 ms duration
            onDoubleClick(v);
            lastClickTime = 0;  // Reset to 0
        } else {
            onSingleClick(v);
        }
        lastClickTime = currentClickTime;
    }

    public abstract void onSingleClick(View v);
    public abstract void onDoubleClick(View v);
}
