package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import com.facebook.shimmer.ShimmerFrameLayout;

public class AttendaceRecordDetails extends BaseActivity {

    Spinner get_month,get_year;

    String monthh,yr;

    LinearLayout attend;


    ArrayList<String> mnth ;
    ArrayList<String> year;

    private String TAG = AttendaceRecordDetails.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://10.0.43.1/kungfu2/api/v1/user.php?data=show_attendance";

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
        View contentView = inflater.inflate(R.layout.activity_attendace_record_details, null, false);
        drawer.addView(contentView, 0);
        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.listforattrecdetails);

        attend = findViewById(R.id.yr_id_attend);


        get_month = findViewById(R.id.spinner1);
        get_year = findViewById(R.id.spinner2);

        mnth = new ArrayList<>();
        year = new ArrayList<>();

        mnth.add(0,"select");
        mnth.add("January");
        mnth.add("February");
        mnth.add("March");
        mnth.add("April");
        mnth.add("May");
        mnth.add("June");
        mnth.add("July");
        mnth.add("August");
        mnth.add("September");
        mnth.add("October");
        mnth.add("November");
        mnth.add("December");

        year.add(0,"select");
        year.add("2017");
        year.add("2018");
        year.add("2019");



        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, mnth);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        get_month.setAdapter(adapter);

        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, year);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        get_year.setAdapter(adapter1);


        get_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).equals("select")) {
                    // do nothing
                    lv.setAdapter(null);
                    attend.setVisibility(View.GONE);

                }

                else{
                    monthh= String.valueOf(get_month.getSelectedItemId());

                    SharedPreferences sp1 = getSharedPreferences("month", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("monthid", monthh);
                    editor.clear();
                    editor.commit();

                    Log.e("Selected month id",monthh);

                    attend.setVisibility(View.VISIBLE);

                    ListAdapter adapter2 = new SimpleAdapter(
                            AttendaceRecordDetails.this, contactList,
                            R.layout.listforattendancerecdetails, new String[]{"id"
                    }, new int[]{R.id.id
                    });

                    lv.setAdapter(adapter2);

                    contactList.clear();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        get_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).equals("select")) {
                    // do nothing
                    lv.setAdapter(null);
                }

                else{
                    yr = String.valueOf(get_year.getSelectedItem());

                    SharedPreferences sp1 = getSharedPreferences("year", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("yrid", yr);
                    editor.clear();
                    editor.commit();

                    Log.e("Selected year",yr);

                    new GetContacts().execute();

                    ListAdapter adapter2 = new SimpleAdapter(
                            AttendaceRecordDetails.this, contactList,
                            R.layout.listforattendancerecdetails, new String[]{"id"
                    }, new int[]{R.id.id
                    });

                    lv.setAdapter(adapter2);

                    contactList.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
//            pDialog = new ProgressDialog(TrainingCenters.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();

//            mShimmerViewContainer.startShimmerAnimation();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Httpfordetails sh = new Httpfordetails(getApplicationContext());


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


                        String id = c.getString("a_date");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
//
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
//            if (pDialog.isShowing())
//                pDialog.dismiss();


            ListAdapter adapter2 = new SimpleAdapter(
                    AttendaceRecordDetails.this, contactList,
                    R.layout.listforattendancerecdetails, new String[]{"id"
            }, new int[]{R.id.id
            });

            lv.setAdapter(adapter2);

            ((SimpleAdapter) adapter2).notifyDataSetChanged();

            if(lv.getCount()==0) {
                //empty, show alertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AttendaceRecordDetails.this);
                builder.setMessage("No Record Found")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
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

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AttendaceRecordDetails.this,Attendance_record.class);
        startActivity(intent);

    }

}
