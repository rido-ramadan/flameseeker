package com.edgardrake.flameseeker.lib.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Edgar Drake on 24-Jan-17.
 */

public class LocalStorage {

    public static final String FCM_TOKEN = "fcm_token";
    public static final String USER_OBJECT = "user";

    public static SharedPreferences getInstance(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
