package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private String TAG = ProfileFragment.class.getSimpleName();

    SharedPreferences role1;
    private static TextView selectImage, shareImage, shareText;
    private static ImageView imageView;
    public TextView update;
    private static EditText textToShare;
    private static Uri imageUri = null;
    String uiddd="2";
    String suffix=".jpg";
    String file;
    private final int select_photo = 1;
    Context context;
    static String UserId;
    String role;
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url = "http://10.0.43.1/kungfu2/api/v1/user.php?data=profile";
    ArrayList<HashMap<String, String>> contactList;

    public ProfileFragment(Context context) {
        role1 = context.getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        role = role1.getString("userrole","");

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
        // Inflate the layout for this fragment

        View v =inflater.inflate(R.layout.fragment_profile,container,false);
        contactList = new ArrayList<>();

        lv = (ListView)v.findViewById(R.id.profilelist);
        selectImage = (TextView)v.findViewById(R.id.select_image);
        imageView = (ImageView)v.findViewById(R.id.share_imageview);
        update=(TextView)v.findViewById(R.id.update);
        hideNavigationBar();
        Log.e("IMAGEURI:", String.valueOf(imageUri));
        setListeners();
        harshal();
        new GetContacts().execute();
     update.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             new JsonPost().execute(file);
         }
     });
        return v;
    }

    public void harshal() {

//        Date date= new Date();
//
//        long time = date.getTime();
//       Log.e("Timestampcurr:-", String.valueOf(time));
//
//       file=time + "_" + "trainer" + "_" + uiddd +suffix;

    }

    private void setListeners() {
        selectImage.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_image:

                Intent in = new Intent(Intent.ACTION_PICK);
                in.setType("image/*");
                startActivityForResult(in, select_photo);// start

                break;
        }
    }

    public void onActivityResult(int requestcode, int resultcode,
                                    Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case select_photo:
                if (resultcode == RESULT_OK) {
                    try {

                        imageUri = imagereturnintent.getData();// Get intent
                        // data

                        Bitmap bitmap = Utils.decodeUri(getActivity(),
                                imageUri, 200);// call
                        Log.e("PATH:", String.valueOf(imageUri));


                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);// Set image over
                            // bitmap
                            // if bitmap is
                            // not null
                        } else {
                            shareImage.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),
                                    "Error while decoding image.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (FileNotFoundException e) {

                        e.printStackTrace();
                        Toast.makeText(getActivity(), "File not found.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        }

    }




    @Override
    public void onPause(){

        super.onPause();
        if(pDialog != null)
            pDialog.dismiss();
    }




    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            getUserId(this);
//            // Showing progress dialog
//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();


        }
        @Override
        protected Void doInBackground(Void... arg0) {
            Httpforprofile hp = new Httpforprofile(getActivity());


            // Making a request to url and getting response
            String jsonStr = hp.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("0");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("u_id");
                        String tclocation = c.getString("u_name");
                        String name = c.getString("u_name");
                        String email = c.getString("u_mob");
                        String address = c.getString("belt_name");
                        String gender = c.getString("u_reg_date");
                        String location = c.getString("u_status");


//                        String tcstatus = c.getString("tc_status");
//                        String uname = c.getString("uc_status");
//                        String ureg = c.getString("uc_reg_date");
//
//                         Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("data");
//                        String mobile = phone.getString("b_day");
//                        String home = phone.getString("tc_region");
//                        String office = phone.getString("tc_location");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("address", address);
                        contact.put("gender", gender);
                        contact.put("location", location);
//                        contact.put("tcid", tcid);
//                        contact.put("tcregion", tcregion);
                        contact.put("tclocation", tclocation);
//                        contact.put("tcstatus", tcstatus);
//                        contact.put("uname", uname);
//                        contact.put("ureg", ureg);

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
//
//                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), contactList,
                    R.layout.listforprofile, new String[]{"name", "email",
                    "address"}, new int[]{R.id.name,
                    R.id.email, R.id.address})
                    ;
//            ListAdapter adapter1 = new SimpleAdapter(
//                    MainActivity.this, contactList,
//                    R.layout.list_item1, new String[]{"name", "email",
//                    "mobile"}, new int[]{R.id.name,
//                    R.id.email, R.id.mobile});

            lv.setAdapter(adapter);
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
            Toast.makeText(getActivity(),"Registration successful",Toast.LENGTH_SHORT).show();
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
            URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=update_image");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = file;
            Log.e("data",urlparam);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","multipart/form-data");
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
