package com.edgardrake.flameseeker.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.base.BaseActivity;
import com.edgardrake.flameseeker.lib.widget.viewcomponent.ActivityViewComponent;
import com.edgardrake.flameseeker.lib.widget.viewcomponent.ActivityViewComponent.ExpandableComponent;

import butterknife.BindView;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.profile_action_hide_header)
    Button mHideHeaderButton;

    private ProfileHeaderComponent header;
    private ProfileDetailComponent detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        header = new ProfileHeaderComponent(this)
            .setName("Regulus")
            .setGender("Male");
        header.enable(true);

        detail = new ProfileDetailComponent(this)
            .setName("Regulus")
            .setAge(26);
        detail.expand(true).enable(true);

        mHideHeaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header.enable(!header.isEnabled());
            }
        });
    }

    class ProfileHeaderComponent extends ActivityViewComponent<ProfileActivity> {

        @BindView(R.id.profile_image)
        ImageView mUserImage;
        @BindView(R.id.profile_name)
        TextView mUserName;
        @BindView(R.id.profile_gender)
        TextView mUserGender;

        ProfileHeaderComponent(ProfileActivity activity) {
            super(activity, R.id.profile_header_container);
        }

        ProfileHeaderComponent setImage(String url) {
            Glide.with(getActivity()).load(url).into(mUserImage);
            return this;
        }

        ProfileHeaderComponent setName(String name) {
            mUserName.setText(name);
            return this;
        }

        ProfileHeaderComponent setGender(String gender) {
            mUserGender.setText(gender);
            return this;
        }
    }

    class ProfileDetailComponent extends ExpandableComponent<ProfileActivity> {

        @BindView(R.id.profile_detail_name)
        TextView mName;
        @BindView(R.id.profile_detail_age)
        TextView mAge;

        ProfileDetailComponent(ProfileActivity activity) {
            super(activity, R.id.profile_detail_container, R.id.profile_detail_expander,
                R.id.profile_detail_expandable);
        }

        @Override
        public ExpandableComponent expand(boolean expand) {
            super.expand(expand);
            ((TextView) getExpanderView())
                .setCompoundDrawablesWithIntrinsicBounds(0, 0, isExpanded()?
                    R.drawable.ic_chevron_up_blue_32dp :
                    R.drawable.ic_chevron_down_blue_32dp, 0);
            return this;
        }

        ProfileDetailComponent setName(String name) {
            mName.setText(name);
            return this;
        }

        ProfileDetailComponent setAge(int age) {
            mAge.setText(String.valueOf(age));
            return this;
        }
    }
}
