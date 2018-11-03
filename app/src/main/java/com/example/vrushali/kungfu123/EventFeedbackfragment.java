package com.example.vrushali.kungfu123;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class EventFeedbackfragment extends Fragment {

    Spinner type_event,sub_event;
    CardView event_card;

    SharedPreferences result1,result2,result3;

    String selected_event_type,res,res1,res2,event,temp,temp1,temp2;

    ArrayList<String> event_type ;
    ArrayList<String> event_name;

    ArrayAdapter<String> myAdapter,myAdapter1;

    private String TAG = Feedback.class.getSimpleName();
    public RatingBar rt1, rt2, rt3, rt4;
    public EditText ed1, ed2, ed3, ed4;
    public Button btn;


    String URL="http://10.0.43.1/kungfu2/api/v1/user.php?data=events_details";
    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=student_event_reg_details";


    private ProgressDialog pDialog;

    public EventFeedbackfragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_feedbackfragment, container, false);


        type_event = v.findViewById(R.id.spinner1);
        sub_event = v.findViewById(R.id.spinner2);

        event_card = v.findViewById(R.id.card_view1);

        rt1 = (RatingBar) v.findViewById(R.id.rating1);
        ed1 = (EditText) v.findViewById(R.id.note1);
        btn = (Button) v.findViewById(R.id.button);

        event_type = new ArrayList<String>();

        event_type.add(0,"select");
        event_type.add("Grading-Exam");
        event_type.add("Competition");
        event_type.add("Camp");

        event_name = new ArrayList<String>();
        event_name.add(0,"select");

        myAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, event_type);
        type_event.setAdapter(myAdapter);

        type_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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

                SharedPreferences sp1 = getActivity().getSharedPreferences("selectevent", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp1.edit();

                editor.putString("eventname", selected_event_type);
                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sub_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("select")){
                    // do nothing

                }
                else{

                    String item =parent.getItemAtPosition(position).toString();

                    String string = item;
                    String[] parts = string.split("-");
                    event = parts[0]; // 004
                    String part2 = parts[1]; // 034556

                    SharedPreferences sp1 = getActivity().getSharedPreferences("subevent", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("eventid", event);
                    editor.commit();

                    event_card.setVisibility(View.VISIBLE);

                }

//                new GetContacts1().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result1 = getActivity().getSharedPreferences("selectevent", Context.MODE_PRIVATE);
                temp = result1.getString("eventname", "");

                result2 = getActivity().getSharedPreferences("subevent", Context.MODE_PRIVATE);
                temp1 = result2.getString("eventid", "");

                result3 = getActivity().getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
                temp2 = result3.getString("userid", "");



                String rate1 = String.valueOf(Math.round(rt1.getRating()));

                String nt1 = ed1.getText().toString();

                new JsonPost().execute(temp,temp1,temp2,rate1,nt1);
            }
        });
        return v;
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

    private void hideNavigationBar() {
        getActivity().getWindow().getDecorView()
                .setSystemUiVisibility(

                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_IMMERSIVE
                );
    }

    class JsonPost extends AsyncTask<String ,String,String> {


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
            Toast.makeText(getActivity(),"Event Feedback submitted successfully",Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(SigninActivity.this,LoginActivity.class));

//            textView.setText(result);
        }
    }

////////////////////

    private String PostJson(String[] params)  {





        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";


        try {
            URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=event_feedback_submit");
            connection = (HttpURLConnection) url.openConnection();

            String urlparam = "{\"event_type\":\""+params[0]+"\",\"event_id\":\""+params[1]+"\"," +
                    "\"student_id\":\""+params[2] +"\",\"rating_5\":\""+params[3] +"\"," +
                    "\"note_5\":\""+params[4] +"\"}";

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


}
