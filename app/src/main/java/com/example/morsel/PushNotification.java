package com.example.morsel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotification extends FirebaseMessagingService {
    String t;
    public PushNotification() {
    }



    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("msgfrom", "From: " + remoteMessage.getFrom());
        Toast.makeText(this,"msg rcvd",Toast.LENGTH_SHORT).show();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("msgdata", "Message data payload: " + remoteMessage.getData());
            Toast.makeText(this,"msg data",Toast.LENGTH_SHORT).show();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("msgbody", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Toast.makeText(this,"msg body",Toast.LENGTH_SHORT).show();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void onNewToken(String token) {
        Log.d("rfr", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        t=token;

    }

}