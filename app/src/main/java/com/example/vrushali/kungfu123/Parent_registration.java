package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.HashMap;

public class Parent_registration extends Fragment {

    private String TAG = Parent_registration.class.getSimpleName();

    DatePickerDialog picker;
    Button btnGet, btnGet1 , add_button;
    TextView tvw, register_text;

    String child_count ="1";

    EditText namet,mob_no,pwd,con_pwd,addr,cname,add_fees,gr_no;

    String nm,gender,mob,pd,cpd,address,cnm,bd,af,gno,child_gender,reg_d;
    String buttonSelected,buttonSelected1,res4,part1,res3,part3;

    RadioGroup radiogroup,child_radiogroup;

    Spinner spin1, spin2;

    ArrayList<String> belt_level_names;
    ArrayList<String> select_batch;

    String URL="http://10.0.43.1/kungfu2/api/v1/user.php?data=belt_levels";
    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=batches";

    public Parent_registration() {

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

        View v = inflater.inflate(R.layout.fragment_parent_registration, container, false);

        namet = v.findViewById(R.id.name);
        mob_no = v.findViewById(R.id.mobno);
        pwd = v.findViewById(R.id.pwd);
        con_pwd = v.findViewById(R.id.conpwd);
        addr = v.findViewById(R.id.edittextarea);


        cname = v.findViewById(R.id.fname);
        add_fees = v.findViewById(R.id.ad_fees);
        gr_no = v.findViewById(R.id.gr_no);

        btnGet = (Button) v.findViewById(R.id.select_bdate);
        tvw = (TextView) v.findViewById(R.id.birthdate);

        btnGet1 = (Button) v.findViewById(R.id.select_reg_date);
        register_text = (TextView) v.findViewById(R.id.registerdate);

        radiogroup = v.findViewById(R.id.radioGroupParent);
        child_radiogroup = v.findViewById(R.id.radioGroup1);

        spin1 = (Spinner) v.findViewById(R.id.beltspinner);

        spin2 = (Spinner) v.findViewById(R.id.batchspinner);

        add_button = v.findViewById(R.id.save_btn);

        belt_level_names=new ArrayList<String>();
        loadSpinnerData(URL);



        belt_level_names.add(0,"select");

        select_batch = new ArrayList<String>();
        select_batch.add(0,"select");

        new GetBatch().execute();

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals("select")){
                    // do nothing
                }
                else{
                    String item =adapterView.getItemAtPosition(i).toString();

                    Toast.makeText(adapterView.getContext(),"Selected:"+item,Toast.LENGTH_SHORT).show();

                    String string = item;
                    String[] parts = string.split("-");
                    part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(adapterView.getItemAtPosition(i).equals("select")){
                    // do nothing
                }
                else{
                    String item =adapterView.getItemAtPosition(i).toString();

                    Toast.makeText(adapterView.getContext(),"Selected:"+item,Toast.LENGTH_SHORT).show();

                    String string = item;
                    String[] parts = string.split("-");
                    part3 = parts[0]; // 004
                    String part2 = parts[1]; // 034556

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvw.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        btnGet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                register_text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioMale:
                        buttonSelected = "Male";
                        break;

                    case R.id.radioFemale:
                        buttonSelected = "Female";
                        break;

                    default:
                }
            }
        });

        child_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioMale1:
                        buttonSelected1 = "Male";
                        break;

                    case R.id.radioFemale1:
                        buttonSelected1 = "Female";
                        break;

                    default:
                }
            }
        });


        add_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                String role = "parent";
                nm = namet.getText().toString();
                gender = buttonSelected;
                mob = mob_no.getText().toString();
                pd = pwd.getText().toString();
                cpd = con_pwd.getText().toString();
                address = addr.getText().toString();
                cnm = cname.getText().toString();
                bd=tvw.getText().toString();
                af = add_fees.getText().toString();
                child_gender = buttonSelected1;
                gno = gr_no.getText().toString();
                reg_d = register_text.getText().toString();

                if(namet.length() == 0 || namet.equals("") || namet == null)
                {
                    namet.requestFocus();
                    namet.setError("Enter name");

                }
                else if (mob_no.length() == 0 || mob_no.equals("") || mob_no == null) {

                    mob_no.requestFocus();
                    mob_no.setError("Enter mobile number");

                }
                else if (mob_no.length() != 10){
                    mob_no.requestFocus();
                    mob_no.setError("10 Digit number");

                }
                else if (pwd.length() == 0 || pwd.equals("") || pwd == null)
                {
                    //EditText is not empty
                    pwd.requestFocus();
                    pwd.setError("Enter the password");

                }
                else if (con_pwd.length() == 0 || con_pwd.equals("") || con_pwd == null)
                {
                        con_pwd.requestFocus();
                        con_pwd.setError("Confirm password");

                }
                  else if (addr.length() == 0 || addr.equals("") || addr == null)
                {
                    //EditText is not empty
                    addr.requestFocus();
                    addr.setError("Enter Address");
                }
                else if (cname.length() == 0 || cname.equals("") || cname == null)
                {
                    //EditText is not empty
                    cname.requestFocus();
                    cname.setError("Enter child name");
                }
                else if (tvw.length() == 0 || tvw.equals("") || tvw == null)
                {
                    //EditText is not empty
                    tvw.requestFocus();
                    tvw.setText("Enter date *");
                }
                else if (add_fees.length() == 0 || add_fees.equals("") || add_fees == null)
                {
                    //EditText is not empty
                    add_fees.requestFocus();
                    add_fees.setError("Enter fees");
                }
                else if (gr_no.length() == 0 || gr_no.equals("") || gr_no == null)
                {
                    //EditText is not empty
                    gr_no.requestFocus();
                    gr_no.setError("Enter Gr_no");
                }
                else if (register_text.length() == 0 || register_text.equals("") || register_text == null)
                {
                    //EditText is not empty
                    register_text.requestFocus();
                    register_text.setText("Enter Date *");
                }
                else {

                    new JsonPost().execute(mob, role, nm, gender, pd, child_count, address, cnm, child_gender, part1, part3, gno, af, bd, reg_d);

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

                        belt_level_names.add(res4);
                        Log.e("Belt Levels:", res4);

                    }

                    spin1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, belt_level_names));

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

                        res3 = a + "-" + eg;
                        Log.e("Concated String:-",res3);

                        select_batch.add(res3);
                        Log.e("FINAL BATCHID:", res3);
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

            spin2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, select_batch));
        }

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


            Toast.makeText(getActivity(),"Registration successful",Toast.LENGTH_SHORT).show();

        }
    }

    private String PostJson(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";


        try {
            java.net.URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=insert_users");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"u_mobile\":\""+params[0]+"\",\"u_role\":\""+ params[1]+"\"," +
                    "\"u_name\":\""+params[2] +"\",\"u_gender\":\""+params[3] +"\"," +
                    "\"u_password\":\""+params[4] +"\",\"uc_count\":\""+params[5] +"\"," +
                    "\"u_address\":\""+params[6] +"\",\"uc_name\":\""+params[7] +"\"," +
                    "\"uc_gender\":\""+params[8] +"\",\"uc_belt\":\""+params[9] +"\"," +
                    "\"uc_batch\":\""+params[10] +"\",\"uc_gr\":\""+params[11] +"\"," +
                    "\"uc_fee\":\""+params[12] +"\",\"uc_dob\":\""+params[13] +"\"," +
                    "\"uc_reg_date\":\""+params[14]+"\"}";
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