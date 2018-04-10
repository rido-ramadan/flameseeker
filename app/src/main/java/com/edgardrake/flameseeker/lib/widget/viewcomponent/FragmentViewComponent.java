package com.edgardrake.flameseeker.lib.widget.viewcomponent;

import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;

/**
 * Created by Edgar Drake on 04-Jan-18.
 */

public abstract class FragmentViewComponent<T extends Fragment> {

    protected ViewGroup mContainer;

    protected T fragment;
    protected boolean isEnabled;

    public FragmentViewComponent(@NonNull T fragment, @IdRes int containerID) {
        View view = fragment.getView();
        assert view != null;
        bind(this, view);
        mContainer = findById(view, containerID);

        this.isEnabled = false;
        this.fragment = fragment;
    }

    @CallSuper
    public FragmentViewComponent enable(boolean enable) {
        isEnabled = enable;
        if (mContainer != null) {
            mContainer.setVisibility(isEnabled? View.VISIBLE : View.GONE);
        }

        return this;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public ViewGroup getView() {
        return mContainer;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public static abstract class ExpandableComponent<T extends Fragment>
        extends FragmentViewComponent {

        @NonNull View mExpander;
        @NonNull ViewGroup mExpandableLayout;

        private boolean isExpanded;

        public ExpandableComponent(@NonNull T fragment,
                                   @IdRes int containerID,
                                   @IdRes int expanderID,
                                   @IdRes int expandableID) {
            super(fragment, containerID);
            isExpanded = false;

            mExpander = findById(getView(), expanderID);
            mExpandableLayout = findById(getView(), expandableID);

            mExpander.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expand(!isExpanded);
                }
            });
        }

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
