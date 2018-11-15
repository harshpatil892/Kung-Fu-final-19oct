package com.example.vrushali.kungfu123;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class Individual_notify extends Fragment {


    private String TAG = Individual_notify.class.getSimpleName();

    SharedPreferences sp;
    Button noti;
    Spinner spin1;
    String item1,select_item_id,title,address,res1,batch,sorted;
    String uiddd="23";
    EditText harsh,harshal;
    CheckBox checkBox_getid;
    ListView listView;
    ArrayList namelist;
    private CustomAdapterNotification adapter;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ArrayList<String> select_batch;
    String URL = "http://10.0.43.1/kungfu2/api/v1/user.php?data=batches";
    String URL1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=student_info_for_trainer_by_batch";


    public Individual_notify() {
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
        View v =inflater.inflate(R.layout.fragment_individual_notify,container,false);

        spin1 = (Spinner) v.findViewById(R.id.spinner1);

        namelist = new ArrayList<>();

        listView = v.findViewById(R.id.listforstudinfo);
        harsh=(EditText)v.findViewById(R.id.name);
        harshal=(EditText)v.findViewById(R.id.address_area);

        select_batch = new ArrayList<String>();
        select_batch.add(0,"select");

        new GetContacts().execute();

//        adapter = new CustomAdapterNotification(namelist, getActivity());
//
//        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                String value = listView.getItemAtPosition(position).toString();
                Log.e("VALUE OF:-",value);
                Log.e("ITEM SELECETD",value);
                sorted = value.replaceAll("[a-z,{}.A-Z=]","");
                Log.e("HARSHAL :",sorted);

                title=harsh.getText().toString();
                address=harshal.getText().toString();

                FirebaseMessaging.getInstance().subscribeToTopic("test");
                FirebaseInstanceId.getInstance().getToken();

                new JsonPost().execute(title,address,sorted);
//                UserModelNotify dataModel= (UserModelNotify) namelist.get(position);
//                dataModel.checked = !dataModel.checked;
////                adapter.notifyDataSetChanged();

            }
        });

//        noti.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                title=harsh.getText().toString();
//                address=harshal.getText().toString();
//
//                FirebaseMessaging.getInstance().subscribeToTopic("test");
//                FirebaseInstanceId.getInstance().getToken();
//
//                SharedPreferences sp1 = getActivity().getSharedPreferences("notify", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp1.edit();
//
//                editor.putString("noti_title", title);
//                editor.putString("noti_msg", address);
//                editor.commit();
//
//
//                Log.e("TITLE:",title);
//                Log.e("Adreessss:-",address);
//                new JsonPost().execute(title,address,uiddd);
//
//            }
//        });
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals("select")){

                }
                else{
                    item1 =adapterView.getItemAtPosition(i).toString();
//                    select_item_id =String.valueOf(spin1.getSelectedItemId());
//                    Log.e("Selected item:",select_item_id);

                    String string = item1;
                    String[] parts = string.split("-");
                    batch = parts[0]; // 004
                    String part2 = parts[1]; // 034556



                    SharedPreferences sp1 = getActivity().getSharedPreferences("batchinfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("batchid", batch);

                    new GetListNames(batch).execute();

                    editor.clear();
                    editor.commit();

                    Toast.makeText(adapterView.getContext(),"Selected:"+item1,Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return v;
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
            HttpHandlerBatch sh = new HttpHandlerBatch(getContext());


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

//                        HashMap<String, String> contact = new HashMap<>();
//
//                        contact.put("eg", eg);
//
//                        select_batch.add(eg);


                        res1 = a + "-" + eg;
                        select_batch.add(res1);

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

            spin1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, select_batch));
        }

    }

    class GetListNames extends AsyncTask<Void, Void, Void> {


        String item1;
        public GetListNames(String item) {

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
        protected Void doInBackground(Void... arg0) {
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

                        String a = c.getString("uc_id");
                        String b = c.getString("uc_name");
                        String cd = c.getString("fulladdr");
                        String d = c.getString("u_mob");

                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("a",a);
                        contact.put("b",b);

                        SharedPreferences sp1=getActivity().getSharedPreferences("getid", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp1.edit();
                        editor.putString("studid", a);
                        editor.commit();

                        namelist.add(contact);

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
//            adapter = new CustomAdapterNotification(namelist, getActivity());
//
//            listView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//


            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), namelist,
                    R.layout.listview_notify, new String[]{"a", "b",
            }, new int[]{R.id.id,
                    R.id.name});

            listView.setAdapter(adapter);

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
            Toast.makeText(getActivity(),"Notification sent",Toast.LENGTH_SHORT).show();
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
            java.net.URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=indivisual_notification");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"msg\":\""+title+"\",\"title\":\""+address+"\",\"uc_id\":\""+ sorted +"\"}";
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

