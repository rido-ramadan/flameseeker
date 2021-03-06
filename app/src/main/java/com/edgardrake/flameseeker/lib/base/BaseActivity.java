package com.edgardrake.flameseeker.lib.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.http.HttpContext;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Edgar Drake on 25-Jan-17.
 */

public abstract class BaseActivity extends AppCompatActivity implements HttpContext {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public interface RequestCallback {
        void onSuccess(String response) throws IOException;
        void onFailure(IOException e);
    }

    private OkHttpClient httpClient;
    private Handler mainHandler;

    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpClient = new OkHttpClient();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        httpClient.dispatcher().cancelAll();
        super.onDestroy();
    }

    @Override
    public Handler getMainHandler() {
        return mainHandler;
    }

    @Override
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initializeBasicComponents();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        initializeBasicComponents();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
        initializeBasicComponents();
    }

    private void initializeBasicComponents() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Swap fragment at the specified container to a new fragment. Useful for fragment to execute
     * activity fragment transaction without knowing the parent's activity specification.
     * @param containerResID ViewGroup which fragment wished to be replaced
     * @param fragment New fragment to replace the contains at the specified ViewGroup
     */
    public void swapFragment(@IdRes int containerResID, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(containerResID, fragment)
            .addToBackStack(null)
            .commit();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home &&
            getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Check whether this app package has the specified permission(s)
     * @param permissions Array of string permission to be checked
     * @return True if <b>all</b> permission is granted, otherwise false
     */
    public boolean hasPermissions(@NonNull String... permissions) {
        boolean hasPermission = true;
        for (String permission : permissions) {
            hasPermission = hasPermission && ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED;
        }
        return hasPermission;
    }

    /**
     * Perform request for specified permission(s)
     * @param requestCode Unique code for request. Specific code usually tied to specific case
     * @param permissions Strings of permission(s) to be asked for grant
     */
    public void requestPermissions(int requestCode, @NonNull String... permissions) {
        ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
    }

    /**
     * Basic, barebone method of calling OkHTTP request. Most of the request parameter must be
     * handcrafted by developer.
     * @param request OkHTTP Request object.
     * @param callback Callback function to be called when request has finished. Must be implemented
     *                 by hand in respective activity. {@link RequestCallback}
     * @see <a href="https://github.com/square/okhttp/wiki/Recipes">wiki/recipes</a>
     */
    protected void call(Request request, final RequestCallback callback) {
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callback.onSuccess(response.body().string());
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                        }
                    }
                });
            }
        });
    }

    /**
     * Wrapper method of GET request using {@link BaseActivity#call(Request, RequestCallback)}.
     * Modified to be more rookie developer friendly.
     * @param url URL of the HTTP request. Must include protocol like HTTP/HTTPS
     * @param headers Header of the request, in form of map of string
     * @param callback Callback function to be called when request has finished. Must be implemented
     *                 by hand in respective activity. {@link RequestCallback}
     */
    public void GET(String url, Map<String, String> headers, RequestCallback callback) {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null)
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        Request GET = builder.build();
        call(GET, callback);
    }

    /**
     * Wrapper method of POST request using {@link BaseActivity#call(Request, RequestCallback)}.
     * Modified to be more rookie developer friendly.
     * @param url URL of the HTTP request. Must include protocol like HTTP/HTTPS
     * @param headers Header of the request, in form of map of string
     * @param form Form body of the POST request, in form of map of string
     * @param callback Callback function to be called when request has finished. Must be implemented
     *                 by hand in respective activity. {@link RequestCallback}
     */
    public void POST(String url, Map<String, String> headers, Map<String, String> form,
                     RequestCallback callback) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (form != null)
            for (String key : form.keySet()) {
                formBody.add(key, form.get(key));
            }

        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null)
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }

        Request POST = builder.post(formBody.build()).build();
        call(POST, callback);
    }

    /**
     * Wrapper method of multipart POST request using
     * {@link BaseActivity#call(Request, RequestCallback)}. Modified to be more rookie developer
     * friendly.
     * @param url URL of the HTTP request. Must include protocol like HTTP/HTTPS
     * @param headers Header of the request, in form of map of string
     * @param form Form body of the POST request, in form of map of string
     * @param callback Callback function to be called when request has finished. Must be implemented
     *                 by hand in respective activity. {@link RequestCallback}
     */
    public void POST(String url, Map<String, String> headers, Map<String, String> form,
                     Map<String, File> files, RequestCallback callback) {
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        if (form != null)
            for (String key : form.keySet()) {
                multipartBody.addFormDataPart(key, form.get(key));
            }
        if (files != null)
            for (String key : files.keySet()) {
                File file = files.get(key);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    MimeTypeMap.getFileExtensionFromUrl(file.getName()));

                multipartBody.addFormDataPart(key, file.getName(),
                    RequestBody.create(MediaType.parse(mimeType), file));
            }

        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null)
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }

        Request POST = builder.post(multipartBody.build()).build();
        call(POST, callback);
    }

    /**
     * Wrapper method of JSON POST request using
     * {@link BaseActivity#call(Request, RequestCallback)}. Modified to be more developer
     * friendly.
     * @param url URL of the HTTP request. Must include protocol like HTTP/HTTPS
     * @param headers Header of the request, in form of map of string
     * @param json JSON file to be sent via POST request
     * @param callback Callback function to be called when request has finished. Must be implemented
     *                 by hand in respective activity. {@link RequestCallback}
     */
    public void POST(String url, Map<String, String> headers, String json,
                     RequestCallback callback) {
        RequestBody body =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null)
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }

        Request POST = builder.post(body).build();
        call(POST, callback);
    }
}
