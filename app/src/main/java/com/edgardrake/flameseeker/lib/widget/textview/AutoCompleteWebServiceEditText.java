package com.edgardrake.flameseeker.lib.widget.textview;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Edgar Drake on 19-Oct-17.
 */

public class AutoCompleteWebServiceEditText<T> extends AppCompatAutoCompleteTextView
    implements AdapterView.OnItemClickListener {

    private ServiceListener serviceListener;
    private OnDataSetListener<T> dataListener;
    private boolean isCallWebService;
    private T data;

    public AutoCompleteWebServiceEditText(Context context) {
        super(context);
        initializeEditText();
    }

    public AutoCompleteWebServiceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeEditText();
    }

    public AutoCompleteWebServiceEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeEditText();
    }

    private void initializeEditText() {
        addTextChangedListener(new KeyboardTapListener(300) {
            @Override
            public void onTextChanged(String text) {
                if (isCallWebService) {  // Only call if user is typing as usual
                    // Reset the tag to restrict user from choosing non-desirable value
                    data = null;
                    serviceListener.call(text.toString());
                } else {
                    isCallWebService = true;  // Reset to default state
                }
            }
        });
        setOnItemClickListener(this);
        isCallWebService = true;
    }

    public void setOnServiceCalled(ServiceListener listener) {
        this.serviceListener = listener;
    }

    public void setOnDataSetListener(OnDataSetListener<T> listener) {
        this.dataListener = listener;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        this.isCallWebService = false;  // Prevent web service calling if user click on the result
        this.setText(data != null? data.toString() : null);
        this.selectAll();

        if (dataListener != null) {
            dataListener.onSet(data);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setData((T) parent.getAdapter().getItem(position));
    }

    /**
     * Left this method empty to prevent text replacement before onItemClicked is called
     * @param text
     */
    @Override
    protected void replaceText(CharSequence text) {}

    /**
     * Strictly perform this method on web service call success
     * @param queryResults
     */
    public void onResult(List<T> queryResults) {
        ArrayAdapter<T> adapter = new ArrayAdapter<>(this.getContext(),
            android.R.layout.simple_dropdown_item_1line, queryResults);
        this.setAdapter(adapter);
        this.showDropDown();
    }

    public interface ServiceListener<T> {
        void call(String keyword);
    }

    public interface OnDataSetListener<T> {
        void onSet(T data);
    }
}
