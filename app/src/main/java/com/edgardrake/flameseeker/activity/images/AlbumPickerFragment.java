package com.edgardrake.flameseeker.activity.images;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.activity.images.util.AlbumEntry;
import com.edgardrake.flameseeker.lib.base.BaseFragment;
import com.edgardrake.flameseeker.lib.utilities.ImageUtils;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewAdapter;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class AlbumPickerFragment extends BaseFragment {

    @BindView(R.id.album_list)
    RecyclerView mAlbumList;

    private List<AlbumEntry> albums;

    public static AlbumPickerFragment newInstance() {
        return new AlbumPickerFragment();
    }

    public AlbumPickerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        albums = new ArrayList<>();
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
            holder.mAlbumName.setText(album.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getParentActivity().swapFragment(R.id.fragment, MultiImagePickerFragment.newInstance(album));
                }
            });
        }
    }
}
