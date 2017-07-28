package com.edgardrake.flameseeker.activity.authentication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.base.BaseActivity;
import com.edgardrake.flameseeker.lib.widget.recyclerview.draggable.DraggableRecyclerViewAdapter;
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

    private List<User> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        dataset = new ArrayList<>();
//        mUserList.setAdapter(new UserListAdapter());
        DraggableRecyclerViewAdapter.attachToRecyclerView(new DragAdapter(dataset), mUserList);

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

    class UserHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_profile_picture)
        ImageView mProfilePicture;
        @BindView(R.id.user_username)
        TextView mUsername;
        @BindView(R.id.user_email)
        TextView mUserEmail;

        UserHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false));
            ButterKnife.bind(this, itemView);
        }

        UserHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class UserListAdapter extends RecyclerView.Adapter<UserHolder> {

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UserHolder(parent);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {
            User user = dataset.get(position);
            holder.mUsername.setText(user.getUsername());
            holder.mUserEmail.setText(user.getEmail());
        }
    }

    class DragAdapter extends DraggableRecyclerViewAdapter<User> {

        DragAdapter(List<User> users) {
            super(users);
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UserHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof UserHolder) {
                User user = dataset.get(position);
                UserHolder userHolder = (UserHolder) holder;
                userHolder.mUsername.setText(user.getUsername());
                userHolder.mUserEmail.setText(user.getEmail());
            }
        }
    }
}
