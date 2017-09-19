package com.edgardrake.flameseeker.lib.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.utilities.NumberUtils;

/**
 * Created by Edgar Drake on 18-Jul-17.
 *
 * An specialize subclass of {@link AppCompatEditText} that is used to show a formatted currency in
 * Indonesian currency style locale to end-user, while providing required raw value in {@link Long}
 * format for the developers. As this widget only provide currency, only positive number allowed,
 * while negative number is considered as invalid value.
 */

public class CurrencyEditText extends AppCompatEditText
    implements TextWatcher, View.OnFocusChangeListener, View.OnTouchListener {

    public static int INVALID_VALUE = -1;

    private boolean isEditMode = true;

    private long value = INVALID_VALUE;
    private String formattedValue = "";
    private long maxValue = INVALID_VALUE;

    public CurrencyEditText(Context context) {
        super(context);
        initializeEditText();
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributeSet(attrs);
        initializeEditText();
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttributeSet(attrs);
        initializeEditText();
    }

    public long getValue() {
        return value;
    }

    public void setMaxValue(long value) {
        maxValue = value;

        // Ensure the EditText's value to reduce to new maximum value while prevent stack overflow
        if (this.value > maxValue) {
            this.value = maxValue;
            setText(String.valueOf(value));
        }
    }

    private void initializeEditText() {
        addTextChangedListener(this);
        setOnFocusChangeListener(this);
    }

    private void applyAttributeSet(AttributeSet attrs) {
        TypedArray set = getContext().obtainStyledAttributes(attrs, R.styleable.CurrencyEditText);
        try {
            maxValue = set.getInteger(R.styleable.CurrencyEditText_maxValue, INVALID_VALUE);
        } finally {
            set.recycle();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isEditMode) {
            // Edit mode is only accessible if user doing edit directly in activity/fragment.
            if (s.length() > 0) {
                String rawValue = s.toString().replaceAll("[^0-9/-]", "");
                value = Long.parseLong(rawValue);
                if (maxValue > INVALID_VALUE && value > maxValue) {
                    value = maxValue;
                }
                formattedValue = NumberUtils.getCurrency(value);

                // Set edit mode to false to avoid infinite recursion
                isEditMode = false;
                // With this, setText in line below will automatically skip to global else
                setText(formattedValue);
                setSelection(formattedValue.length());
            } else {
                formattedValue = "";
                value = INVALID_VALUE;
            }
        } else {
            // Reset edit mode to true, reenable the text change formatting
            isEditMode = true;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setSelection(formattedValue.length());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        setSelection(formattedValue.length());
        return true;
    }
}
