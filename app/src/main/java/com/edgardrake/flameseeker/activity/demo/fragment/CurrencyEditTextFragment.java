package com.edgardrake.flameseeker.activity.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.base.BaseFragment;
import com.edgardrake.flameseeker.lib.utilities.NumberUtils;
import com.edgardrake.flameseeker.lib.widget.textview.CurrencyEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrencyEditTextFragment extends BaseFragment {

    @BindView(R.id.price_input_1)
    CurrencyEditText mPrice1;
    @BindView(R.id.price_input_2)
    CurrencyEditText mPrice2;
    @BindView(R.id.price_sum)
    TextView mSum;

    public CurrencyEditTextFragment() {}

    public static CurrencyEditTextFragment newInstance() {
        CurrencyEditTextFragment fragment = new CurrencyEditTextFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRoot = inflater.inflate(R.layout.fragment_currency_edit_text, container, false);
        ButterKnife.bind(this, mRoot);
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPrice1.addTextChangedListener(onValueChanged);
        mPrice2.addTextChangedListener(onValueChanged);
    }

    TextWatcher onValueChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            long sum = mPrice1.getValue() + mPrice2.getValue();
            mSum.setText(NumberUtils.getCurrency(sum));
        }
    };
}
