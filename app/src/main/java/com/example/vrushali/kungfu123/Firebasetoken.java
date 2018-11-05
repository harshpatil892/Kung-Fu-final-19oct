package com.example.vrushali.kungfu123;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Firebasetoken extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";
    @Override
    public void onTokenRefresh(){
        super.onTokenRefresh();
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,recent_token);
        registerToken(recent_token);
        SharedPreferences sp1=getApplicationContext().getSharedPreferences("reg_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp1.edit();
        editor.putString("token", recent_token);
        editor.commit();


    }

    private void registerToken(String recent_token) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("TOKEN",recent_token)
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.43.1/kungfu2/api/v1/user.php?data=indivisual_notification")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
