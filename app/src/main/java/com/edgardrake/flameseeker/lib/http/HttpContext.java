package com.edgardrake.flameseeker.lib.http;

import android.content.Context;
import android.os.Handler;

import okhttp3.OkHttpClient;

/**
 * Created by Edgar Drake on 26-Jan-17.
 */

public interface HttpContext {
    Handler getMainHandler();
    OkHttpClient getHttpClient();
    Context getContext();
}
