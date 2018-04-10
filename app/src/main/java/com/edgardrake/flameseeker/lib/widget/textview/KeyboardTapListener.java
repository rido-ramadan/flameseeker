package com.edgardrake.flameseeker.lib.widget.textview;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Custom wrapper for {@link TextWatcher}, with delayed action rather than immediate action.
 * When user change the text inside {@link EditText}, it will queue the action until the timer's run
 * out. Each time the text is changed, the timer will reset itself to the specified user input.<br/>
 * <br/>
 * When the timer's run out, it will invoke the {@link #onTextChanged(String)}
 * which should be defined in the activity/fragment class.
 */
public abstract class KeyboardTapListener implements TextWatcher {

    private Handler requestHandler;
    private Runnable delayedTask;
    private int delayToExecute;

    public KeyboardTapListener(int delayInMillis) {
        delayToExecute = delayInMillis;
        requestHandler = new Handler();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        if (delayedTask != null) {
            requestHandler.removeCallbacks(delayedTask);
        }
        requestHandler.postDelayed(delayedTask = generateTask(text), delayToExecute);
    }

    private Runnable generateTask(final String text) {
        return new Runnable() {
            @Override
            public void run() {
                onTextChanged(text);
            }
        };
    }

    public abstract void onTextChanged(String text);
}
