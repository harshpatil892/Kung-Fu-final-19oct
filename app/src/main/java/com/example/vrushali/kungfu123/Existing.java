package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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


public class Existing extends Fragment{

    private String TAG = Existing.class.getSimpleName();

    String buttonSelected,res,res1,part1,part3,belt,part5;
    EditText ed_name,ed_fees,grno;

    DatePickerDialog picker;
    Button btnGet,btnGet1;
    TextView tvw,register_text;
    ArrayAdapter<String> myAdapter;
    RadioGroup radio_group;
    RadioButton r_male,r_female;

    Button save_info;

    Spinner batch_spin1,belt_spin,parent_spin;

    ArrayList<String> belt_level_names;
    ArrayList<String> select_batch1;
    ArrayList<String> select_parent;
    String item1,select_item_id;

    String URL="http://10.0.43.1/kungfu2/api/v1/user.php?data=belt_levels";
    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=batches";
    String URL2 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=get_parents";

    public Existing() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_existing,container,false);


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ed_name = v.findViewById(R.id.name);
        ed_fees = v.findViewById(R.id.ad_fees);
        grno = v.findViewById(R.id.gr_no);

        btnGet = (Button) v.findViewById(R.id.select_bdate);
        tvw = (TextView) v.findViewById(R.id.birthdate);

        btnGet1 = (Button) v.findViewById(R.id.select_reg_date);
        register_text = (TextView) v.findViewById(R.id.registerdate);

        batch_spin1 = (Spinner) v.findViewById(R.id.e_batchspin);

        parent_spin = (Spinner) v.findViewById(R.id.e_parentspin);

        belt_spin = (Spinner) v.findViewById(R.id.e_beltspin);

        radio_group = v.findViewById(R.id.radioGroup);
        r_male = v.findViewById(R.id.radioMale);
        r_female = v.findViewById(R.id.radioFemale);

        save_info = v.findViewById(R.id.button_save);



        belt_level_names=new ArrayList<String>();
        belt_level_names.add(0,"select");
        loadSpinnerData(URL);

        select_batch1 = new ArrayList<String>();
        select_batch1.add(0,"select");
        new GetContacts().execute();

        select_parent = new ArrayList<String>();
        select_parent.add(0,"select");


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


        batch_spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals("select")){
                    // do nothing

                }
                else{
                    item1 =adapterView.getItemAtPosition(i).toString();

                    String string = item1;
                    String[] parts = string.split("-");
                    part3 = parts[0]; // 004
                    String part2 = parts[1]; // 034556

//                    select_item_id =String.valueOf(batch_spin1.getSelectedItemId());
//                    Log.e("Selected item:",select_item_id);

                    SharedPreferences sp1 = getActivity().getSharedPreferences("batchinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("batchid", part3);
                    editor.clear();
                    editor.commit();

                    new GetContacts1(part3).execute();

                    myAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, select_parent);
                    parent_spin.setAdapter(myAdapter);

                    myAdapter.clear();


                    Toast.makeText(adapterView.getContext(),"Selected:"+item1,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        parent_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(adapterView.getItemAtPosition(i).equals("select")){
                    // do nothing

                }
                else{
                    String item = adapterView.getItemAtPosition(i).toString();
                    Log.e("Selected parent",item);
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


        belt_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(parent.getItemAtPosition(position).equals("select")){
                    // do nothing

                }
                else{
                    String item =parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(),"Selected:"+item,Toast.LENGTH_SHORT).show();

                    String string = item;
                    String[] parts = string.split("-");
                    part5 = parts[0]; // 004
                    String part2 = parts[1]; // 034556
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                picker = new DatePickerDialog(getActivity(),R.style.DialogTheme,
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
                picker = new DatePickerDialog(getActivity(),R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                register_text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        save_info.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                String ucname = ed_name.getText().toString();
                String bd=tvw.getText().toString();
                String regd=register_text.getText().toString();
                String gender = buttonSelected;
                String fees = ed_fees.getText().toString();
                String gn = grno.getText().toString();

                if(ed_name.length() == 0 || ed_name.equals("") || ed_name == null)
                {
                    ed_name.requestFocus();
                    ed_name.setError("Enter name");

                }
                else if (tvw.length() == 0 || tvw.equals("") || tvw == null) {

                    tvw.requestFocus();
                    tvw.setText("Enter Date *");

                }
                else if (register_text.length() == 0 || register_text.equals("") || register_text == null) {

                    register_text.requestFocus();
                    register_text.setError("Enter Date *");

                }
                else if (ed_fees.length() == 0 || ed_fees.equals("") || ed_fees == null) {

                    ed_fees.requestFocus();
                    ed_fees.setError("Enter fees");

                }
                else if (grno.length() == 0 || grno.equals("") || grno == null) {

                    grno.requestFocus();
                    grno.setError("Enter Gr_no");

                }
                else {

                    new JsonPost().execute(ucname, part1, gender, part5, gn, part3, fees, bd, regd);
                    grno.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
            }
        });


        return v;

    }

    private void loadSpinnerData(String url) {

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
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

                    belt_spin.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, belt_level_names));

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

//                        HashMap<String, String> contact = new HashMap<>();
//
//                        contact.put("eg", eg);

                        res1 = a + "-" + eg;
                        Log.e("Concated String:-",res1);

                        select_batch1.add(res1);
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

            batch_spin1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, select_batch1));
        }

    }


    class GetContacts1 extends AsyncTask<Void, Void, Void> {

        String item1;
        public GetContacts1(String item) {

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
            String jsonStr = sh.makeServiceCall(URL2);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String a = c.getString("u_id");
                        String b = c.getString("u_name");
//
                        res = a + "-" + b;

                        Log.e("Concated:-",res);

                        select_parent.add(res);
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

            myAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, select_parent);
            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            parent_spin.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();

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

            ed_name.getText().clear();
            tvw.setText("");
            register_text.setText("");
//            buttonSelected;
            ed_fees.getText().clear();
            grno.getText().clear();
            batch_spin1.setSelection(0);
//            parent_spin.setSelection();
            belt_spin.setSelection(0);


        }
    }

    private String PostJson(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";


        try {
            java.net.URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=add_child");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"uc_name\":\""+params[0]+"\",\"uc_parent\":\""+ params[1]+"\"," +
                    "\"uc_gender\":\""+params[2] +"\",\"uc_belt\":\""+params[3] +"\"," +
                    "\"uc_gr\":\""+params[4] +"\",\"uc_batch\":\""+params[5] +"\"," +
                    "\"uc_fee\":\""+params[6] +"\",\"uc_dob\":\""+params[7] +"\"," +
                    "\"uc_reg_date\":\""+params[8] +"\"}";
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

