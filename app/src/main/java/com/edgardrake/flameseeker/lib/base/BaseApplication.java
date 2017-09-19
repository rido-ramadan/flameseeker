package com.edgardrake.flameseeker.lib.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by Edgar Drake on 14-Jul-17.
 */

public class BaseApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getContext() {
        return appContext;
    }
}
