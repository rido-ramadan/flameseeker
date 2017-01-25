package com.edgardrake.flameseeker.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.edgardrake.flameseeker.BaseActivity;
import com.edgardrake.flameseeker.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class AuthActivity extends BaseActivity
    implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

    @Override
    public void onClick(View v) {

    }

    public static void startThisActivity(Context context) {
        context.startActivity(new Intent(context, AuthActivity.class));
    }
}
