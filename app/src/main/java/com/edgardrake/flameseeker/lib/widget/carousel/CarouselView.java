package com.edgardrake.flameseeker.lib.widget.carousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.edgardrake.flameseeker.R;

import junit.framework.Assert;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Edgar Drake on 13-Feb-18.
 */

public class CarouselView extends FrameLayout {

    private static final String TAG = "CarouselView";
    private static final int DELAY_TO_SCROLL = 3000;

    @BindView(R.id.carousel)
    ViewPager mCarousel;
    @BindView(R.id.carousel_indicator)
    CircleIndicator mIndicator;

    private Bitmap[] imageSources;
    private int loadCount;

    private PagerAdapter adapter;

    private Handler threadHandler;
    private Runnable task;
    private int delayToScroll;

    public CarouselView(Context context) {
        super(context);
        initializeView(context);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
        readAttributes(attrs);
    }

    public CarouselView(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context);
        readAttributes(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int maxHeight = 0;
        for (int i = 0; i < mCarousel.getChildCount(); i++) {
            View mChildPage = mCarousel.getChildAt(i);
            mChildPage.measure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            maxHeight = Math.max(maxHeight, mChildPage.getMeasuredHeight());
        }
        if (maxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight,
                MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initializeView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.carousel, this);
        ButterKnife.bind(this);

        threadHandler = new Handler(context.getMainLooper());
        task = new Runnable() {
            @Override
            public void run() {
                actionChangePage();
            }
        };
    }

    private void readAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CarouselView, 0 ,0);
        try {
            delayToScroll = a.getInt(R.styleable.CarouselView_delay, DELAY_TO_SCROLL);
        } finally {
            a.recycle();
        }
    }

    private void setAdapter(CarouselAdapter adapter) {
        this.adapter = adapter;
        mCarousel.setAdapter(this.adapter);
        mIndicator.setViewPager(mCarousel);

        mCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float posOffset, int posOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                resetCountdown(delayToScroll);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        resetCountdown(delayToScroll);
    }

    private void resetCountdown(int delay) {
        // Remove existing handler
        threadHandler.removeCallbacks(task);
        // Reset the timer
        threadHandler.postDelayed(task, delay);
    }

    public <T, V> void setDataset(final List<T> images,
                                  final List<V> dataset,
                                  final OnCarouselClicked<V> onPageClicked) {
        Assert.assertEquals(images.size(), dataset.size());
        this.imageSources = new Bitmap[images.size()];
        loadCount = 0;

        // Load all images in the specified list
        for (int i = 0; i < images.size(); i++) {
            final int index = i;
            T data = images.get(i);  // Image source is unknown class, because it can be anything

            Glide.with(getContext()).load(data).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        imageSources[index] = resource;
                        loadCount++;

                        if (loadCount == imageSources.length) {  // All images loaded
                            onDataLoaded(dataset, onPageClicked);
                        }
                    }
                });
        }
    }

    private <T> void onDataLoaded(List<T> dataset, OnCarouselClicked<T> onClicked) {
        assert imageSources != null && imageSources.length > 0;
        Log.d(TAG, "OnDataLoaded");

        CarouselAdapter<T> cAdapter = new CarouselAdapter<>(dataset, imageSources, onClicked);
        setAdapter(cAdapter);
    }

    private void actionChangePage() {
        int currentPage = mCarousel.getCurrentItem();
        int maxPage = mCarousel.getAdapter().getCount() - 1;
        mCarousel.setCurrentItem(currentPage < maxPage? mCarousel.getCurrentItem() + 1 : 0);
    }

    public static class CarouselAdapter<T> extends PagerAdapter {

        private List<T> dataset;
        private Bitmap[] images;
        private OnCarouselClicked<T> listener;

        public CarouselAdapter(List<T> dataset, Bitmap[] images, OnCarouselClicked<T> onClicked) {
            this.dataset = dataset;
            this.images = images;
            this.listener = onClicked;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            final View itemView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.carousel_item, container, false);
            final ImageView carouselImage = ButterKnife.findById(itemView, R.id.carousel_image);

            carouselImage.setImageBitmap(images[position]);

            final T data = dataset.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(data, position);
                }
            });
            container.addView(itemView);
            return itemView;
        }

        @Override
        public int getCount() {
            return dataset.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Remove banner_item.xml from ViewPager
            container.removeView((View) object);
        }
    }

    public interface OnCarouselClicked<T> {
        void onClick(T data, int position);
    }
}