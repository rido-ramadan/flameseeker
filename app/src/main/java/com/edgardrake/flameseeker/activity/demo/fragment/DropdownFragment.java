package com.edgardrake.flameseeker.activity.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.widget.dropdown.Dropdown;
import com.edgardrake.flameseeker.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DropdownFragment extends Fragment {

    @BindView(R.id.dropdown)
    Dropdown mDropdown;

    private List<User> users;

    public DropdownFragment() {}

    public static DropdownFragment newInstance() {
        DropdownFragment fragment = new DropdownFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User.Builder(String.valueOf(i))
                .setEmail(String.format(Locale.getDefault(), "email-%d@domain.tld", i))
                .setName("Name-" + i)
                .setUsername("Username-" + i)
                .build());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRoot = inflater.inflate(R.layout.fragment_dropdown, container, false);
        ButterKnife.bind(this, mRoot);
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDropdown.setAdapter(users, R.layout.simple_item_spinner_default,
            android.R.layout.simple_spinner_dropdown_item,
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Toast.makeText(getContext(), users.get(pos).getID(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
    }
}
