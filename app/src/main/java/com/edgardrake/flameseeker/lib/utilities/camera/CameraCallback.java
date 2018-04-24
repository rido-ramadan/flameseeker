package com.edgardrake.flameseeker.lib.utilities.camera;

import android.support.annotation.NonNull;

public interface CameraCallback {
    /**
     * Callback invoked when image is taken.
     * @param filePath Absolute file path of the recently image taken.
     */
    void onImageCaptured(@NonNull String filePath);
}
