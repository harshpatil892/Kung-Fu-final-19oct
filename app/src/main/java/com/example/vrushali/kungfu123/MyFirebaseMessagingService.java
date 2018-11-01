package com.example.vrushali.kungfu123;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.generateViewId;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    SharedPreferences result1,role1;
    String temp,role;
    String name ;
    String idName;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        result1 = getSharedPreferences("noti", MODE_PRIVATE);
//        String restoredText = prefs.getString("text", "hii");
//        if (restoredText != null) {
//            temp = prefs.getString("name", "");//"No name defined" is the default value.
//            role = prefs.getString("idName", ""); //0 is the default value.
//        }

        temp = result1.getString("name", name);//"No name defined" is the default value.
        role = result1.getString("idName", idName); //0 is the default value.

        notificationBuilder.setContentTitle(temp);
        notificationBuilder.setContentText(role);
//        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.ic_beach);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long[] view = {500,1000};
        notificationBuilder.setVibrate(view);
//        notificationManager.notify(1, notificationBuilder.build());
    }
    }

