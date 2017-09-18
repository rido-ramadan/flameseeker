package com.edgardrake.flameseeker.activity.images.util;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Edgar Drake on 15-Sep-17.
 */

public class AlbumEntry implements Serializable {

    private long ID;
    private String name;

    private List<ImageEntry> images;

    public AlbumEntry(long ID, String name) {
        this.ID = ID;
        this.name = name;
        images = new ArrayList<>();
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }


    public List<ImageEntry> getImages() {
        return images;
    }

    public AlbumEntry addImage(ImageEntry image) {
        this.images.add(image);
        return this;
    }

    public String getThumbnail() {
        return getImages().isEmpty()? null : getImages().get(0).getPath();
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Album ID: %1$d name: %2$s contents: %3$d",
            ID, name, images.size());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AlbumEntry && ((AlbumEntry) obj).ID == ID;
    }
}
