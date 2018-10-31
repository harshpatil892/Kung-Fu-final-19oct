package com.example.vrushali.kungfu123;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class Event_Register extends Fragment{

    private String TAG = Event_Register.class.getSimpleName();

    EditText fees;

    Spinner type_event,sub_event,spin_batch,spin_student,spin_belt;
    LinearLayout linearLayout;
    Button add_btn;
    String selected_event_type,res,res1,res2,res3,res4,item1,select_batch_id,res6;
    String event,batch,student,belt,get_fees;
    ArrayAdapter<String> myAdapter,myAdapter1,myAdapter2,myAdapter3;
    ArrayList<String> event_type ;
    ArrayList<String> event_name;
    ArrayList<String> batches;
    ArrayList<String> select_student;
    ArrayList<String> belt_level;

    String URL="http://10.0.43.1/kungfu2/api/v1/user.php?data=events_details";
    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=batches";
    String URL2="http://10.0.43.1/kungfu2/api/v1/user.php?data=belt_levels";
    String URL3 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=student_info_for_trainer_by_batch";

    public Event_Register() {
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

        View v =inflater.inflate(R.layout.fragment_event__register,container,false);

        hideNavigationBar();

        type_event = (Spinner) v.findViewById(R.id.spinner1);
        sub_event = (Spinner) v.findViewById(R.id.spinner2);
        spin_batch = (Spinner) v.findViewById(R.id.spinner3);
        spin_student = (Spinner) v.findViewById(R.id.spinner4);
        spin_belt = (Spinner) v.findViewById(R.id.spinner5);

        fees = v.findViewById(R.id.reg_fees);

        linearLayout = v.findViewById(R.id.belt_linear_layout);

        add_btn = v.findViewById(R.id.add_button);

        event_type = new ArrayList<>();
        event_name = new ArrayList<>();

        event_type.add(0,"select");
        event_type.add("Grading-Exam");
        event_type.add("Competition");
        event_type.add("Camp");

        myAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_type);
        type_event.setAdapter(myAdapter);

        batches = new ArrayList<>();
        batches.add(0,"select");
        new GetBatch().execute();

        select_student = new ArrayList<>();
        select_student.add(0,"select");

        belt_level = new ArrayList<>();
        belt_level.add(0,"select");
        loadSpinnerData(URL2);

        myAdapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, batches);
        spin_batch.setAdapter(myAdapter2);

        type_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).equals("select")){
                    // do nothing

                    myAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_name);
                    sub_event.setAdapter(myAdapter1);

                    myAdapter1.clear();

                    linearLayout.setVisibility(View.GONE);

                }

                else if(type_event.getSelectedItem().equals("Grading-Exam")){

                    linearLayout.setVisibility(View.VISIBLE);

                    selected_event_type =String.valueOf(type_event.getSelectedItem());
                    Log.e("Selected item:",selected_event_type);

                    new GetBeltExam(selected_event_type).execute();

                    myAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_name);
                    sub_event.setAdapter(myAdapter1);

                    myAdapter1.clear();
                }
                else if(type_event.getSelectedItem().equals("Competition")){

                    linearLayout.setVisibility(View.GONE);

                    selected_event_type =String.valueOf(type_event.getSelectedItem());
                    Log.e("Selected item:",selected_event_type);

                    new GetCompetition(selected_event_type).execute();

                    myAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_name);
                    sub_event.setAdapter(myAdapter1);

                    myAdapter1.clear();
                }

                else if(type_event.getSelectedItem().equals("Camp")){

                    linearLayout.setVisibility(View.GONE);

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


        spin_batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).equals("select")){
                    // do nothing

                }
                else{

                    item1 =parent.getItemAtPosition(position).toString();
                    select_batch_id =String.valueOf(spin_batch.getSelectedItemId());
                    Log.e("Selected item:",select_batch_id);

                    SharedPreferences sp1 = getActivity().getSharedPreferences("batchinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("batchid", batch);
                    editor.clear();
                    editor.commit();

                    new GetStudents(batch).execute();

                    myAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, select_student);
                    spin_student.setAdapter(myAdapter);

                    myAdapter.clear();
//
//                    String string = item1;
//                    String[] parts = string.split("-");
//                    batch = parts[0]; // 004
//                    String part2 = parts[1]; // 034556

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sub_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item =parent.getItemAtPosition(position).toString();

                String string = item;
                String[] parts = string.split("-");
                event = parts[0]; // 004
                String part2 = parts[1]; // 034556

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spin_student.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item =parent.getItemAtPosition(position).toString();

                String string = item;
                String[] parts = string.split("-");
                student = parts[0]; // 004
                String part2 = parts[1]; // 034556

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spin_belt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).equals("select")){
                    // do nothing

                }
                else{
                    String item = parent.getItemAtPosition(position).toString();
//                    Log.e("Selected belt",item);
                    Toast.makeText(parent.getContext(),"Selected:"+item,Toast.LENGTH_SHORT).show();

                    String string = item;
                    String[] parts = string.split("-");
                    belt = parts[0]; // 004
                    String part2 = parts[1]; // 034556

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_fees = fees.getText().toString();

                if(type_event.getSelectedItem().equals("Grading-Exam")){

                    new JsonPost1().execute(event,student,belt,get_fees);

                }else if(type_event.getSelectedItem().equals("Competition")){

                    new JsonPost2().execute(event,student,get_fees);

                }else if(type_event.getSelectedItem().equals("Camp")){

                    new JsonPost3().execute(event,student,get_fees);

                }


            }
        });



        return v;
    }

    private void loadSpinnerData(String url) {

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {
                try {

                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray c = jsonObj.getJSONArray("0");

                    for (int i = 0; i < c.length(); i++) {
                        JSONObject cn = c.getJSONObject(i);
                        String temp = cn.getString("id");
                        String temp1 = cn.getString("name");
                        String temp2 = cn.getString("level");

                        res4 = temp + "-" + temp1;
                        Log.e("Concated String:-",res4);

                        belt_level.add(res4);
                        Log.e("Belt Levels:", res4);
                    }

                    spin_belt.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, belt_level));

                }catch (JSONException e){e.printStackTrace();}
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
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

    class GetBatch extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandlerBatch sh3 = new HttpHandlerBatch(getContext());

            // Making a request to url and getting response
            String jsonStr = sh3.makeServiceCall(URL1);
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


                        batches.add(eg);

