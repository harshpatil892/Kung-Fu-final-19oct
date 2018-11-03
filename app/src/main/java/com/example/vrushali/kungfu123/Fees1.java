package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Fees1 extends BaseActivity {

    private String TAG = Fees1.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;


    // URL to get contacts JSON
    private static String url = "http://10.0.43.1/kungfu2/api/v1/user.php?data=show_fees";
    //    Uri.Builder builder = new Uri.Builder()
//            .appendQueryParameter("role","trainer" )
//            .appendQueryParameter("uid", "3");
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
        View contentView = inflater.inflate(R.layout.activity_fees1, null, false);
        drawer.addView(contentView, 0);

        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    @Override
    public void onPause(){

        super.onPause();
        if(pDialog != null)
            pDialog.dismiss();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            getUserId(this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Fees1.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


//        @Override
//        public void onDestroy() {
//            super.onDestroy();
//            if (pDialog != null) {
//                pDialog.dismiss();
//                pDialog = null;
//            }
//        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler1 sh = new HttpHandler1(getApplicationContext());


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

                        String id = c.getString("uc_name");
                        String name = c.getString("sf_date");
                        String email = c.getString("t_name");
                        String address = c.getString("sf_s_id");
                        String gender = c.getString("sf_total");
                        String location = c.getString("sf_date");
                        String tcid = c.getString("sf_reciever");
                        String tcregion = c.getString("sf_month");
                        String tclocation = c.getString("sf_year");
                        String tcstatus = c.getString("sf_status");
//                        String uname = c.getString("uc_status");
//                        String ureg = c.getString("uc_reg_date");
//
                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("data");
//                        String mobile = phone.getString("b_day");
//                        String home = phone.getString("tc_region");
//                        String office = phone.getString("tc_location");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("address", address);
                        contact.put("gender", gender);
                        contact.put("location", location);
                        contact.put("tcid", tcid);
                        contact.put("tcregion", tcregion);
                        contact.put("tclocation", tclocation);
                        contact.put("tcstatus", tcstatus);
//                        contact.put("uname", uname);
//                        contact.put("ureg", ureg);

//                        contact.put("mobile", mobile);

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
                    Fees1.this, contactList,
                    R.layout.listforfeesrecordparent, new String[]{"id","name","email"}, new int[]{R.id.id,R.id.name,R.id.email
            });

//            ListAdapter adapter1 = new SimpleAdapter(
//                    MainActivity.this, contactList,
//                    R.layout.list_item1, new String[]{"name", "email",
//                    "mobile"}, new int[]{R.id.name,
//                    R.id.email, R.id.mobile});

            lv.setAdapter(adapter);
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

}
