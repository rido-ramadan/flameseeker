package com.edgardrake.flameseeker.activity.images.util;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Edgar Drake on 18-Sep-17.
 */

public class ImageEntry implements Comparable<ImageEntry> {
    private long ID;
    private String path;
    private long dateModified;

    public ImageEntry(long ID, String path, long date) {
        this.ID = ID;
        this.path = path;
        this.dateModified = date;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Image ID: %1$d path: %2$s date:%3$s",
            ID, path, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date(dateModified)));
    }

    @Override
    public int compareTo(@NonNull ImageEntry image) {
        return Long.compare(dateModified, image.dateModified);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ImageEntry && ((ImageEntry) obj).ID == ID;
    }
}
