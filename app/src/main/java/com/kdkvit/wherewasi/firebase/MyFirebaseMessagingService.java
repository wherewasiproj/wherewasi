package com.kdkvit.wherewasi.firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kdkvit.wherewasi.utils.SharedPreferencesUtils;

import models.User;
import utils.DatabaseHandler;

import static actions.actions.sendUserToBe;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    final String TAG = "MyFirebaseMessagingService";
    public final static String FCM_MESSAGE_RECEIVER = "message_received";

    // Notification messages will arrive at system tray if app is in background and will arrive at onMessageReceived if app is in foreground
    // Data Notification which have a payload will always arrive to this service onMessageReceived

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseMessaging.getInstance().subscribeToTopic("positives");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // ...
        Log.i("BLE", "RECEIVED POSITIVE");
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            // In case application is in foreground
            Intent intent = new Intent(FCM_MESSAGE_RECEIVER);
            String uuid = remoteMessage.getData().get("user_id");
            Long markTime = Long.parseLong(remoteMessage.getData().get("mark_time"));
            Long insTime = Long.parseLong(remoteMessage.getData().get("insertion_time"));
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            db.updateInteractionsToPositive(uuid, markTime, insTime);

            // If application is not in foreground then post notification

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "");
    }
}
