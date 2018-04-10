package com.edgardrake.flameseeker.activity.images;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.activity.images.util.AlbumEntry;
import com.edgardrake.flameseeker.activity.images.util.ImageEntry;
import com.edgardrake.flameseeker.lib.base.BaseFragment;
import com.edgardrake.flameseeker.lib.utilities.Logger;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewAdapter;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultiImagePickerFragment extends BaseFragment {

    public static final String SELECTED_IMAGE_PATHS = "selected";

    private static final String ALBUM_ENTRY = "album_entry";
    private static final String LIMIT = "max_count";

    @BindView(R.id.image_list)
    RecyclerView mImageList;

    private Integer count;
    private List<ImageEntry> images;
    private ArrayList<String> selectedPaths;

    public static MultiImagePickerFragment newInstance(AlbumEntry album, int limit) {
        MultiImagePickerFragment fragment = new MultiImagePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_ENTRY, album);
        args.putSerializable(LIMIT, limit);
        fragment.setArguments(args);
        return fragment;
    }

    public MultiImagePickerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        images = new ArrayList<>();
        images.addAll(((AlbumEntry) getArguments().getSerializable(ALBUM_ENTRY)).getImages());
        count = getArguments().getInt(LIMIT);
        selectedPaths = new ArrayList<>();
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_done).setVisible(count > 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                actionDoneChooseImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ||
            newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((GridLayoutManager) mImageList.getLayoutManager())
                .setSpanCount(getResources().getInteger(R.integer.image_column_count));
        }
    }

    /**
     * Finish choosing the image, send list of strings contains the accessible image path for upload
     */
    private void actionDoneChooseImage() {
        getParentActivity().setResult(Activity.RESULT_OK,
            new Intent().putStringArrayListExtra(SELECTED_IMAGE_PATHS, selectedPaths));
        getParentActivity().finish();
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
            mImageCheck.setVisibility(mImageCheck.getVisibility() == View.VISIBLE?
                View.GONE : View.VISIBLE);
        }

        private boolean isChecked() {
            return mImageCheck.getVisibility() == View.VISIBLE;
        }
    }

    private class ImageAdapter extends DraggableRecyclerViewAdapter<ImageEntry, ImageHolder> {

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
            final ImageEntry image = images.get(position);
            // holder.mThumbnail.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
            Glide.with(holder.getContext()).load(image.getPath()).dontAnimate()
                .centerCrop().into(holder.mThumbnail);
            holder.mImageCheck.setVisibility(selectedPaths.contains(image.getPath())?
                View.VISIBLE : View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (count != null) { // count != null, respect the limit count
                        if (count == 1) {  // special case, count 1 means choose an image means done
                            selectedPaths.add(image.getPath());
                            actionDoneChooseImage();
                        } else if (!selectedPaths.contains(image.getPath()) && !holder.isChecked()) {
                            if (selectedPaths.size() < count) {  // count isn't reached
                                selectedPaths.add(image.getPath());
                                holder.toggle();
                            } else {  // count already reached, nothing happens
                                Toast.makeText(getContext(), R.string.max_limit_reach,
                                    Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            selectedPaths.remove(image.getPath());
                            holder.toggle();
                        }
                    } else {  // count == null means no limit
                        if (!selectedPaths.contains(image.getPath()) && !holder.isChecked()) {
                            selectedPaths.add(image.getPath());
                        } else {
                            selectedPaths.remove(image.getPath());
                        }
                        holder.toggle();
                    }
                }
            });
        }
    }
}
