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

public class Httpfordetails {

    private static final String TAG = Httpfordetails.class.getSimpleName();

    SharedPreferences result1,result2,result3,result4,result5;

    static String UserId;
    String temp,temp1,temp2,temp3,temp4;

    public Httpfordetails(Context context){

        result1 = context.getSharedPreferences("studentid", Context.MODE_PRIVATE);
        temp = result1.getString("gsid", "");

        result2 = context.getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        temp1 = result2.getString("userrole","");

        result3 = context.getSharedPreferences("month", Context.MODE_PRIVATE);
        temp2 = result3.getString("monthid","");

        result4 = context.getSharedPreferences("year", Context.MODE_PRIVATE);
        temp3 = result4.getString("yrid","");

        result5 = context.getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        temp4 = result5.getString("userid", "");

    }


    public String makeServiceCall(String reqUrl) {

        String response = null;
        try {
            URL url = new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=show_attendance");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.getRequestMethod();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("role", temp1);
            jsonParam.put("uid", temp);
            jsonParam.put("month", temp2);
            jsonParam.put("year", temp3);

            Log.e("role", temp1);
            Log.e("uid", temp);
            Log.e("month", temp2);
            Log.e("year", temp3);

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


    public String makeServiceCall1(String reqUrl) {

        String response = null;
        try {
            URL url = new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=show_attendance");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.getRequestMethod();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("role", temp1);
            jsonParam.put("uid", temp4);
            jsonParam.put("month", temp2);
            jsonParam.put("year", temp3);

            Log.e("role", temp1);
            Log.e("uid", temp4);
            Log.e("month", temp2);
            Log.e("year", temp3);

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
