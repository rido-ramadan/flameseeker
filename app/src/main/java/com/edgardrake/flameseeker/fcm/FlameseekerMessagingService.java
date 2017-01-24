package com.edgardrake.flameseeker.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.edgardrake.flameseeker.MessageListActivity;
import com.edgardrake.flameseeker.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FlameseekerMessagingService extends FirebaseMessagingService {

    private static final String TAG = "Flameseeker.FCM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "From: " + remoteMessage.getFrom());
        Log.i(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        final String title = remoteMessage.getNotification().getTitle();
        final String message = remoteMessage.getNotification().getBody();
        final String uri = remoteMessage.getData().get("uri");
        final Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.notification_image);

        sendNotification(title, message, image, uri);
    }

    private void sendNotification(String title, String message, Bitmap image, String uri) {
        int requestCode = 0;

        Intent intent = new Intent(this, MessageListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
            PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_flameseeker))
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent);

        if (image != null) {
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(image)
                .setBigContentTitle(title)
                .setSummaryText(message));
        }

        NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(requestCode, notificationBuilder.build());
    }
}
