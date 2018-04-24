package com.edgardrake.flameseeker.lib.utilities;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.activity.images.util.AlbumEntry;
import com.edgardrake.flameseeker.activity.images.util.ImageEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Edgar Drake on 18-Sep-17.
 */

public class ImageUtils {

    public static List<AlbumEntry> getAllAlbums(Context context) {
        List<AlbumEntry> albums = new ArrayList<>();

        // which image properties are we querying
        String[] projection = new String[] {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED
        };

        String orderBy = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        Cursor cursor = context.getContentResolver()
            .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, orderBy);

        if (cursor == null) {
            return albums;  // Ungraceful exit due to error of not finding any matching query result
        }

        int columnAlbumID = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
        int columnAlbumName = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int columnImageID = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        int columnImageDate = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
        int columnImagePath = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

        while (cursor.moveToNext()) {

            // Create instance album
            AlbumEntry album = new AlbumEntry(
                cursor.getLong(columnAlbumID),
                cursor.getString(columnAlbumName));

            // Check whether list of albums already has this or not
            if (albums.contains(album)) {  // if contains, fetch that existing data
                album = albums.get(albums.indexOf(album));
            } else {  // if not contained, add this new album
                albums.add(album);
            }

            ImageEntry image = new ImageEntry(
                cursor.getLong(columnImageID),
                cursor.getString(columnImagePath),
                cursor.getLong(columnImageDate));
            album.addImage(image);
        }
        cursor.close();

        return albums;
    }

    public static void showImagePreview(Context context, Object imageSource) {
        View mDialog = LayoutInflater.from(context).inflate(R.layout.dialog_image_preview, null);
        ImageView mPreview = ButterKnife.findById(mDialog, R.id.image_preview);
        Glide.with(context).load(imageSource).into(mPreview);

        new AlertDialog.Builder(context)
            .setView(mDialog)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            })
            .create().show();
    }
}
