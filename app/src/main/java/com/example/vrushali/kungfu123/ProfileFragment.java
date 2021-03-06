package com.example.vrushali.kungfu123;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private String TAG = ProfileFragment.class.getSimpleName();

    private Bitmap bitmap;

    private static final int STORAGE_PERMISSION_CODE = 123;

    SharedPreferences result1,result2;
    private static TextView selectImage, shareImage, shareText;
    private static ImageView imageView;
    public TextView update;
    private static EditText textToShare;
    String suffix=".jpg";
    String fname,img;
    private final int select_photo = 1;
    Context context;
    static String UserId;
    String var1,var2,path1;
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url = "http://10.0.43.1/kungfu2/api/v1/user.php?data=profile";
    String url1 = "http://10.0.43.1/kungfu2/api/v1/user.php?data=update_image";

    ArrayList<HashMap<String, String>> contactList;

    private Uri filePath;

    final String end = "\r\n";
    final String twoHyphens = "--";

    public ProfileFragment(Context context) {


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

        requestStoragePermission();

        View v =inflater.inflate(R.layout.fragment_profile,container,false);
        contactList = new ArrayList<>();

        lv = (ListView)v.findViewById(R.id.profilelist);
        selectImage = (TextView)v.findViewById(R.id.select_image);
        imageView = (ImageView)v.findViewById(R.id.share_imageview);
        update=(TextView)v.findViewById(R.id.img_update);
//        hideNavigationBar();

        new GetContacts().execute();

        selectImage.setOnClickListener(this);
        update.setOnClickListener(this);

        return v;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getPath(Uri uri){

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == select_photo && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            Log.e("Filepath", String.valueOf(filePath));

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),select_photo);
    }

    public void uploadMultipart() {
        result1 = getActivity().getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        var1 = result1.getString("userrole","");

        result2 = getActivity().getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        var2 = result2.getString("userid","");

        Date date= new Date();

        long time = date.getTime();
        Log.e("Timestampcurr:-", String.valueOf(time));

        fname = time + "_" + var1 + "_" + var2 + suffix;

        Log.e("Role",var1);
        Log.e("UIDD",var2);
        Log.e("ffffff",fname);

        path1 = getPath(filePath);

        Log.e("Path1",path1);

        try{
            String uploadid = UUID.randomUUID().toString();

            new MultipartUploadRequest(getActivity(), uploadid, url)
                    .addFileToUpload(path1,"path1") //Adding file
                    .addParameter("file", fname) //Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

            new JsonPost().execute(fname);

        }catch (Exception exc){
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();

            Log.e("Exception",exc.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == selectImage) {
            showFileChooser();
        }
        if (v == update) {
            uploadMultipart();
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
                        img = c.getString("u_image");


                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("address", address);
                        contact.put("gender", gender);
                        contact.put("location", location);
                        contact.put("tclocation", tclocation);
                        contact.put("img",img);

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

            lv.setAdapter(adapter);


            Glide.with(getActivity()).load("http://10.0.43.1/kungfu2/images/"+img).into(imageView);

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

            Toast.makeText(getActivity(),"Image updated successfully",Toast.LENGTH_SHORT).show();

        }
    }

    private String PostJson(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";

        try {
            java.net.URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=update_image");

            FileInputStream fStream = new FileInputStream(path1);

            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type","multipart/form-data;boundary="+fname);
            connection.setRequestProperty("file", fname);

            DataOutputStream ds =new DataOutputStream(connection.getOutputStream());

            ds.writeBytes(twoHyphens + fname + end);
            ds.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                    + fname + "\"" + end);
            ds.writeBytes(end);

            int bufferSize = 1024;
            byte[] buffer1 = new byte[bufferSize];
            int length = -1;

            while((length = fStream.read(buffer1)) != -1) {
                ds.write(buffer1, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + fname + twoHyphens + end);

            fStream.close();
            ds.flush();
            ds.close();


            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                Toast.makeText(getActivity(), connection.getResponseMessage(), Toast.LENGTH_LONG);
            }


            StringBuffer buffer=new StringBuffer();

            InputStream is = connection.getInputStream();
            byte[] data2 = new byte[bufferSize];
            int leng = -1;
            while((leng = is.read(data2)) != -1) {
                buffer.append(new String(data2, 0, leng));
            }

            data = buffer.toString();

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
