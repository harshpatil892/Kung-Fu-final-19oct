package com.example.vrushali.kungfu123;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;

public class FeesRecord extends Fragment implements SearchView.OnQueryTextListener{

    private String TAG = FeesRecord.class.getSimpleName();
    ArrayList<HashMap<String, String>> contactList1;
    Spinner spin1;
    ListView lv;
    ArrayList<String> select_batch;
    private SearchView mSearchView;
    String part1,res1;

    String URL = "http://10.0.43.1/kungfu2/api/v1/user.php?data=batches";
    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=show_fees";

    String select_item_id,item1;
    public FeesRecord() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (InitApplication.getInstance().isNightModeEnabled()) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        super.onCreate(savedInstanceState);

}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_fees_record,container,false);
        hideNavigationBar();
        spin1 = (Spinner) v.findViewById(R.id.spinner1);
        lv = (ListView) v.findViewById(R.id.feerecord);
        mSearchView = (SearchView)v.findViewById(R.id.searchView1);

        select_batch = new ArrayList<String>();
        contactList1 = new ArrayList<>();
        select_batch.add(0,"select");
        new GetContacts().execute();

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).equals("select")){
                    // do nothing

                }
                else{
                    item1 =parent.getItemAtPosition(position).toString();

                    String string = item1;
                    String[] parts = string.split("-");
                    part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556

                    SharedPreferences sp1 = getActivity().getSharedPreferences("batchidd", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("gbatchid", part1);
                    editor.clear();
                    editor.commit();

                    new GetContacts1(part1).execute();

                    Toast.makeText(parent.getContext(),"Selected:"+item1,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s = lv.getItemAtPosition(position).toString();

                String[] items = s.split(",");
                String fees = items[0].split("=")[1]; //CURRENT
                String name = items[1].split("=")[1]; //NAME
                String idd = items[2].replaceAll("[a-z,{}.A-Z=]","");

                Log.e("fees",fees);
                Log.e("name",name);
                Log.e("id",idd);

                SharedPreferences sp1 = getActivity().getSharedPreferences("studentidd", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp1.edit();

                editor.putString("gstudid", idd);
                editor.clear();
                editor.commit();

                Intent intent = new Intent(getActivity(),FeeRecorddetail.class);
                startActivity(intent);
            }
        });


        lv.setTextFilterEnabled(true);
        setupSearchView();

        return v;
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

            // Showing progress dialog
//                pDialog = new ProgressDialog(getActivity());
//                pDialog.setMessage("Please wait...");
//                pDialog.setCancelable(false);
//                pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandlerBatch sh1 = new HttpHandlerBatch(getContext());

            String jsonStr = sh1.makeServiceCall(URL);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray contacts = jsonObj.getJSONArray("0");

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    String a = c.getString("b_id");
                    String b = c.getString("b_day");
                    String cd = c.getString("b_from_time");
                    String d = c.getString("b_to_time");
                    String eg = c.getString("b_name");
                    String f = c.getString("b_location");

                    res1 = a + "-" + eg;
                    select_batch.add(res1);

                }

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
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

            spin1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, select_batch));
        }

    }
    private class GetContacts1 extends AsyncTask<Void, Void, Void> {

        String item1;
        public GetContacts1(String item) {

            this.item1 =item;

            Log.e("Selected batch id:",item1);

        }

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
            Httpforfeesrecord sh = new Httpforfeesrecord(getActivity());

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

                        String sid = c.getString("uc_id");
                        String name = c.getString("uc_name");
                        String email = c.getString("sf_total");

                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", sid);
                        contact.put("name", name);
                        contact.put("email", email);

                        // adding contact to contact list
                        contactList1.add(contact);


                        Log.e("Contact list", String.valueOf(contactList1));
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
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

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), contactList1,
                    R.layout.listforfeerecord, new String[]{"id", "name","email"
            }, new int[]{R.id.id,
                    R.id.name,R.id.email});

            lv.setAdapter(adapter);
        }
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
