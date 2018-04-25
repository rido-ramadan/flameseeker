package com.edgardrake.flameseeker.activity.images;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.base.BaseActivity;
import com.edgardrake.flameseeker.lib.utilities.camera.CameraCallback;
import com.edgardrake.flameseeker.lib.utilities.camera.CameraUtils;
import com.edgardrake.flameseeker.lib.utilities.camera.PermissionDeniedCallback;

import java.util.ArrayList;
import java.util.Collection;

public class MultiImagePickerActivity extends BaseActivity {

    public static final String SELECTED_IMAGE_PATHS = "selected";
    private static final String LIMIT = "limit";
    private static final String IS_CAMERA_ENABLED = "is_camera_enabled";

    /**
     * Hard limit of how many images can be taken in a single session
     */
    @Nullable private Integer limit;
    private int current;
    private boolean isCameraEnabled = true;

    private CameraUtils cameraUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_picker);

        assert getSupportActionBar() != null;
        int intentLimit = getIntent().getIntExtra(LIMIT, -1);
        limit = intentLimit > -1? intentLimit : null;
        getSupportActionBar().setTitle(getString(limit != null?
            R.string.choose_image_limit : R.string.choose_image_unlimited, limit));

        isCameraEnabled = getIntent().getBooleanExtra(IS_CAMERA_ENABLED, true);

        cameraUtil = new CameraUtils(getActivity(), new CameraCallback() {
            @Override
            public void onImageCaptured(@NonNull String filePath) {
                actionSetImageResult(filePath);
            }
        });

        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment, AlbumPickerFragment.newInstance(limit))
            .commit();
    }

    private static void startThisActivityForResult(Activity activity,
                                                   @Nullable Integer limit,
                                                   boolean enableCamera,
                                                   int code) {
        activity.startActivityForResult(new Intent(activity, MultiImagePickerActivity.class)
                .putExtra(LIMIT, limit)
                .putExtra(IS_CAMERA_ENABLED, enableCamera),
            code);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_multi_image_picker, menu);
        menu.findItem(R.id.action_open_camera).setVisible(isCameraEnabled);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_camera:
                cameraUtil.actionOpenCamera();
                return true;
            case R.id.action_done:
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraUtil.onRequestPermissionResult(requestCode, permissions, grantResults,
            new PermissionDeniedCallback() {
                @Override
                public void onDenied(Collection<String> deniedPermissions) {
                    String message = getString(R.string.permission_request_error,
                        TextUtils.join(", ", deniedPermissions));
                    new AlertDialog.Builder(getActivity())
                        .setMessage(message)
                        .create().show();
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraUtil.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Callback action when user has successfully captured an image with camera
     * @param filePath File path to the recently taken image
     */
    private void actionSetImageResult(@NonNull String filePath) {
        ArrayList<String> imagePaths = new ArrayList<>();
        imagePaths.add(filePath);
        setResult(Activity.RESULT_OK, new Intent().putStringArrayListExtra(
            MultiImagePickerActivity.SELECTED_IMAGE_PATHS, imagePaths));
        finish();
    }

    public static class Builder {

        private Activity activity;
        private int code;
        private Integer limit;
        private int current;
        private boolean enableCamera;

        public Builder(Activity activity, int code) {
            this.activity = activity;
            this.code = code;
        }

        public Builder setLimit(@Nullable Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder setCurrent(int current) {
            this.current = current;
            return this;
        }

        public Builder setCameraEnabled(boolean enabled) {
            this.enableCamera = enabled;
            return this;
        }

        public void build() {
            MultiImagePickerActivity.startThisActivityForResult(
                activity, limit, enableCamera, code);
        }
    }
}
