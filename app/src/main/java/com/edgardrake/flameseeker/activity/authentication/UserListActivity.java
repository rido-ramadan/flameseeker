package com.edgardrake.flameseeker.activity.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.activity.images.MultiImagePickerActivity;
import com.edgardrake.flameseeker.activity.images.MultiImagePickerFragment;
import com.edgardrake.flameseeker.lib.base.BaseActivity;
import com.edgardrake.flameseeker.lib.utilities.Logger;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewAdapter;
import com.edgardrake.flameseeker.lib.widget.recyclerview.DraggableRecyclerViewHolder;
import com.edgardrake.flameseeker.model.AuthUser;
import com.edgardrake.flameseeker.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends BaseActivity {

    @BindView(R.id.user_list)
    RecyclerView mUserList;
    @BindView(R.id.add_user)
    FloatingActionButton mAddUserButton;
    @BindView(R.id.edit_current_user)
    FloatingActionButton mEditCurrentUser;
    @BindView(R.id.open_gallery)
    FloatingActionButton mOpenGallery;

    private List<User> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        dataset = new ArrayList<>();
        new DragAdapter(dataset).attachToRecyclerView(mUserList);

        mUserList.addItemDecoration(new DividerItemDecoration(getActivity(),
            DividerItemDecoration.VERTICAL));

        mAddUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(dataset.size());
                User newUser = new User.Builder(id)
                    .setEmail(String.format(Locale.getDefault(), "email-%s@domain.tld", id))
                    .setUsername("username-" + id)
                    .setName("User " + id)
                    .build();
                addData(newUser);
            }
        });

        mEditCurrentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUser user = AuthUser.getInstance(getContext());
                String prevEmail = user.getEmail();
                if (user.getEmail().equals("regulus@drake.com")) {
                    user.edit().setEmail("rytlock@ascalon.com").commit();
                } else {
                    user.edit().setEmail("regulus@drake.com").commit();
                }
                String postEmail = user.getEmail();
                Toast.makeText(getActivity(), String.format("Prev: %s\nPost: %s", prevEmail, postEmail), Toast.LENGTH_SHORT).show();
            }
        });

        mOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImagePickerActivity.startThisActivityForResult(getActivity(), 10, 100);
            }
        });
    }

    public void setDataset(List<User> source) {
        dataset.clear();
        dataset.addAll(source);
        mUserList.getAdapter().notifyItemRangeInserted(0, source.size());
    }

    public void addData(User user) {
        int insertionPosition = dataset.size();
        dataset.add(user);
        mUserList.getAdapter().notifyItemInserted(insertionPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 100) {
            ArrayList<String> paths =
                data.getStringArrayListExtra(MultiImagePickerFragment.SELECTED_IMAGE_PATHS);
            Logger logger = Logger.log(getContext());
            for (String path : paths) {
                logger.addEntry("Path", path);
            }
            logger.show();
        }
    }

    class UserHolder extends DraggableRecyclerViewHolder {
        @BindView(R.id.user_profile_picture)
        ImageView mProfilePicture;
        @BindView(R.id.user_username)
        TextView mUsername;
        @BindView(R.id.user_email)
        TextView mUserEmail;
        @BindView(R.id.user_reorder_handle)
        ImageView mReorderHandle;

        UserHolder(ViewGroup parent) {
            super(R.layout.row_user, parent);
            ButterKnife.bind(this, itemView);
        }
    }

    class DragAdapter extends DraggableRecyclerViewAdapter<User, UserHolder> {

        DragAdapter(List<User> users) {
            super(users);
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UserHolder(parent);
        }

        @Override
        public void onBindViewHolder(final UserHolder holder, int position) {
            User user = dataset.get(position);
            holder.mUsername.setText(user.getUsername());
            holder.mUserEmail.setText(user.getEmail());
            setOnDragHandle(holder.mReorderHandle, holder);
        }
    }
}
