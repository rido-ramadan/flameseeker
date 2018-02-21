package com.edgardrake.flameseeker.lib.widget.carousel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
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

    private int width;
    private int height;
    private float aspectRatio;

    private Bitmap[] images;
    private int loadCount;

    private PagerAdapter adapter;

    private Handler threadHandler;
    private Runnable task;

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Ensure that if the view is destroyed by OS lifecycle,
        // it will still retain its previous value
        if (MeasureSpec.getSize(widthMeasureSpec) > 0)
            this.width = MeasureSpec.getSize(widthMeasureSpec);
    }

    private int getRenderedHeight() {
        float height = 0;
        if (aspectRatio > 0) {
            height = this.width / aspectRatio;
        }
        return (int) height;
    }

    private void renderCarousel(int width, int height) {
        this.width = width;
        this.height = height;
        mCarousel.getLayoutParams().width = width;
        mCarousel.getLayoutParams().height = height;
    }

    private void setMinAspectRatio(Bitmap image) {
        // Calculate the image aspect ratio
        float currentAspectRatio = image.getWidth() * 1.0f / image.getHeight();

        // Find the minimum aspect ratio (most like closer to 1.0)
        if (aspectRatio == 0) {
            aspectRatio = currentAspectRatio;
        } else if (currentAspectRatio < aspectRatio) {
            aspectRatio = currentAspectRatio;
        }
    }

    private void setAdapter(CarouselAdapter adapter) {
        this.adapter = adapter;
        mCarousel.setAdapter(this.adapter);
        mIndicator.setViewPager(mCarousel);

        mCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float posOffset, int posOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetCountdown(DELAY_TO_SCROLL);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        resetCountdown(DELAY_TO_SCROLL);
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
        this.images = new Bitmap[images.size()];
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
                        CarouselView.this.images[index] = resource;
                        loadCount++;

                        setMinAspectRatio(resource);

                        if (loadCount == CarouselView.this.images.length) {  // All images loaded
                            onDataLoaded(dataset, onPageClicked);
                        }
                    }
                });
        }
    }

    private <T> void onDataLoaded(List<T> dataset, OnCarouselClicked<T> onClicked) {
        assert images != null && images.length > 0;
        Log.d(TAG, "OnDataLoaded");

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                renderCarousel(mCarousel.getMeasuredWidth(), getRenderedHeight());
            }
        });
        renderCarousel(mCarousel.getMeasuredWidth(), getRenderedHeight());

        CarouselAdapter<T> cAdapter = new CarouselAdapter<>(dataset, images, onClicked);
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
        public Object instantiateItem(ViewGroup container, int position) {

            final View itemView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.carousel_item, container, false);
            final ImageView carouselImage = ButterKnife.findById(itemView, R.id.carousel_image);

            carouselImage.setImageBitmap(images[position]);

            final T data = dataset.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(data);
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
        void onClick(T data);
    }
}
