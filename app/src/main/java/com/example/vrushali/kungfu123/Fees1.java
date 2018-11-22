package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
//        if(pDialog != null)
//            pDialog.dismiss();
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
                    JSONArray contacts = jsonObj.getJSONArray("0");

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
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "No Fee Record Found",
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
                    Fees1.this, contactList,
                    R.layout.listforfeesrecordparent, new String[]{"j","f","g","k","b"},
                    new int[]{R.id.mnth,R.id.fee1,R.id.Feerecvdate1,R.id.feepaidyr1,R.id.feereciver1
                    });

            lv.setAdapter(adapter);

            if(lv.getCount()==0) {
                //empty, show alertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Fees1.this);
                builder.setMessage("No Record Found")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                Intent intent1 = new Intent(Fees1.this,MainActivity.class);
                                startActivity(intent1);
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.setOnShowListener( new DialogInterface.OnShowListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.darkblue);
                    }
                });

                alert.show();
            }
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