//                        res3 = a + "-" + eg;
//                        Log.e("Concated String:-",res3);
//
//                        batches.add(res3);
//                        Log.e("FINAL BATCHID:", res3);
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

            spin_batch.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, batches));
        }

    }

    class GetStudents extends AsyncTask<Void, Void, Void> {


        String item1;
        public GetStudents(String item) {

            this.item1 =item;

            Log.e("Selected batch id:",item1);

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
            HttpHandlerbatchId sh = new HttpHandlerbatchId(getContext());

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall1(URL3);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("0");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String a = c.getString("uc_id");
                        String b = c.getString("uc_name");
                        String cd = c.getString("fulladdr");
                        String e = c.getString("u_mob");

                        res6 = a + "-" + b;

                        Log.e("Concated:-",res6);

                        select_student.add(res6);
                        Log.e("FINAL students:", res6);


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

            myAdapter3 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, select_student);
            myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spin_student.setAdapter(myAdapter3);
            myAdapter3.notifyDataSetChanged();

        }
    }

    class JsonPost1 extends AsyncTask<String ,String,String> {


        @Override
        protected String doInBackground(String... params) {

            String response=PostJson1(params);
            Log.e("params",""+params);
            Log.e("response",""+response);
            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(getActivity(),"Event registered succesfully",Toast.LENGTH_SHORT).show();

        }
    }

    private String PostJson1(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";

        try {
            java.net.URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=belt_exam_regi");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"event_id\":\""+params[0]+"\",\"student_id\":\""+ params[1]+"\"," +
                    "\"belt_level\":\""+params[2] +"\",\"student_fee\":\""+ params[3]+"\"}";
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

    class JsonPost2 extends AsyncTask<String ,String,String> {


        @Override
        protected String doInBackground(String... params) {

            String response=PostJson2(params);
            Log.e("params",""+params);
            Log.e("response",""+response);
            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(getActivity(),"Event registered succesfully",Toast.LENGTH_SHORT).show();

        }
    }

    private String PostJson2(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";


        try {
            java.net.URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=belt_comp_regi");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"event_id\":\""+params[0]+"\",\"student_id\":\""+ params[1]+"\"," +
                    "\"student_fee\":\""+params[2] +"\"}";
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


    class JsonPost3 extends AsyncTask<String ,String,String> {


        @Override
        protected String doInBackground(String... params) {

            String response=PostJson3(params);
            Log.e("params",""+params);
            Log.e("response",""+response);
            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(getActivity(),"Event registered succesfully",Toast.LENGTH_SHORT).show();

        }
    }

    private String PostJson3(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";


        try {
            java.net.URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=train_camp_regi");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"event_id\":\""+params[0]+"\",\"student_id\":\""+ params[1]+"\"," +
                    "\"student_fee\":\""+params[2] +"\"}";
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




