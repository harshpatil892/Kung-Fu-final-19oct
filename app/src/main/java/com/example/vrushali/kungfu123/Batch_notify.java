package com.example.vrushali.kungfu123;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

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


public class Batch_notify extends Fragment {


    private String TAG = Batch_notify.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    public EditText tit, des;
    public Button sendbtn;
    String harsh,title,address;
    // URL to get contacts JSON
    private static String url = "http://10.0.43.2/kungfu2/api/ v1/user.php?data=training_centers";

    ArrayList<HashMap<String, String>> contactList;

    public Batch_notify() {
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

        View v = inflater.inflate(R.layout.fragment_batch_notify, container, false);
        tit = (EditText) v.findViewById(R.id.name);
        des = (EditText) v.findViewById(R.id.edittextarea);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        contactList = new ArrayList<>();
        lv = (ListView)v.findViewById(R.id.listforbatch);

        new GetContacts().execute();

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
                title=tit.getText().toString();
                address=des.getText().toString();

                FirebaseMessaging.getInstance().subscribeToTopic("test");
                FirebaseInstanceId.getInstance().getToken();

                Log.e("TITLE:",title);
                Log.e("Adreessss:-",address);

                if(tit.length() == 0 || tit.equals("") || tit == null)
                {
                    tit.requestFocus();
                    tit.setError("Enter Title");

                }
                else if (des.length() == 0 || des.equals("") || des == null) {

                    des.requestFocus();
                    des.setError("Enter Date *");

                }else{

                new JsonPost().execute(title,address,harsh);

                }

            }
        });



        return v;
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandlerBatch sh = new HttpHandlerBatch(getActivity());

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

                        String id = c.getString("b_id");
                        String name = c.getString("b_name");
//                        String email = c.getString("tc_location");
//                        String address = c.getString("address");
//                        String gender = c.getString("gender");
//
//                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
//                        contact.put("email", email);
//                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);
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


//            mShimmerViewContainer.stopShimmerAnimation();

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), contactList,
                    R.layout.listforbatchnotify, new String[]{"id","name"
            }, new int[]{R.id.id,R.id.name
                    });

            lv.setAdapter(adapter);

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


            try {
                JSONObject jsonObject = new JSONObject(result);
                String success = jsonObject.getString("st");

                if(success.equals("1")){
                    Toast.makeText(getActivity(), "Notification Sent", Toast.LENGTH_SHORT).show();

                }else if(success.equals("2")){
                    Toast.makeText(getActivity(), "Some Fields Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "No user Exists", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();

                Toast.makeText(getActivity(),e.toString(), Toast.LENGTH_SHORT).show();

            }
        }
    }

////////////////////

    private String PostJson(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";


        try {
            java.net.URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=batch_notification");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"msg\":\""+title+"\",\"title\":\""+address+"\",\"batch_id\":\""+ harsh +"\"}";
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
