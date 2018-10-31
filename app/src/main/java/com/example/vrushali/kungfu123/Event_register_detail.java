package com.example.vrushali.kungfu123;

import android.content.Context;
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
import android.widget.Button;
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


public class Event_register_detail extends Fragment implements SearchView.OnQueryTextListener{
    private String TAG = Event_register_detail.class.getSimpleName();
    Spinner type_event,sub_event;


    String selected_event_type,res,res1,res2;
    private SearchView mSearchView;
    ArrayList<String> event_type ;
    ArrayList<String> event_name;

    ArrayAdapter<String> myAdapter,myAdapter1;

    String URL="http://10.0.43.1/kungfu2/api/v1/user.php?data=events_details";

    ArrayList<HashMap<String, String>> contactList1;
    ListView lv;

    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=student_event_reg_details";
    public Event_register_detail() {
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
        View v =inflater.inflate(R.layout.fragment_event_register_detail,container,false);

        type_event = (Spinner) v.findViewById(R.id.spinner1);
        sub_event = (Spinner) v.findViewById(R.id.spinner2);
        mSearchView = (SearchView)v.findViewById(R.id.searchView1);
        event_type = new ArrayList<>();
        event_name = new ArrayList<>();

        event_type.add(0,"select");
        event_type.add("Grading-Exam");
        event_type.add("Competition");
        event_type.add("Camp");

        myAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_type);
        type_event.setAdapter(myAdapter);


        lv = (ListView) v.findViewById(R.id.eventrecord);
        hideNavigationBar();

        contactList1 = new ArrayList<>();

        new GetContacts1().execute();

        type_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String select_item = String.valueOf(type_event.getSelectedItem());

                SharedPreferences sp1 = getActivity().getSharedPreferences("selectevent", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp1.edit();

                editor.putString("eventname", select_item);
                editor.clear();
                editor.commit();



                if(parent.getItemAtPosition(position).equals("select")){
                    // do nothing

                    myAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_name);
                    sub_event.setAdapter(myAdapter1);

                    myAdapter1.clear();


                }

                else if(type_event.getSelectedItem().equals("Grading-Exam")){


                    selected_event_type =String.valueOf(type_event.getSelectedItem());
                    Log.e("Selected item:",selected_event_type);

                    new GetBeltExam(selected_event_type).execute();

                    myAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_name);
                    sub_event.setAdapter(myAdapter1);

                    myAdapter1.clear();
                }
                else if(type_event.getSelectedItem().equals("Competition")){


                    selected_event_type =String.valueOf(type_event.getSelectedItem());
                    Log.e("Selected item:",selected_event_type);

                    new GetCompetition(selected_event_type).execute();

                    myAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_name);
                    sub_event.setAdapter(myAdapter1);

                    myAdapter1.clear();
                }

                else if(type_event.getSelectedItem().equals("Camp")){


                    selected_event_type =String.valueOf(type_event.getSelectedItem());
                    Log.e("Selected item:",selected_event_type);

                    new GetCamp(selected_event_type).execute();

                    myAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_name);
                    sub_event.setAdapter(myAdapter1);

                    myAdapter1.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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




    class GetBeltExam extends AsyncTask<Void, Void, Void> {

        String item1;
        public GetBeltExam(String item) {

            this.item1 =item;

            Log.e("Selected belt exam:",item1);

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
        protected Void doInBackground(Void... arg1) {
            Httpforgradingevent sh = new Httpforgradingevent(getContext());

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String a = c.getString("be_id");
                        String b = c.getString("be_name");
                        String g = c.getString("be_date");
                        String d = c.getString("be_beltlevel");
                        String e = c.getString("be_address");
                        String f = c.getString("be_organizeby");
                        String h = c.getString("be_fee");
                        String j = c.getString("be_note");
                        String k = c.getString("be_status");
                        String l = c.getString("date");

                        res = a + "-" + b;

                        Log.e("Concated:-",res);

                        event_name.add(res);
                        Log.e("FINAL BATCHID:", res);
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
            } else {
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

            myAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_name);
            myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sub_event.setAdapter(myAdapter1);
            myAdapter1.notifyDataSetChanged();

        }

    }

    class GetCompetition extends AsyncTask<Void, Void, Void> {

        String item1;
        public GetCompetition(String item) {

            this.item1 =item;
            Log.e("Selected competition:",item1);

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
        protected Void doInBackground(Void... arg1) {
            Httpforcompetition sh = new Httpforcompetition(getContext());

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String a = c.getString("bc_id");
                        String b = c.getString("bc_name");
                        String g = c.getString("bc_date");
                        String d = c.getString("bc_belt_level");
                        String e = c.getString("bc_comp");
                        String f = c.getString("bc_category");
                        String h = c.getString("bc_group");
                        String j = c.getString("bc_address");
                        String k = c.getString("bc_organizeby");
                        String l = c.getString("bc_fee");
                        String m = c.getString("bc_note");
                        String n = c.getString("bc_status");

                        res1 = a + "-" + b;

                        Log.e("Concated:-",res1);

                        event_name.add(res1);
                        Log.e("FINAL BATCHID:", res1);
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
            } else {
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

            myAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_name);
            myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sub_event.setAdapter(myAdapter1);
            myAdapter1.notifyDataSetChanged();

        }

    }

    class GetCamp extends AsyncTask<Void, Void, Void> {

        String item1;
        public GetCamp(String item) {

            this.item1 =item;

            Log.e("Selected belt exam:",item1);

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
        protected Void doInBackground(Void... arg1) {
            Httpforcampevent sh = new Httpforcampevent(getContext());

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String a = c.getString("te_id");
                        String b = c.getString("te_name");
                        String g = c.getString("te_date");
                        String d = c.getString("te_beltlevel");
                        String e = c.getString("te_address");
                        String f = c.getString("te_organizeby");
                        String h = c.getString("te_fee");
                        String j = c.getString("te_note");
                        String k = c.getString("te_status");
                        String l = c.getString("date");

                        res2 = a + "-" + b;

                        Log.e("Concated:-",res2);

                        event_name.add(res2);
                        Log.e("FINAL BATCHID:", res2);
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
            } else {
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

            myAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_name);
            myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sub_event.setAdapter(myAdapter1);
            myAdapter1.notifyDataSetChanged();

        }

    }

    private class GetContacts1 extends AsyncTask<Void, Void, Void> {

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
            Httpforeventdetails sh = new Httpforeventdetails(getActivity());

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

                        String id = c.getString("uname");
                        String name = c.getString("fees");
                        String email = c.getString("result");

                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);


                        // adding contact to contact list
                        contactList1.add(contact);
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
                    R.layout.listforeventdetails, new String[]{"id", "name","email"
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
