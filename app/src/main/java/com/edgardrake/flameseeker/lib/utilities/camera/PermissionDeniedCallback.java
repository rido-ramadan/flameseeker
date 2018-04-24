package com.edgardrake.flameseeker.lib.utilities.camera;

import java.util.Collection;

public interface PermissionDeniedCallback {
    /**
     *
     * @param deniedPermissions
     */
    void onDenied(Collection<String> deniedPermissions);
}
