package com.edgardrake.flameseeker.activity.messaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.edgardrake.flameseeker.lib.base.BaseActivity;
import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.http.HTTP;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * An activity representing a single Message detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MessageListActivity}.
 */
public class MessageDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                HTTP.GET(getActivity(), "http://dev.prelo.id/api/app/version?app_type=android",
//                    null, new HTTP.RequestCallback() {
//                        @Override
//                        public void onSuccess(String response) {
//                            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onFailure(IOException e) {
//                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT)
//                                .show();
//                        }
//                    });

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=" + getString(R.string.server_key));

                String json = getString(R.string.json_notification_fcm_payload, FirebaseInstanceId.getInstance().getToken());
                Log.d("HTTP.POST.JSON", json);

                HTTP.POST(getActivity(), "https://fcm.googleapis.com/fcm/send", headers, json,
                    new HTTP.RequestCallback() {
                        @Override
                        public void onSuccess(String response) throws IOException {
                            Log.d("Push", response);
//                            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(IOException e) {

                        }
                    });
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(MessageDetailFragment.ARG_ITEM_ID,
                getIntent().getStringExtra(MessageDetailFragment.ARG_ITEM_ID));
            MessageDetailFragment fragment = new MessageDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                .add(R.id.message_detail_container, fragment)
                .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, MessageListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
