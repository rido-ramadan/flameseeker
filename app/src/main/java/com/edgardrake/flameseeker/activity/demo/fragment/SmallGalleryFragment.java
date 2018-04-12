package com.edgardrake.flameseeker.activity.demo.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.activity.images.MultiImagePickerActivity;
import com.edgardrake.flameseeker.activity.images.MultiImagePickerFragment;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewAdapter;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmallGalleryFragment extends Fragment {

    private static final int OPEN_GALLERY = 0x1;

    @BindView(R.id.image_list)
    RecyclerView mImageList;
    @BindView(R.id.open_gallery)
    FloatingActionButton mOpenGalleryButton;

    private List<File> images;

    public SmallGalleryFragment() {}

    public static SmallGalleryFragment newInstance() {
        SmallGalleryFragment fragment = new SmallGalleryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRoot = inflater.inflate(R.layout.fragment_small_gallery, container, false);
        ButterKnife.bind(this, mRoot);
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageAdapter adapter = new ImageAdapter();
        adapter.attachToRecyclerView(mImageList);

        mOpenGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImagePickerActivity
                    .startThisActivityForResult(getActivity(), 100, OPEN_GALLERY);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OPEN_GALLERY:
                    ArrayList<String> imagePaths = data.getStringArrayListExtra(
                        MultiImagePickerFragment.SELECTED_IMAGE_PATHS);

                    int startInsertionPoint = images.size();
                    int length = imagePaths.size();

                    for (String filePath : imagePaths) {
                        images.add(new File(filePath));
                    }
                    mImageList.getAdapter().notifyItemRangeInserted(startInsertionPoint, length);
                    break;
            }
        }
    }

    class ImageAdapter extends DraggableRecyclerViewAdapter<File, ImageHolder> {

        public ImageAdapter() {
            super(images);
        }

        @Override
        public int getItemCount() {
            return images != null? images.size() : 0;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageHolder(parent);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            holder.setFile(images.get(holder.getAdapterPosition()));
        }

        @Override
        public void onViewRecycled(ImageHolder holder) {
            super.onViewRecycled(holder);
        }
    }

    class ImageHolder extends DraggableRecyclerViewHolder {

        @BindView(R.id.image)
        ImageView mImage;
        @BindView(R.id.image_uri)
        TextView mUri;
        @BindView(R.id.image_file_size)
        TextView mFileSize;

        private File imageFile;

        public ImageHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_image, parent, false));
            ButterKnife.bind(this, itemView);
        }

        public ImageHolder setImage(Object url) {
            Glide.with(itemView.getContext()).load(url).into(mImage);
            return this;
        }

        public ImageHolder setUri(String uri) {
            mUri.setText(uri);
            return this;
        }

        public ImageHolder setSize(long fileSize) {
            mFileSize.setText(String.format(Locale.getDefault(), "%d kb", fileSize));
            return this;
        }

        public ImageHolder setFile(File image) {
            imageFile = image;
            this.setImage(imageFile)
                .setUri(imageFile.getAbsolutePath())
                .setSize(imageFile.length() / 1024);
            return this;
        }
    }

}
