package com.example.vrushali.kungfu123;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;

public class Feedbackfragment extends Fragment {

    private String TAG = Feedback.class.getSimpleName();
    public RatingBar rt1, rt2, rt3, rt4;
    public EditText ed1, ed2, ed3, ed4;
    public Button btn;
    String shibu,pratish;
    private ProgressDialog pDialog;
    private ListView lv;
    String trainid = "5";
    String studid = "13";
    // URL to get contacts JSON
    private static String url = "http://10.0.43.1/kungfu2/api/v1/user.php?data=get_feedback_questions";

    ArrayList<HashMap<String, String>> contactList;

    public Feedbackfragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feedbackfragment, container, false);

        contactList = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.list);
        rt1 = (RatingBar) v.findViewById(R.id.rating1);
        rt2 = (RatingBar) v.findViewById(R.id.rating2);
        rt3 = (RatingBar) v.findViewById(R.id.rating3);
        rt4 = (RatingBar) v.findViewById(R.id.rating4);

        ed1 = (EditText) v.findViewById(R.id.note1);
        ed2 = (EditText) v.findViewById(R.id.note2);
        ed3 = (EditText) v.findViewById(R.id.note3);
        ed4 = (EditText) v.findViewById(R.id.note4);

        btn = (Button) v.findViewById(R.id.button);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String rate1 = String.valueOf(Math.round(rt1.getRating()));
                String rate2 = String.valueOf(Math.round(rt2.getRating()));
                String rate3 = String.valueOf(Math.round(rt3.getRating()));
                String rate4 = String.valueOf(Math.round(rt4.getRating()));

                String nt1 = ed1.getText().toString();
                String nt2 = ed2.getText().toString();
                String nt3 = ed3.getText().toString();
                String nt4 = ed4.getText().toString();
                Log.e("Notes:", nt4);

                 shibu=trainid;
                 pratish=studid;

                new JsonPost().execute(shibu,pratish, rate1, nt1,rate2,nt2,rate3,nt3,rate4,nt4);
            }
        });

//        new GetContacts().execute();
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
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_IMMERSIVE
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
            Toast.makeText(getActivity(),"Feedback Submitted",Toast.LENGTH_SHORT).show();
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
            URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=post_feedback");
            connection = (HttpURLConnection) url.openConnection();

            String urlparam = "{\"trainer_id\":\""+shibu+"\",\"student_id\":\""+ pratish+"\"," +
                    "\"rating_1\":\""+params[2] +"\",\"note_1\":\""+params[3] +"\"," +
                    "\"rating_2\":\""+params[4] +"\",\"note_2\":\""+params[5] +"\"," +
                    "\"rating_3\":\""+params[6] +"\",\"note_3\":\""+params[7] +"\"," +
                    "\"rating_4\":\""+params[8] +"\",\"note_4\":\""+params[9] +"\"}";

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
//    private class GetContacts extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            HttpHandlerf sh = new HttpHandlerf();
//
//            // Making a request to url and getting response
//            String jsonStr = sh.makeServiceCall(url);
//
//            Log.e(TAG, "Response from url: " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    // Getting JSON Array node
//                    JSONArray contacts = jsonObj.getJSONArray("data");
//
//                    // looping through All Contacts
//                    for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);
//
//                        String id = c.getString("fq_name");
//                        String name = c.getString("fq_name");
////                        String email = c.getString("tc_location");
////                        String address = c.getString("address");
////                        String gender = c.getString("gender");
////
////                        // Phone node is JSON Object
////                        JSONObject phone = c.getJSONObject("phone");
////                        String mobile = phone.getString("mobile");
////                        String home = phone.getString("home");
////                        String office = phone.getString("office");
//
//                        // tmp hash map for single contact
//                        HashMap<String, String> contact = new HashMap<>();
//
//                        // adding each child node to HashMap key => value
//                        contact.put("id", id);
//                        contact.put("name", name);
////                        contact.put("email", email);
////                        contact.put("mobile", mobile);
//
//                        // adding contact to contact list
//                        contactList.add(contact);
//                    }
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getActivity(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    });
//
//                }
//            } else {
//                Log.e(TAG, "Couldn't get json from server.");
//               getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG)
//                                .show();
//                    }
//                });
//
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            // Dismiss the progress dialog
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            /**
//             * Updating parsed JSON data into ListView
//             * */
//            ListAdapter adapter = new SimpleAdapter(
//                    getActivity(), contactList,
//                    R.layout.fragment_feedbackfragment, new String[]{"name", "email",
//                    "mobile"}, new int[]{R.id.name,
//                    R.id.email, R.id.mobile});
//
//            lv.setAdapter(adapter);
//        }
//
//    }

