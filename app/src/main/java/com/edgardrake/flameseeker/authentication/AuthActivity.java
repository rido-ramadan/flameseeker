package com.edgardrake.flameseeker.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.edgardrake.flameseeker.lib.base.BaseActivity;
import com.edgardrake.flameseeker.R;

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
