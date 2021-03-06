package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Addfees extends Fragment {

    private String TAG = Addfees.class.getSimpleName();

    String a;
    String res,abc;
    Spinner spin1, spin2;
    Button btn1, mMonth ,yYear;
    TextView mItemSelected,yItemSelected;
    EditText insert_fees;
    String part1,part3,res1;

    private ListView lv;
    SharedPreferences result1;

    ArrayList<String> select_batch;
    ArrayList<String> select_student = new ArrayList<String>();;
    ArrayAdapter<String> myAdapter;

    ArrayList<HashMap<String, String>> fee_info;

    String select_item_id,result,select_student_id;
    String erg = "";
    String erg1 = "";
    String URL = "http://10.0.43.1/kungfu2/api/v1/user.php?data=batches";
    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=student_info_for_trainer_by_batch";
    String URL2 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=student_last_2_fee_info";

    public Addfees() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_addfees, container, false);

        spin1 = (Spinner) v.findViewById(R.id.spinner1);
        spin2 = (Spinner) v.findViewById(R.id.spinner2);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        insert_fees = v.findViewById(R.id.get_fees);
        btn1 = v.findViewById(R.id.add_button);
        mMonth = v.findViewById(R.id.select_mnth_btn);
        yYear = v.findViewById(R.id.select_yr_btn);
        mItemSelected = v.findViewById(R.id.select_month_tvw);
        yItemSelected = v.findViewById(R.id.select_yr_tvw);

//        checkedMonths = new boolean[listMonths.length + 1];

        select_batch = new ArrayList<String>();
        select_batch.add(0,"select");
        new GetContacts().execute();

        select_student.add(0,"select");

        fee_info = new ArrayList<>();

        lv = v.findViewById(R.id.list_fees);

        mMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);
                final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                DatePickerDialog dp = new DatePickerDialog(getActivity(),R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

//                                erg = String.valueOf(dayOfMonth);
                                erg1 = String.valueOf(monthOfYear + 1);
                                erg = String.valueOf(year);

                                mItemSelected.setText(erg1);
                                Log.e("ERGG",erg1);

                            }

                        }, y, m, d);
                dp.setTitle("Calender");
                dp.setMessage("Please select date");

                dp.show();

            }
        });


        yYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yItemSelected.setText(erg);

            }
        });

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(parent.getItemAtPosition(position).equals("select")){
                    // do nothing

                }
                else{

                    String item1 =parent.getItemAtPosition(position).toString();

                    String string = item1;
                    String[] parts = string.split("-");
                    part3 = parts[0];
                    String part4 = parts[1];

                    select_item_id =String.valueOf(spin1.getSelectedItemId());
                    Log.e("Selected item:",select_item_id);

                    SharedPreferences sp1 = getActivity().getSharedPreferences("batchinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("batchid", part3);
                    editor.clear();
                    editor.commit();

                    new GetContacts1(part3).execute();

                    myAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, select_student);
                    spin2.setAdapter(myAdapter);

                    myAdapter.clear();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).equals("select")){
                    // do nothing

                }
                else{

                    String item =parent.getItemAtPosition(position).toString();

                    String string = item;
                    String[] parts = string.split("-");
                    part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556

                    Log.e("Part1",part1);

                    SharedPreferences sp1 = getActivity().getSharedPreferences("getsidfee", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("s_id", part1);
                    editor.clear();
                    editor.commit();

                    new GetLastFeeInfo(part1).execute();
                    ListAdapter adapter2 = new SimpleAdapter(
                            getActivity(), fee_info,
                            R.layout.listforfeesrecordparent, new String[]{"j","f","g","k","b"},
                            new int[]{R.id.mnth,R.id.fee1,R.id.Feerecvdate1,R.id.feepaidyr1,R.id.feereciver1
                            });

                    lv.setAdapter(adapter2);

                    fee_info.clear();

                    Toast.makeText(parent.getContext(),"Selected:"+part1,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                result1 = getActivity().getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
                final String temp = result1.getString("userid", "");

                String get_fee = insert_fees.getText().toString();
                String get_month = mItemSelected.getText().toString();
                String get_yr = yItemSelected.getText().toString();

                if(insert_fees.length() == 0 || insert_fees == null)
                {
                    insert_fees.requestFocus();
                    insert_fees.setError("Enter fees");

                }
                else if (mItemSelected.length() == 0 || mItemSelected.equals("") || mItemSelected == null) {

                    mItemSelected.requestFocus();
                    mItemSelected.setText("Enter Date *");
                    mItemSelected.setTextColor(R.color.red);
                }
                else if (yItemSelected.length() == 0 || yItemSelected.equals("") || yItemSelected == null) {

                    yItemSelected.requestFocus();
                    yItemSelected.setText("Enter Date *");
                    yItemSelected.setTextColor(R.color.red);
                }else {
                    new JsonPost().execute(part1, get_fee, temp, get_yr, get_month);
                }
            }
        });

        return v;
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

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    String a = c.getString("b_id");
                    String b = c.getString("b_day");
                    String cd = c.getString("b_from_time");
                    String d = c.getString("b_to_time");
                    String eg = c.getString("b_name");
                    String f = c.getString("b_location");



