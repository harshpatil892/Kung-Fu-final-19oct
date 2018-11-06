package com.example.vrushali.kungfu123;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

  EditText mob,pwd;
  SharedPreferences sp;
  TextView log_in;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);

      mob = findViewById(R.id.umob);
      pwd = findViewById(R.id.upwd);

      log_in = findViewById(R.id.loginbtn);

      log_in.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              String un = mob.getText().toString();
              String pwd1= pwd.getText().toString();

              new JsonPost().execute(un,pwd1);
              checkConnection();




          }
      });

  }
    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }
    }

    public void checkConnection(){

        if(isOnline()){


        }else{

            Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

        }

    }
    private void hideNavigationBar() {
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }
      class JsonPost extends AsyncTask<String ,String,String> {

          @Override
          protected String doInBackground(String... params) {

              String response = PostJson(params);

              return response;
          }


          @Override
          protected void onPostExecute(String result) {
              super.onPostExecute(result);

              String un = mob.getText().toString();
              String pwd1= pwd.getText().toString();

              try {

                  JSONObject jsonObject = new JSONObject(result);
                  String success = jsonObject.getString("st");

                  if(success.equals("1")){

                      JSONObject jsonObj = new JSONObject(result);
                      JSONArray c = jsonObj.getJSONArray("0");

                      for (int i = 0; i < c.length(); i++) {
                          JSONObject cn = c.getJSONObject(i);
                          String temp = cn.getString("u_id");
//                          String temp1 = cn.getString("u_name");
//                          String temp2 = cn.getString("u_dob");
//                          String temp3 = cn.getString("u_gender");
//                          String temp4 = cn.getString("u_mob");
//                          String temp5 = cn.getString("u_pwd");
//                          String temp6 = cn.getString("u_address");
//                          String temp7 = cn.getString("u_belt_level");
//                          String temp8 = cn.getString("u_weight");
//                          String temp9 = cn.getString("u_image");
                          String temp10 = cn.getString("u_role");
//                          String temp11 = cn.getString("u_status");
//                          String temp12 = cn.getString("u_reg_date");
//                            String temp13=cn.getString("uc_id");

                          SharedPreferences sp1=getApplicationContext().getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
                          SharedPreferences.Editor editor = sp1.edit();
                          editor.putString("userrole", temp10);
                          editor.putString("userid",temp);
//                          editor.putString("ucid",temp13);
                          editor.commit();

                          Toast.makeText(getApplicationContext(),temp10 , Toast.LENGTH_SHORT).show();
                      }

                      sp=getSharedPreferences("login",Context.MODE_PRIVATE);
                      SharedPreferences.Editor e=sp.edit();
                      e.putString("mobile",un);
                      e.putString("password",pwd1);
                      e.commit();

                      Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                      startActivity(intent);

                      finish();

                  }

              } catch (JSONException e) {
                  e.printStackTrace();

                  Toast.makeText(LoginActivity.this,e.toString(), Toast.LENGTH_SHORT).show();

              }

          }
      }
      private String PostJson(String[] params) {

          HttpURLConnection connection = null;
          BufferedReader br=null;
          String data="";

          try {
              URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=login");
              connection = (HttpURLConnection) url.openConnection();
              String urlparam = "{\"mobile\":\""+ params[0]+"\",\"password\":\""+params[1] +"\"}";
              Log.e("data",urlparam);
              connection.setRequestMethod("POST");
              connection.setRequestProperty("Content-Type","application/json");
              connection.setDoOutput(true);
              DataOutputStream dataOutputStream =new DataOutputStream(connection.getOutputStream());
              dataOutputStream.writeBytes(urlparam);

              dataOutputStream.flush();
              dataOutputStream.close();

              int responsecode=connection.getResponseCode();
              Log.e("data","data:"+ responsecode);

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






}
