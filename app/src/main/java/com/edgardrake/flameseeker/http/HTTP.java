package com.edgardrake.flameseeker.http;

import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.edgardrake.flameseeker.base.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
 * Created by Edgar Drake on 26-Jan-17.
 */

public class HTTP {

    private static final String TAG = "HTTP-Requestor";
    /**
     * CONFIG_SHOW_TOAST: set true for enable show toast on error, else false for silent failure
     */
    private static final boolean CONFIG_SHOW_TOAST = true;
    /**
     * CONFIG_MANAGED_PER_CONTEXT: set true to enable per activity/fragment HTTP queue requester,
     * or false to enable request from a single queue per application. Single queue per activity
     * means request can be cancelled halfway if the activity is closed (not switch to front).
     * Single queue per app means no request can be cancelled halfway.
     */
    private static final boolean CONFIG_MANAGED_PER_CONTEXT = true;

    public interface RequestCallback {
        void onSuccess(String response) throws IOException;
        void onFailure(IOException e);
    }

    private static OkHttpClient singletonHttpClient = null;

    protected static OkHttpClient getSingetonHttpClient() {
        if (singletonHttpClient == null) {
            singletonHttpClient = new OkHttpClient();
        }
        return singletonHttpClient;
    }

    /**
     * Basic, barebone method of calling OkHTTP request. Most of the request parameter must be
     * handcrafted by developer.
     * @param requester Fragment or activity which implement this which invokes this method.
     * @param request OkHTTP Request object.
     * @param callback Callback function to be called when request has finished. Must be implemented
     *                 by hand in respective activity. {@link BaseActivity.RequestCallback}
     * @see <a href="https://github.com/square/okhttp/wiki/Recipes">wiki/recipes</a>
     */
    private static void call(final HttpRequester requester, Request request,
                               final RequestCallback callback) {

        final OkHttpClient httpClient = CONFIG_MANAGED_PER_CONTEXT?
            requester.getHttpClient() : getSingetonHttpClient();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                requester.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                requester.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callback.onSuccess(response.body().string());
                        } catch (IOException e) {
                            if (CONFIG_SHOW_TOAST) {
                                Toast.makeText(requester.getContext(), e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            }
                            Log.e(TAG, "Error: " + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    /**
     * Wrapper method of GET request using
     * {@link HTTP#call(HttpRequester, Request, RequestCallback)}.
     * Modified to be more rookie developer friendly.
     * @param requester Fragment or activity which implement this which invokes this method.
     * @param url URL of the HTTP request. Must include protocol like HTTP/HTTPS
     * @param headers Header of the request, in form of map of string
     * @param callback Callback function to be called when request has finished. Must be implemented
     *                 by hand in respective activity. {@link BaseActivity.RequestCallback}
     */
    public static void GET(HttpRequester requester, String url, Map<String, String> headers,
                           RequestCallback callback) {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null)
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        Request GET = builder.build();
        call(requester, GET, callback);
    }

    /**
     * Wrapper method of POST request using
     * {@link HTTP#call(HttpRequester, Request, RequestCallback)}.
     * Modified to be more rookie developer friendly.
     * @param requester Fragment or activity which implement this which invokes this method.
     * @param url URL of the HTTP request. Must include protocol like HTTP/HTTPS
     * @param headers Header of the request, in form of map of string
     * @param form Form body of the POST request, in form of map of string
     * @param callback Callback function to be called when request has finished. Must be implemented
     *                 by hand in respective activity. {@link BaseActivity.RequestCallback}
     */
    public static void POST(HttpRequester requester, String url, Map<String, String> headers,
                            Map<String, String> form, RequestCallback callback) {
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
        call(requester, POST, callback);
    }

    /**
     * Wrapper method of multipart POST request using
     * {@link HTTP#call(HttpRequester, Request, RequestCallback)}. Modified to be more developer
     * friendly.
     * @param requester Fragment or activity which implement this which invokes this method.
     * @param url URL of the HTTP request. Must include protocol like HTTP/HTTPS
     * @param headers Header of the request, in form of map of string
     * @param form Form body of the POST request, in form of map of string
     * @param files Files to be uploaded via multipart POST request, in form of map of files
     * @param callback Callback function to be called when request has finished. Must be implemented
     *                 by hand in respective activity. {@link RequestCallback}
     */
    public static void POST(HttpRequester requester, String url, Map<String, String> headers,
                            Map<String, String> form, Map<String, File> files,
                            RequestCallback callback) {
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
        call(requester, POST, callback);
    }
}
