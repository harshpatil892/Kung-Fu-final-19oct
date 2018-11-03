package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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

public class FeeRecorddetail extends BaseActivity {

    private String TAG = FeeRecorddetail.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    private static String url = "http://10.0.43.1/kungfu2/api/v1/user.php?data=student_last_2_fee_info";

    ArrayList<HashMap<String, String>> fee_detail_list;

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
        View contentView = inflater.inflate(R.layout.activity_fee_recorddetail, null, false);
        drawer.addView(contentView, 0);

        fee_detail_list = new ArrayList<>();

        lv = findViewById(R.id.listforfeedetail);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            getUserId(this);
            // Showing progress dialog
//            pDialog = new ProgressDialog(Fees1.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
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
            String jsonStr = sh.makeServiceCall1(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("student_fees");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String a = c.getString("uc_name");
                        String b = c.getString("t_name");
                        String cd = c.getString("sf_id");
                        String e = c.getString("sf_s_id");
                        String f = c.getString("sf_total");
                        String g = c.getString("sf_date");
                        String h = c.getString("sf_reciever");
                        String j = c.getString("sf_month");
                        String k = c.getString("sf_year");
                        String l = c.getString("sf_status");
//
                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("data");
//                        String mobile = phone.getString("b_day");
//                        String home = phone.getString("tc_region");
//                        String office = phone.getString("tc_location");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("a", a);
                        contact.put("b", b);
                        contact.put("cd",cd);
                        contact.put("e", e);
                        contact.put("f", f);
                        contact.put("g", g);
                        contact.put("h", h);
                        contact.put("j", j);
                        contact.put("k", k);
                        contact.put("l", l);

                        // adding contact to contact list
                        fee_detail_list.add(contact);
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
//            if (pDialog.isShowing())
//
//                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    FeeRecorddetail.this, fee_detail_list,
                    R.layout.listforfeerecorddetail, new String[]{"j","a","f","g","k","b"}, new int[]{R.id.mnthyr,R.id.name1,R.id.fee1,R.id.Feerecvdate1,
                    R.id.feepaidyr1,R.id.feereciver1});

            lv.setAdapter(adapter);
        }

    }
}
