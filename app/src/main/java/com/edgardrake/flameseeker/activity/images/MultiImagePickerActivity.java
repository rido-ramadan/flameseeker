package com.edgardrake.flameseeker.activity.images;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.base.BaseActivity;

import butterknife.BindView;

public class MultiImagePickerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_picker);

        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment, AlbumPickerFragment.newInstance())
            .commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });
    }

    public static void startThisActivityForResult(Activity activity, int code) {
        activity.startActivityForResult(
            new Intent(activity, MultiImagePickerActivity.class),
            code);
    }

}
