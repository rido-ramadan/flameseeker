package com.edgardrake.flameseeker.lib.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.edgardrake.flameseeker.lib.http.HttpContext;

import okhttp3.OkHttpClient;

/**
 * Created by Edgar Drake on 26-Jan-17.
 */

public class BaseFragment extends Fragment implements HttpContext {

    private Handler mainHandler;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Handler getMainHandler() {
        return mainHandler;
    }

    @Override
    public OkHttpClient getHttpClient() {
        return getParentActivity().getHttpClient();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public BaseActivity getParentActivity() {
        return (BaseActivity) getActivity();
    }

    public final BaseFragment getFragment() {
        return this;
    }
}
