package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Attendance_record extends BaseActivity implements SearchView.OnQueryTextListener{

    Spinner spin1;
    private SearchView mSearchView;
    private String TAG = Attendance_record.class.getSimpleName();
    ArrayAdapter<String> adapter;
    private RecyclerView recyclerView;
    ArrayList<String> select_batch;
    public CardView cardView;
    String part1,res;
    TextView header;

    private android.support.v7.widget.SearchView searchView;
    String URL = "http://10.0.43.1/kungfu2/api/v1/user.php?data=batches";
    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=show_attendance";
    private ListView lv;
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
        View contentView = inflater.inflate(R.layout.activity_attendance_record, null, false);
        drawer.addView(contentView, 0);
        contactList = new ArrayList<>();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        lv = (ListView) findViewById(R.id.attrec);
        cardView=(CardView)findViewById(R.id.cardView8);
        mSearchView = (SearchView)findViewById(R.id.searchView1);
        header = (TextView)findViewById(R.id.header);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {

                String value = lv.getItemAtPosition(position).toString();
                Log.e("ITEM SELECETD",value);

                String[] items = value.split(",");
                String stud_name = items[0].split("=")[1];
                String stud_id = items[1].replaceAll("[a-z,{}.A-Z=]","");

                Log.e("HARSHAL :",stud_id);

                SharedPreferences sp1 = getSharedPreferences("studentid", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp1.edit();

                editor.putString("gsid", stud_id);
                editor.clear();
                editor.commit();

                Intent intent = new Intent(Attendance_record.this,AttendaceRecordDetails.class);
                startActivity(intent);

            }
        });



        spin1 = findViewById(R.id.spinner1);

        select_batch = new ArrayList<String>();
        select_batch.add(0,"Select");


        new GetBatch().execute();


        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals("Select")) {

                    lv.setAdapter(null);
                    header.setVisibility(View.GONE);

                }

                else{

                    cardView.setVisibility(View.VISIBLE);
                    String item2 =adapterView.getItemAtPosition(i).toString();

                    String string = item2;
                    String[] parts = string.split("-");
                    part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556

                    SharedPreferences sp1 = getSharedPreferences("attbatchid", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("gatt", part1);
                    editor.clear();
                    editor.commit();

                    new GetContacts().execute();

                    ListAdapter adapter = new SimpleAdapter(
                            Attendance_record.this, contactList,
                            R.layout.listforattendancerecord, new String[]{"id", "name",
                    }, new int[]{R.id.id,
                            R.id.name});

                    lv.setAdapter(adapter);

                    contactList.clear();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        lv.setTextFilterEnabled(true);
        setupSearchView();

    }

    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {

        if (TextUtils.isEmpty(newText)) {
            lv.clearTextFilter();
        } else {
            lv.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
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

    class GetBatch extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress dialog
//                pDialog = new ProgressDialog(getActivity());
//                pDialog.setMessage("Please wait...");
//                pDialog.setCancelable(false);
//                pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandlerBatch sh = new HttpHandlerBatch(getApplicationContext());


            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("0");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String a = c.getString("b_id");
                        String b = c.getString("b_day");
                        String cd = c.getString("b_from_time");
                        String d = c.getString("b_to_time");
                        String eg = c.getString("b_name");
                        String f = c.getString("b_location");

                        res = a + "-" + eg;

                        select_batch.add(res);

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Invalid Operation " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Please Check Internet Connection!",
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

            spin1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, select_batch));

//            Toast.makeText(getApplicationContext(), "In Spinner", Toast.LENGTH_SHORT).show();

        }

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


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Httpforattendancerecord sh = new Httpforattendancerecord(getApplicationContext());

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL1);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("0");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

//                        String id = c.getString("u_id");
                        String name = c.getString("uc_name");
//                        String name1 = c.getString("uc_batch");
//                        String name2 = c.getString("a_id");
//                        String name3 = c.getString("a_s_id");
//                        String name4 = c.getString("a_t_id");
//                        String name5 = c.getString("a_date");
//                        String name6 = c.getString("a_status");
                        String id = c.getString("uc_id");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);

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
//            if (pDialog.isShowing())
//                pDialog.dismiss();

           header.setVisibility(View.VISIBLE);
           ListAdapter adapter = new SimpleAdapter(
                    Attendance_record.this, contactList,
                    R.layout.listforattendancerecord, new String[]{"id", "name",
            }, new int[]{R.id.id,
                    R.id.name});

            lv.setAdapter(adapter);

            ((SimpleAdapter) adapter).notifyDataSetChanged();

            if(lv.getCount()==0) {
                //empty, show alertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Attendance_record.this);
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


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Attendance_record.this,MainActivity.class);
        startActivity(intent);

    }

}
