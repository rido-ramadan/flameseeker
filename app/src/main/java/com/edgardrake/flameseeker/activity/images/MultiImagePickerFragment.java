package com.edgardrake.flameseeker.activity.images;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.activity.images.util.AlbumEntry;
import com.edgardrake.flameseeker.activity.images.util.ImageEntry;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewAdapter;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultiImagePickerFragment extends Fragment {

    private static final String ALBUM_ENTRY = "album_entry";

    @BindView(R.id.image_list)
    RecyclerView mImageList;

    private List<ImageEntry> images;

    public static MultiImagePickerFragment newInstance(AlbumEntry album) {
        MultiImagePickerFragment fragment = new MultiImagePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_ENTRY, album);
        fragment.setArguments(args);
        return fragment;
    }

    public MultiImagePickerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<>();
        images.addAll(((AlbumEntry) getArguments().getSerializable(ALBUM_ENTRY)).getImages());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multi_image_picker, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageList.setHasFixedSize(true);
        new ImageAdapter(images).attachToRecyclerView(mImageList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    class ImageHolder extends DraggableRecyclerViewHolder {

        @BindView(R.id.image_thumbnail)
        ImageView mThumbnail;
        @BindView(R.id.image_check)
        ImageView mImageCheck;

        private ImageHolder(ViewGroup parent) {
            super(R.layout.grid_image, parent);
            ButterKnife.bind(this, itemView);
        }

        private void toggle() {
            mImageCheck.setVisibility(mImageCheck.getVisibility() == View.VISIBLE? View.GONE : View.VISIBLE);
        }
    }

    class ImageAdapter extends DraggableRecyclerViewAdapter<ImageEntry, ImageHolder> {

        private ImageAdapter(List<ImageEntry> images) {
            super(images);
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageHolder(parent);
        }

        @Override
        public void onBindViewHolder(final ImageHolder holder, int position) {
            ImageEntry image = images.get(position);
            // holder.mThumbnail.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
            Glide.with(holder.getContext()).load(image.getPath()).centerCrop().into(holder.mThumbnail);
            holder.mImageCheck.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.toggle();
                }
            });
        }
    }
}
