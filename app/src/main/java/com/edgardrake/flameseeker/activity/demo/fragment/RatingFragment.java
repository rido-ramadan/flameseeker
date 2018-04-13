package com.edgardrake.flameseeker.activity.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.widget.rating.RatingView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {

    @BindView(R.id.rating_view)
    RatingView mRating;
    @BindView(R.id.max_rating)
    TextInputEditText mMaxRating;
    @BindView(R.id.rating_star)
    TextInputEditText mCurrentRating;
    @BindView(R.id.rating_apply)
    Button mApplyButton;

    public RatingFragment() {}

    public static RatingFragment newInstance() {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRoot = inflater.inflate(R.layout.fragment_rating, container, false);
        ButterKnife.bind(this, mRoot);
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRating.setOnRatingChangedListener(new RatingView.RatingChangedListener() {
            @Override
            public void OnRatingChanged(double rating) {
                Toast.makeText(getContext(), String.format(Locale.getDefault(),
                    "Rating : %f", rating), Toast.LENGTH_SHORT).show();
            }
        });

        mApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mMaxRating.getText())) {
                    int maxRating = Integer.parseInt(mMaxRating.getText().toString());
                    mRating.setMaxRating(maxRating);
                }

                if (!TextUtils.isEmpty(mCurrentRating.getText())) {
                    double currentRating = Double.parseDouble(mCurrentRating.getText().toString());
                    mRating.setRating(currentRating);
                }
            }
        });
    }
}
