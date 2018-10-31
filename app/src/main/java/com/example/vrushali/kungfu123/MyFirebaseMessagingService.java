package com.example.vrushali.kungfu123;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.support.constraint.Constraints.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String temp,role;
    SharedPreferences result1,role1;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        result1 = getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        role1 = getSharedPreferences("userinfos", Context.MODE_PRIVATE);
        temp = result1.getString("tit", "");
        role = role1.getString("add","");

        Log.e("TITITI:",role);
        Log.e("MESSSAGE:",temp);

//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//// Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//// Handle message within 10 seconds
//        }
//
//// Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }


        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(temp);
        notificationBuilder.setContentText(role);

//        notificationBuilder.setAutoCancel(true);


        notificationBuilder.setContentTitle(role);
        notificationBuilder.setContentText(temp);

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
