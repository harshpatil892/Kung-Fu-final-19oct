package com.example.vrushali.kungfu123;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class Firebasetoken extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";
    @Override
    public void onTokenRefresh(){
        super.onTokenRefresh();
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,recent_token);

        SharedPreferences sp1=getApplicationContext().getSharedPreferences("reg_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp1.edit();
        editor.putString("token", recent_token);
        editor.commit();


    }

}
