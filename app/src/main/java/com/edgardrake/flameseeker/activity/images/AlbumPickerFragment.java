package com.edgardrake.flameseeker.activity.images;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edgardrake.flameseeker.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AlbumPickerFragment extends Fragment {

    public AlbumPickerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_image_picker, container, false);
    }
}
