package com.edgardrake.flameseeker.activity.images;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.activity.images.util.AlbumEntry;
import com.edgardrake.flameseeker.lib.base.BaseFragment;
import com.edgardrake.flameseeker.lib.utilities.ImageUtils;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewAdapter;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class AlbumPickerFragment extends BaseFragment {

    private static final String LIMIT = "limit";

    private static final int REQUEST_PERMISSION_STORAGE = 0x32;

    @BindView(R.id.album_list)
    RecyclerView mAlbumList;

    private List<AlbumEntry> albums;
    @Nullable private Integer limit;

    public static AlbumPickerFragment newInstance(@Nullable Integer limit) {
        AlbumPickerFragment fragment = new AlbumPickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(LIMIT, limit);
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumPickerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        albums = new ArrayList<>();
        limit = (Integer) getArguments().getSerializable(LIMIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_picker, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAlbumList.setHasFixedSize(true);
        new AlbumAdapter(albums).attachToRecyclerView(mAlbumList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGalleryAlbum();  // Having storage permission will allow us to open the gallery
        } else {
            // Open request permission
            String[] permissions = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            requestPermissions(permissions, REQUEST_PERMISSION_STORAGE);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_done).setVisible(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_STORAGE:
                if (grantResults.length > 0) {
                    boolean isGranted = true;
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            isGranted = false;
                            break;
                        }
                    }

                    // Permission granted if isGranted is true
                    if (isGranted) {
                        openGalleryAlbum();  // Album access is granted
                    } else {
                        Toast.makeText(getActivity(), R.string.permission_denied,
                            Toast.LENGTH_SHORT).show();
                        getParentActivity().finish();
                    }
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ||
            newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((GridLayoutManager) mAlbumList.getLayoutManager())
                .setSpanCount(getResources().getInteger(R.integer.album_column_count));
        }
    }

    /**
     * Perform storage provider query to get all available albums in the storage
     */
    private void openGalleryAlbum() {
        if (albums.isEmpty()) {
            albums.addAll(ImageUtils.getAllAlbums(getContext()));
            mAlbumList.getAdapter().notifyItemRangeInserted(0, albums.size());
        }
    }

    class AlbumHolder extends DraggableRecyclerViewHolder {
        @BindView(R.id.album_thumbnail)
        ImageView mThumbnail;
        @BindView(R.id.album_name)
        TextView mAlbumName;

        private AlbumHolder(ViewGroup parent) {
            super(R.layout.grid_album, parent);
            ButterKnife.bind(this, itemView);
        }
    }

    class AlbumAdapter extends DraggableRecyclerViewAdapter<AlbumEntry, AlbumHolder> {

        private AlbumAdapter(List<AlbumEntry> albums) {
            super(albums);
        }

        @Override
        public int getItemCount() {
            return albums.size();
        }

        @Override
        public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AlbumHolder(parent);
        }

        @Override
        public void onBindViewHolder(AlbumHolder holder, int position) {
            final AlbumEntry album = albums.get(position);
            String thumbnail = album.getThumbnail();
            // holder.mThumbnail.setImageBitmap(BitmapFactory.decodeFile(thumbnail));
            Glide.with(holder.getContext()).load(thumbnail).centerCrop().into(holder.mThumbnail);
            holder.mAlbumName.setText(String.format(Locale.getDefault(), "%s (%d)",
                album.getName(), album.getImages().size()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getParentActivity().swapFragment(R.id.fragment,
                        MultiImagePickerFragment.newInstance(album, limit));
                }
            });
        }
    }
}
