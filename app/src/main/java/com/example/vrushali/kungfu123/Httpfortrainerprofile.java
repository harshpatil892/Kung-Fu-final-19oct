package com.example.vrushali.kungfu123;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Httpfortrainerprofile {

    private static final String TAG = Httpfortrainerprofile.class.getSimpleName();


    SharedPreferences result1;

    String temp;

    public Httpfortrainerprofile(Context context) {

        result1 = context.getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        temp = result1.getString("ucid", "");
        Log.e("TEMPPPP:",temp);
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=trainer_profile");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("cid", temp);

            Log.e("CHILDID:-",temp);

            OutputStreamWriter out = new   OutputStreamWriter(conn.getOutputStream());
            out.write(jsonParam.toString());
            out.close();
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
