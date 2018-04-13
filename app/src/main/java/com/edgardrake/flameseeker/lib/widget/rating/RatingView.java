package com.edgardrake.flameseeker.lib.widget.rating;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.utilities.NumberUtils;

public class RatingView extends GridLayout {

    private ImageView[] mStars;

    private double rating;
    private int maxRating;
    private int radius;
    private int tintColor;
    private boolean isEnabled;

    @DrawableRes private int drawableEmpty;
    @DrawableRes private int drawableHalf;
    @DrawableRes private int drawableFilled;

    @Nullable RatingChangedListener listener;

    public RatingView(Context context) {
        this(context, null);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs, @StyleRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(attrs);
    }

    private void inflateView(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_rating_view, this, true);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RatingView, 0 ,0);
        try {
            drawableEmpty = a.getResourceId(R.styleable.RatingView_drawableEmpty,
                R.drawable.ic_star_empty_white_24dp);
            drawableHalf = a.getResourceId(R.styleable.RatingView_drawableHalf,
                R.drawable.ic_star_half_white_24dp);
            drawableFilled = a.getResourceId(R.styleable.RatingView_drawableFilled,
                R.drawable.ic_star_full_white_24dp);

            maxRating = a.getInt(R.styleable.RatingView_maxRating, 5);
            setColumnCount(maxRating);

            radius = a.getDimensionPixelSize(
                R.styleable.RatingView_width, NumberUtils.dpToPx(20));
            tintColor = a.getColor(R.styleable.RatingView_tint,
                ContextCompat.getColor(getContext(), android.R.color.transparent));

            setMaxRating(maxRating);

            rating = a.getFloat(R.styleable.RatingView_rating, 0);
            setRating(rating);

            isEnabled = a.getBoolean(R.styleable.RatingView_enabled, true);
            setEnabled(isEnabled);
        } finally {
            a.recycle();
        }
    }

    public void setRating(double rating) {
        if (rating >= 0 && rating <= mStars.length) {
            this.rating = rating;

            double roundedRating = Math.round(rating * 2) * 0.5;
            int major = (int) roundedRating;
            double point = roundedRating - major;
            for (int i = 0; i < major; i++) {
                mStars[i].setImageResource(drawableFilled);
            }
            if (major < mStars.length) { // This code is only applicable if hearts is less than 5
                if (point >= 0.5f && point < 0.9999f) {
                    mStars[major].setImageResource(drawableHalf);
                } else {
                    mStars[major].setImageResource(drawableEmpty);
                }
                for (int j = major + 1; j < mStars.length; j++) {
                    mStars[j].setImageResource(drawableEmpty);
                }
            }

            if (listener != null) {
                listener.OnRatingChanged(this.rating);
            }
        } else {
            throw new RuntimeException("Number must be between 0 - " + mStars.length);
        }
    }

    public double getRating() {
        return rating;
    }

    public void setMaxRating(int maxRating) {
        // Clear all view first
        removeAllViews();

        mStars = new ImageView[maxRating];
        for (int i = 0; i < maxRating; i++) {
            mStars[i] = (ImageView) LayoutInflater.from(getContext())
                .inflate(R.layout.widget_rating_star, this, false);
            mStars[i].setImageResource(drawableEmpty);
            mStars[i].getLayoutParams().width = radius;
            mStars[i].getLayoutParams().height = radius;
            mStars[i].setColorFilter(tintColor);

            final int ratingValue = i + 1;
            mStars[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(ratingValue);
                }
            });
            mStars[i].invalidate();
            addView(mStars[i]);
        }
    }

    public int getMaxRating() {
        return maxRating;
    }

    public void setRadius(int radiusPx) {
        for (ImageView mStar : mStars) {
            mStar.getLayoutParams().width = radiusPx;
            mStar.getLayoutParams().height = radiusPx;
        }
    }

    public void setColorTint(int tintColor) {
        for (ImageView mStar : mStars) {
            mStar.setColorFilter(tintColor);
        }
    }

    public void setOnRatingChangedListener(RatingChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mStars != null) {
            for (ImageView mStar : mStars) {
                if (mStar != null) mStar.setEnabled(enabled);
            }
        }
    }

    public interface RatingChangedListener {
        void OnRatingChanged(double rating);
    }
}
