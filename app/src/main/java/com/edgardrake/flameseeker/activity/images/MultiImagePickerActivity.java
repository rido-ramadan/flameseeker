package com.edgardrake.flameseeker.activity.images;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
    private static final String TITLE_WITH_LIMIT = "title_with_limit";
    private static final String TITLE_NO_LIMIT = "title_no_limit";

    private static final int DEFAULT_ID_TITLE_WITH_LIMIT = R.string.choose_image_limit;
    private static final int DEFAULT_ID_TITLE_NO_LIMIT = R.string.choose_image_unlimited;

    /**
     * Hard limit of how many images can be taken in a single session
     */
    @Nullable private Integer limit;
    private int current;
    /**
     *
     */
    private boolean isCameraEnabled = true;

    @StringRes private int titleWithLimit;
    @StringRes private int titleNoLimit;

    private CameraUtils cameraUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_picker);

        assert getSupportActionBar() != null;
        int intentLimit = getIntent().getIntExtra(LIMIT, -1);
        limit = intentLimit > -1? intentLimit : null;

        titleWithLimit = getIntent().getIntExtra(TITLE_WITH_LIMIT, DEFAULT_ID_TITLE_WITH_LIMIT);
        titleNoLimit = getIntent().getIntExtra(TITLE_NO_LIMIT, DEFAULT_ID_TITLE_NO_LIMIT);

        getSupportActionBar().setTitle(getString(limit != null?
            titleWithLimit : titleNoLimit, limit));

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

    private static void startThisActivityForResult(@NonNull Activity activity,
                                                   @Nullable Integer limit,
                                                   boolean enableCamera,
                                                   @StringRes int titleWithLimitID,
                                                   @StringRes int titleNoLimitID,
                                                   int code) {
        activity.startActivityForResult(new Intent(activity, MultiImagePickerActivity.class)
                .putExtra(LIMIT, limit)
                .putExtra(IS_CAMERA_ENABLED, enableCamera)
                .putExtra(TITLE_WITH_LIMIT, titleWithLimitID)
                .putExtra(TITLE_NO_LIMIT, titleNoLimitID)
            , code);
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

        @NonNull private Activity activity;
        private int code;
        @Nullable private Integer limit;
        private int current;
        private boolean enableCamera;
        @StringRes private int titleWithLimit = DEFAULT_ID_TITLE_WITH_LIMIT;
        @StringRes private int titleNoLimit = DEFAULT_ID_TITLE_NO_LIMIT;

        public Builder(@NonNull Activity activity, int code) {
            this.activity = activity;
            this.code = code;
        }

        public Builder setLimit(@Nullable Integer limit) {
            if (limit != null && limit < 0)
                throw new RuntimeException("Image limit must not be a negative number");
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

        public Builder setTitle(@StringRes int titleWithLimitID, @StringRes int titleNoLimitID) {
            this.titleWithLimit = titleWithLimitID;
            this.titleNoLimit = titleNoLimitID;
            return this;
        }

        public void build() {
            MultiImagePickerActivity.startThisActivityForResult(
                activity, limit, enableCamera, titleWithLimit, titleNoLimit, code);
        }
    }
}
