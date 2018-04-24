package com.edgardrake.flameseeker.lib.utilities.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.edgardrake.flameseeker.lib.utilities.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraUtils {

    public static final int ACTION_OPEN_CAMERA = 0x16;
    public static final int PERMISSION_ACCESS_STORAGE = 0x32;

    private static final String FILE_PROVIDER_SUFFIX = ".fileprovider";

    private Activity context;
    private String imageFilePath;

    @Nullable
    private CameraCallback cameraCallback;

    public CameraUtils(Activity activity, @Nullable CameraCallback callback) {
        context = activity;
        cameraCallback = callback;
    }

    private boolean hasPermissions(@NonNull String... permissions) {
        boolean hasPermission = true;
        for (String permission : permissions) {
            hasPermission = hasPermission && ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
        }
        return hasPermission;
    }

    public void actionOpenCamera() {
        if (hasPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Start open camera intent
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivityForResult(
                    cameraIntent
                        .putExtra(MediaStore.EXTRA_OUTPUT, generateImageURI()),
                    ACTION_OPEN_CAMERA);
            }
        } else {
            requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void requestPermissions(@NonNull String... permissions) {
        ActivityCompat.requestPermissions(context, permissions, PERMISSION_ACCESS_STORAGE);
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults,
                                          @Nullable PermissionDeniedCallback onDeniedListener) {
        if (requestCode == PERMISSION_ACCESS_STORAGE) {
            boolean isGranted = true;
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false;
                    deniedPermissions.add(permissions[i]);
                }
            }

            if (isGranted) {
                actionOpenCamera();
            } else if (onDeniedListener != null) {
                onDeniedListener.onDenied(deniedPermissions);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ACTION_OPEN_CAMERA:
                    if (cameraCallback != null) {
                        cameraCallback.onImageCaptured(imageFilePath);
                    }
                    break;
            }
        }
    }

    /**
     * Get valid URI from file, using {@link FileProvider} if OS version is {@link VERSION_CODES#N}
     * @param file
     * @return
     */
    private Uri getFileUri(@NonNull File file) {
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            return FileProvider.getUriForFile(context,
                context.getPackageName() + FILE_PROVIDER_SUFFIX, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * Save data streamed from intent["output"] in form of JPEG bitmap image
     * then define filename based on defined format
     * @return Temporary image file
     */
    @Nullable
    private File createTempFile() {
        Locale locale = Locale.getDefault();

        // Prepare directory and empty file buffer
        String filename = String.format(locale, "IMG-%s.jpg",
            new SimpleDateFormat("yyyyMMdd-HHmmssSSSS", locale).format(new Date()));
        File albumFolder = context.getApplicationContext()
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File tempImageFile = new File(albumFolder, filename);

        // Store temp image and hold the temp path
        imageFilePath = tempImageFile.getAbsolutePath();
        return tempImageFile;
    }

    /**
     * Generate file image URI. Since Android Nougat, URI follow special convention of using
     * {@link FileProvider} class as service to expose internal private files.
     * @return
     */
    @Nullable
    private Uri generateImageURI() {
        Uri imageURI = null;

        File outputImageFile = createTempFile();
        if (outputImageFile != null) {
            imageURI = getFileUri(outputImageFile);
        }
        return imageURI;
    }

    private void logImage(Bitmap imageBitmap) {
        Logger.log(context)
            .addEntry("width", String.valueOf(imageBitmap.getWidth()))
            .addEntry("height", String.valueOf(imageBitmap.getHeight()))
            .show();
    }
}
