package com.edgardrake.flameseeker.fcm;

import android.util.Log;

import com.edgardrake.flameseeker.lib.data.LocalStorage;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Edgar Drake on 23-Jan-17.
 */

public class FlameseekerInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "Flameseeker.FCM";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        LocalStorage.getInstance(this).edit().putString(LocalStorage.FCM_TOKEN, token).apply();
        Log.i(TAG, "Stored FCM token: " + token);
    }
}