//                    HashMap<String, String> contact = new HashMap<>();
//
//                    // adding each child node to HashMap key => value
//                    contact.put("eg", eg);
//
//                    select_batch.add(eg);

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
            String jsonStr = sh.makeServiceCall1(URL1);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("0");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        a = c.getString("uc_id");
                        String b = c.getString("uc_name");
                        String cd = c.getString("fulladdr");
                        String e = c.getString("u_mob");

                        res = a + "-" + b;

                        Log.e("Concated:-",res);

                        select_student.add(res);
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

            myAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, select_student);
            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spin2.setAdapter(myAdapter);
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

            Toast.makeText(getActivity(),"Fees added succesfully",Toast.LENGTH_SHORT).show();

            insert_fees.getText().clear();
            mItemSelected.setText("");
            yItemSelected.setText("");
            spin1.setSelection(0);
            spin2.setSelection(0);

        }
    }

    private String PostJson(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";


        try {
            java.net.URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=insert_fees");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"student_id\":\""+params[0]+"\",\"sf_amount\":\""+ params[1]+"\"," +
                    "\"sf_reciever\":\""+params[2] +"\",\"sf_year\":\""+params[3] +"\"," +
                    "\"sf_month\":\""+params[4] +"\"}";
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


    private class GetLastFeeInfo extends AsyncTask<Void, Void, Void> {

        String item2;
        public GetLastFeeInfo(String item) {

            this.item2 =item;

            Log.e("Selected stud id:",item2);

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler1 sh = new HttpHandler1(getActivity());


            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall2(URL2);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("student_fees");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String a = c.getString("uc_name");
                        String b = c.getString("t_name");
                        String cd = c.getString("sf_id");
                        String e = c.getString("sf_s_id");
                        String f = c.getString("sf_total");
                        String g = c.getString("sf_date");
                        String h = c.getString("sf_reciever");
                        String j = c.getString("sf_month");
                        String k = c.getString("sf_year");
                        String l = c.getString("sf_status");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("a", a);
                        contact.put("b", b);
                        contact.put("cd",cd);
                        contact.put("e", e);
                        contact.put("f", f);
                        contact.put("g", g);
                        contact.put("h", h);
                        contact.put("j", j);
                        contact.put("k", k);
                        contact.put("l", l);

                        // adding contact to contact list
                        fee_info.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "No Record Found",
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
//
//                pDialog.dismiss();

            ListAdapter adapter2 = new SimpleAdapter(
                    getActivity(), fee_info,
                    R.layout.listforfeesrecordparent, new String[]{"j","f","g","k","b"},
                    new int[]{R.id.mnth,R.id.fee1,R.id.Feerecvdate1,R.id.feepaidyr1,R.id.feereciver1
                    });

            lv.setAdapter(adapter2);

            ((SimpleAdapter) adapter2).notifyDataSetChanged();

//            if(lv.getCount()==0) {
//                //empty, show alertDialog
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage("No Record Found")
//                        .setCancelable(true)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//
//                                Intent intent1 = new Intent(getActivity(),MainActivity.class);
//                                startActivity(intent1);
//                            }
//                        });
//                final AlertDialog alert = builder.create();
//                alert.setOnShowListener( new DialogInterface.OnShowListener() {
//                    @SuppressLint("ResourceAsColor")
//                    @Override
//                    public void onShow(DialogInterface arg0) {
//                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.darkblue);
//                    }
//                });
//
//                alert.show();
//            }
        }

    }

}