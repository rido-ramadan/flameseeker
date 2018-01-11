package com.edgardrake.flameseeker.activity.images;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.base.BaseActivity;

public class MultiImagePickerActivity extends BaseActivity {

    private static final String LIMIT = "limit";

    private int limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_picker);

        limit = getIntent().getIntExtra(LIMIT, 0);
        getSupportActionBar().setTitle(getString(R.string.choose_image_limit, limit));

        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment, AlbumPickerFragment.newInstance(limit))
            .commit();
    }

    public static void startThisActivityForResult(Activity activity, int limit, int code) {
        activity.startActivityForResult(
            new Intent(activity, MultiImagePickerActivity.class).putExtra(LIMIT, limit),
            code);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_multi_image_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_camera:
                return true;
            case R.id.action_done:
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
