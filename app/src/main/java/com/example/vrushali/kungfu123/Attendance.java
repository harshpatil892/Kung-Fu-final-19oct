package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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

public class Attendance extends BaseActivity implements SearchView.OnQueryTextListener{

    private String TAG = Attendance.class.getSimpleName();

    Spinner spin1;
    String trainid,batch,res;
    private SearchView mSearchView;
    String  item,harsh,temp;
    ArrayList<String> select_batch;
    String URL = "http://10.0.43.1/kungfu2/api/v1/user.php?data=batches";
    private static String url = "http://10.0.43.1/kungfu2/api/v1/user.php?data=student_info_for_trainer_by_batch";
    private ListView lv;
    ArrayList<HashMap<String, String>> contactList;

    SharedPreferences result1;

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
        View contentView = inflater.inflate(R.layout.activity_attendance, null, false);
        drawer.addView(contentView, 0);

        spin1 = findViewById(R.id.spinner1);
        mSearchView = (SearchView)findViewById(R.id.searchView1);
        select_batch = new ArrayList<String>();
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.takeatt);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {

                String value = lv.getItemAtPosition(position).toString();
                
                Log.e("ITEM SELECETD",value);
                harsh = value.replaceAll("[a-z,{}.A-Z=]","");
                Log.e("HARSHAL :",harsh);

                result1 = getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
                temp = result1.getString("userid", "");

                trainid = temp;
                new JsonPost().execute(trainid,harsh);

            }
        });

        new GetContacts().execute();


        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals("select")) {
                    // do nothing

                }

                else{

                    String item2 =adapterView.getItemAtPosition(i).toString();

                    String string = item2;
                    String[] parts = string.split("-");
                    batch = parts[0]; // 004
                    String part2 = parts[1]; // 034556

                    SharedPreferences sp1 = getSharedPreferences("attbatchid1", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("gatt1", batch);
                    editor.clear();
                    editor.commit();

                    new GetContacts1(batch).execute();

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lv.setTextFilterEnabled(true);
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

    class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandlerBatch sh = new HttpHandlerBatch(getApplicationContext());

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

                        res =  a + "-" + eg;

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


//            Toast.makeText(getApplicationContext(), "In Spinner", Toast.LENGTH_SHORT).show();

        }

    }

    private class GetContacts1 extends AsyncTask<Void, Void, Void> {

        String item1;
        public GetContacts1(String item) {

            this.item1 =item;

//            Log.e("Selected batch id:",item1);

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Httpfortakeatt sh = new Httpfortakeatt(getApplicationContext());

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

                        String id = c.getString("uc_id");
                        String name = c.getString("uc_name");


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

//            mShimmerViewContainer.setVisibility(View.GONE);
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    Attendance.this, contactList,
                    R.layout.listfortakeattendance, new String[]{"id","name"
            }, new int[]{R.id.id,R.id.name
            });

            lv.setAdapter(adapter);
        }
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
            Toast.makeText(Attendance.this,"Attendance Inserted successfully",Toast.LENGTH_SHORT).show();

        }
    }


    private String PostJson(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";


        try {
            java.net.URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=insert_attendance");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"trainer_id\":\""+trainid+"\",\"students\":\""+ harsh +"\"}";
            Log.e("data",urlparam);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream =new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(urlparam);

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
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Attendance.this,MainActivity.class);
        startActivity(intent);

    }
}
