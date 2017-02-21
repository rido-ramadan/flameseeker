package com.edgardrake.flameseeker.http;

import android.content.Context;
import android.os.Handler;

import okhttp3.OkHttpClient;

/**
 * Created by Edgar Drake on 26-Jan-17.
 */

public interface HttpRequester {
    Handler getMainHandler();
    OkHttpClient getHttpClient();
    Context getContext();
}
