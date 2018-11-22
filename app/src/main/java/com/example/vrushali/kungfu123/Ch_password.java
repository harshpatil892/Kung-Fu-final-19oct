package com.example.vrushali.kungfu123;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Ch_password extends Fragment {

    public EditText pass,npass;
    public Button save;
    String savepassword,uid,npassword;
    String userid="2";
    SharedPreferences result1;
    String temp;
    private static String url = "http://10.0.43.1/kungfu2/api/v1/user.php?data=change_password";
    ArrayList<HashMap<String, String>> contactList;

    public Ch_password() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (InitApplication.getInstance().isNightModeEnabled()) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        View v =inflater.inflate(R.layout.fragment_ch_password,container,false);
        npass=(EditText)v.findViewById(R.id.pwd);
        pass=(EditText)v.findViewById(R.id.con_pwd);
        save=(Button)v.findViewById(R.id.add_button);
        result1 = getActivity().getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        temp = result1.getString("userid", "");
        contactList = new ArrayList<>();
        save.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View v) {

        npassword=npass.getText().toString();
        savepassword=pass.getText().toString();
        uid=temp;
        if(savepassword.equals( npassword))
        {

         new JsonPost().execute(uid,savepassword);

        }
        else {
            Toast.makeText(getActivity(), "Password does not match", Toast.LENGTH_LONG).show();
        }

    }
});

        return v;
    }

class JsonPost extends AsyncTask<String ,String,String>{

        @Override
        protected String doInBackground(String... params) {

            String response=PostJson(params);
            Log.e("params",""+params);
            Log.e("response",""+response);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getActivity(),"Password Changed successfully",Toast.LENGTH_SHORT).show();

        }
    }

 private String PostJson(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";


        try {
            URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=change_password");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"uid\":\""+temp+"\",\"password\":\""+ params[1]+"\"}";
            Log.e("data",urlparam);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream =new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(urlparam);
            Log.e("USERID",uid);
            Log.e("USERIDdddd",userid);
            dataOutputStream.flush();
            dataOutputStream.close();

            int responsecode=connection.getResponseCode();
            int data1 = Log.e("data", "data:" + responsecode);

            br =new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line="";

            StringBuffer buffer=new StringBuffer();

            while((line =br.readLine())!=null){
                buffer.append(line);
                Log.e("line",""+line);

            }
            data=buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } if(connection !=null) {
            connection.disconnect();
            try {
                if (br != null) {
                    br.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        return data;

    }
    private void hideNavigationBar() {
        getActivity().getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }
}
