package com.example.vrushali.kungfu123;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
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



public class Student_registration extends Fragment{

    private String TAG = Student_registration.class.getSimpleName();

    String nm,bd,reg_d,gender,mn,ad,spin_data1,spin_data2,ad_f,pd,cpd,gn;
    EditText name,mob_no,addr,ad_fee,pwd,con_pwd,gr_no;
    String buttonSelected,belt,batch,part1,part3;

    RadioGroup radio_group;
    RadioButton r_male,r_female;

    Fragment fragment;
    DatePickerDialog picker;
    Button btnGet,btnGet1,save;
    TextView tvw,register_text;

    Spinner spin1,spin2;

    ArrayList<String> belt_level_names;
    ArrayList<String> select_batch;
    String URL="http://10.0.43.1/kungfu2/api/v1/user.php?data=belt_levels";
    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=batches";

    public Student_registration() {


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_student_registration,container,false);

        name = v.findViewById(R.id.name);
        mob_no = v.findViewById(R.id.mob_no);
        addr = v.findViewById(R.id.edittextarea);
        ad_fee = v.findViewById(R.id.ad_fees);
        pwd = v.findViewById(R.id.pwd);
        con_pwd = v.findViewById(R.id.con_pwd);
        gr_no = v.findViewById(R.id.gr_no);

        hideNavigationBar();

        btnGet = (Button) v.findViewById(R.id.select_bdate);
        tvw = (TextView) v.findViewById(R.id.birthdate);

        btnGet1 = (Button) v.findViewById(R.id.select_reg_date);
        register_text = (TextView) v.findViewById(R.id.registerdate);

        save = v.findViewById(R.id.button_save);

        radio_group = v.findViewById(R.id.radioGroupStudent);
        r_male = v.findViewById(R.id.radioMale);
        r_female = v.findViewById(R.id.radioFemale);

        spin1 = (Spinner) v.findViewById(R.id.s_beltspin);

        spin2 = (Spinner) v.findViewById(R.id.s_batchspin);

        belt_level_names=new ArrayList<String>();
        loadSpinnerData(URL);

        belt_level_names.add(0,"select");

        select_batch = new ArrayList<String>();

        select_batch.add(0,"select");
        new GetContacts().execute();

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                    String item1 =adapterView.getItemAtPosition(i).toString();

//                    Toast.makeText(adapterView.getContext(),"Selected:"+item,Toast.LENGTH_SHORT).show();

                    String string = item1;
                    String[] parts = string.split("-");
                    part1 = parts[0]; // 004
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

//
//        if(name.getText().toString() != "" && tvw.getText().toString() !="" && register_text.getText().toString() !=""
//                && buttonSelected !=""&& mob_no.getText().toString() !="" &&addr.getText().toString() !=""
//                &&spin_data1=="select" && spin_data2=="select"
//                &&ad_fee.getText().toString() !="" && pwd.getText().toString() !=""&& con_pwd.getText().toString() !=""
//                &&gr_no.getText().toString() !="" ){
//
//            save.setEnabled(true);
//
//        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String role = "student";
                nm = name.getText().toString();
                bd=tvw.getText().toString();
                reg_d=register_text.getText().toString();
                gender =buttonSelected;
                mn= mob_no.getText().toString();
                ad = addr.getText().toString();
                ad_f = ad_fee.getText().toString();
                pd = pwd.getText().toString();
                cpd = con_pwd.getText().toString();
                gn = gr_no.getText().toString();

                new JsonPost().execute(mn,role,nm,gender,pd,ad,bd,reg_d,part1,ad_f,part3,gn);

            }
        });

        return v;
    }

//    public void register(){
//
//        if(!validate()){
//            Toast.makeText(getActivity(),"Data submission cancelled..",Toast.LENGTH_SHORT).show();
//        }else {
//
//            success();
//        }
//    }
//
//    public void success(){
//
//        Toast.makeText(getActivity(),"Data submitted successfully",Toast.LENGTH_SHORT).show();
//    }
//
//
//    public boolean validate(){
//        boolean valid = true;
//
//        if(TextUtils.isEmpty(nm)){
//            name.setError("Please enter name");
//            valid = false;
//        }
//
//
//        if(TextUtils.isEmpty(bd)){
//            tvw.setError("Please select birth date");
//            valid = false;
//        }
//
//        if(TextUtils.isEmpty(reg_d)){
//            register_text.setError("Please select registration date");
//            valid = false;
//        }
//
//        int selectedId = radio_group.getCheckedRadioButtonId();
//        radioButton = (RadioButton) radio_group.findViewById(selectedId);
//
//
//        if(selectedId==-1){
//            Toast.makeText(getActivity(),"Please select gender",Toast.LENGTH_SHORT).show();
//        }
//
//        if(TextUtils.isEmpty(mn)){
//            mob_no.setError("Please enter mobile no.");
//            valid = false;
//        }
//
//        if(TextUtils.isEmpty(ad)){
//            addr.setError("Please enter address");
//            valid = false;
//        }
//
//        if(spin_data1.equals("select")){
//
//            Toast.makeText(getContext(), "Select belt-level", Toast.LENGTH_SHORT).show();
//        }
//
//        if(spin_data2.equals("select")){
//
//            Toast.makeText(getContext(), "Select batch", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//        if(TextUtils.isEmpty(ad_f)){
//            ad_fee.setError("Please enter fees");
//            valid = false;
//        }
//
//        if(TextUtils.isEmpty(pd)){
//            pwd.setError("Please enter password");
//            valid = false;
//        }
//
//        if(TextUtils.isEmpty(cpd)){
//            con_pwd.setError("Please enter password");
//            valid = false;
//        }else if(pwd.getText().toString()!= con_pwd.getText().toString()){
//            Toast.makeText(getContext(),"Password is not same",Toast.LENGTH_SHORT).show();
//            valid =false;
//        }
//
//
//        if(TextUtils.isEmpty(gn)){
//            gr_no.setError("Please enter Gr no.");
//            valid = false;
//        }
//
//        return valid;
//    }



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
            URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=insert_users");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"u_mobile\":\""+params[0]+"\",\"u_role\":\""+ params[1]+"\"," +
                    "\"u_name\":\""+params[2] +"\",\"u_gender\":\""+params[3] +"\"," +
                    "\"u_password\":\""+params[4] +"\",\"u_address\":\""+params[5] +"\"," +
                    "\"u_dob\":\""+params[6] +"\",\"u_reg_date\":\""+params[7] +"\"," +
                    "\"selected_belt_level\":\""+params[8] +"\",\"uc_fee\":\""+params[9] +"\"," +
                    "\"selected_batch\":\""+params[10] +"\",\"uc_gr\":\""+params[11] +"\"}";
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

                        belt = temp + "-" + temp1;
                        Log.e("Concated String:-",belt);

                        belt_level_names.add(belt);
                        Log.e("Belt Levels:", belt);

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


            // Making a request to url and getting response
            String jsonStr = sh1.makeServiceCall(URL1);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) try {
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

                    batch = a + "-" + eg;
//                    Log.e("Concated String:-",belt);

                    select_batch.add(batch);
                    Log.e("Belt Levels:", batch);

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

            spin2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, select_batch));


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