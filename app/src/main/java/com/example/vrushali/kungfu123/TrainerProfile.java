package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainerProfile extends BaseActivity {
  private String TAG = TrainerProfile.class.getSimpleName();

  private ProgressDialog pDialog;
  private ListView lv;
  String show_img;
  ImageView imageView;


  // URL to get contacts JSON
  private static String url = "http://10.0.43.1/kungfu2/api/v1/user.php?data=trainer_profile";

  ArrayList<HashMap<String, String>> contactList;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (InitApplication.getInstance().isNightModeEnabled()) {

      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

    } else {

      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }
    super.onCreate(savedInstanceState);


    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //inflate your activity layout here!
    @SuppressLint("InflateParams")
    View contentView = inflater.inflate(R.layout.activity_trainer_profile, null, false);
    drawer.addView(contentView, 0);
    contactList = new ArrayList<>();

    imageView = findViewById(R.id.profile_image);
    lv = (ListView) findViewById(R.id.trainer_profile);
    new GetContacts().execute();

  }

  private class GetContacts extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      // Showing progress dialog
      pDialog = new ProgressDialog(TrainerProfile.this);
      pDialog.setMessage("Please wait...");
      pDialog.setCancelable(false);
      pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
      Httpfortrainerprofile sh = new Httpfortrainerprofile(getApplicationContext());

      // Making a request to url and getting response
      String jsonStr = sh.makeServiceCall(url);

      Log.e(TAG, "Response from url: " + jsonStr);

      if (jsonStr != null) {
        try {
          JSONObject jsonObj = new JSONObject(jsonStr);

          // Getting JSON Array node
          JSONArray contacts = jsonObj.getJSONArray("0");

          // looping through All Contacts
          for (int i = 0; i < contacts.length(); i++) {
            JSONObject c = contacts.getJSONObject(i);

            String id = c.getString("u_id");
            String name = c.getString("u_name");
            String email = c.getString("u_mob");
            String address = c.getString("belt_name");
            String gender = c.getString("u_reg_date");
            String location = c.getString("u_status");
            show_img = c.getString("u_image");


            // tmp hash map for single contact
            HashMap<String, String> contact = new HashMap<>();

            // adding each child node to HashMap key => value
            contact.put("id", id);
            contact.put("name", name);
            contact.put("email", email);
            contact.put("address", address);
            contact.put("gender", gender);
            contact.put("location", location);
            contact.put("show_img",show_img);

            // adding contact to contact list
            contactList.add(contact);
          }
        } catch (final JSONException e) {
          Log.e(TAG, "Json parsing error: " + e.getMessage());
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Toast.makeText(getApplicationContext(),
                      "Json parsing error: " + e.getMessage(),
                      Toast.LENGTH_LONG)
                      .show();
            }
          });

        }
      } else {
        Log.e(TAG, "Couldn't get json from server.");
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(getApplicationContext(),
                    "Couldn't get json from server. Check LogCat for possible errors!",
                    Toast.LENGTH_LONG)
                    .show();
          }
        });

      }

      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      super.onPostExecute(result);
      // Dismiss the progress dialog
      if (pDialog.isShowing())
        pDialog.dismiss();

      ListAdapter adapter = new SimpleAdapter(
              TrainerProfile.this, contactList,
              R.layout.listforprofile, new String[]{"name", "email",
              "address"}, new int[]{R.id.name,
              R.id.email, R.id.address});

      lv.setAdapter(adapter);

      Glide.with(getApplicationContext()).load("http://10.0.43.1/kungfu2/images/"+show_img).into(imageView);
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

  @Override
  public void onBackPressed() {

    Intent intent = new Intent(TrainerProfile.this,MainActivity.class);
    startActivity(intent);

  }
}
