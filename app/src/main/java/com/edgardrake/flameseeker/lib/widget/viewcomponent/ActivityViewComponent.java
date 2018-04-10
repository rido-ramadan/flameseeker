package com.edgardrake.flameseeker.lib.widget.viewcomponent;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;

/**
 * Created by Edgar Drake on 04-Jan-18.
 */

public abstract class ActivityViewComponent<T extends Activity> {

    protected ViewGroup mContainer;

    protected T activity;
    protected boolean isEnabled;

    public ActivityViewComponent(T activity, @IdRes int containerID) {
        bind(this, activity);
        mContainer = findById(activity, containerID);

        this.isEnabled = false;
        this.activity = activity;
    }

    @CallSuper
    public ActivityViewComponent enable(boolean enable) {
        isEnabled = enable;
        if (mContainer != null) {
            mContainer.setVisibility(isEnabled? View.VISIBLE : View.GONE);
        }

        return this;
    }

    public Activity getActivity() {
        return activity;
    }

    public ViewGroup getView() {
        return mContainer;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public static abstract class ExpandableComponent<T extends Activity>
        extends ActivityViewComponent {

        @NonNull View mExpander;
        @NonNull ViewGroup mExpandableLayout;

        private boolean isExpanded;

        public ExpandableComponent(@NonNull T activity,
                                   @IdRes int containerID,
                                   @IdRes int expanderID,
                                   @IdRes int expandableID) {
            super(activity, containerID);
            isExpanded = false;
            mExpander = findById(activity, expanderID);
            mExpandableLayout = findById(activity, expandableID);

            mExpander.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expand(!isExpanded);
                }
            });
        }

        @CallSuper
        public ExpandableComponent expand(boolean expand) {
            isExpanded = expand;
            mExpandableLayout.setVisibility(isExpanded? View.VISIBLE : View.GONE);
            return this;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public View getExpanderView() {
            return mExpander;
        }
    }
}
