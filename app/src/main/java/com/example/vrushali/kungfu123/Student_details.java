package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Student_details extends BaseActivity implements SearchView.OnQueryTextListener {

    private String TAG = Student_details.class.getSimpleName();
    private ListView lv;
    private SearchView mSearchView;
    Spinner spin1;
    public CardView cardView;
    String batch,res,harsh,trainid,temp;
    String cd;
    ArrayList<String> select_batch;
    String URL = "http://10.0.43.1/kungfu2/api/v1/user.php?data=batches";
    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=student_info_for_trainer_by_batch";

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
        View contentView = inflater.inflate(R.layout.activity_student_details, null, false);
        drawer.addView(contentView, 0);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        spin1 = findViewById(R.id.spinner1);
        contactList = new ArrayList<>();
        mSearchView = (SearchView)findViewById(R.id.searchView1);
        lv = (ListView) findViewById(R.id.list3);
        cardView = (CardView)findViewById(R.id.cardView8);
        select_batch = new ArrayList<String>();
        select_batch.add(0,"Select");

        new GetContacts().execute();

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {

                String value = lv.getItemAtPosition(position).toString();

                Log.e("ITEM SELECETD",value);
                harsh = value.replaceAll("[a-z,{}.A-Z=]","");

                trainid = temp;
                String number = harsh.substring(harsh.length()-11,harsh.length());

                Log.e("HARSHAL :",number);
//
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+number));//change the number
                startActivity(callIntent);

                return true;

            }
        });


        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals("Select")){
                    // do nothing
                    lv.setAdapter(null);
                }
                else{

                    cardView.setVisibility(View.VISIBLE);

                    String item = adapterView.getItemAtPosition(i).toString();
//                    Log.e("Selected belt",item);
                    Toast.makeText(adapterView.getContext(),"Selected:"+item,Toast.LENGTH_SHORT).show();

                    String string = item;
                    String[] parts = string.split("-");
                    batch = parts[0]; // 004
                    String part2 = parts[1]; // 034556

                    SharedPreferences sp1 = getSharedPreferences("gbatchid", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("gbid", batch);
                    editor.commit();

                    new GetStudDetails(batch).execute();

                    ListAdapter adapter = new SimpleAdapter(
                            Student_details.this, contactList,
                            R.layout.listforstuddetails, new String[]{"a", "b",
                            "cd"}, new int[]{R.id.id,
                            R.id.name, R.id.email});

                    lv.setAdapter(adapter);

                    contactList.clear();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lv.setTextFilterEnabled(true);
//        lv.setFilterText("");

        setupSearchView();
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
    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
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
    
    class GetContacts extends AsyncTask<Void, Void, Void> {

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

            spin1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, select_batch));


        }

    }

    class GetStudDetails extends AsyncTask<Void, Void, Void> {

        String item11;
        public GetStudDetails(String item) {

            this.item11 =item;

            Log.e("Selected belt exam:",item11);

        }

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

            String jsonStr1 = sh.makeServiceCall1(URL1);
            Log.e(TAG, "Response from url: " + jsonStr1);
            if (jsonStr1 != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr1);
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("0");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String a = c.getString("uc_name");
                        String b = c.getString("fulladdr");
                        String cd = c.getString("u_mob");
                        String e = c.getString("uc_gender");
                        String f = c.getString("uc_dob");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("a", a);
                        contact.put("b", b);
                        contact.put("cd", cd);
                        contact.put("e", e);
                        contact.put("f", f);

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

             ListAdapter adapter = new SimpleAdapter(
                    Student_details.this, contactList,
                    R.layout.listforstuddetails, new String[]{"a", "b",
                    "cd","e","f"}, new int[]{R.id.id,
                    R.id.name, R.id.email,R.id.gender,R.id.dob});

             lv.setAdapter(adapter);

             ((SimpleAdapter) adapter).notifyDataSetChanged();


        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Student_details.this,MainActivity.class);
        startActivity(intent);

    }


}
