package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class ComFragment extends Fragment {
  private String TAG = ComFragment.class.getSimpleName();

  SharedPreferences role1;

  static String UserId;
  String role;
  private ProgressDialog pDialog;
  private ListView lv;
  private static String url = "http://10.0.43.1/kungfu2/api/v1/user.php?data=belt_comp_result";
  ArrayList<HashMap<String, String>> contactList;

  public ComFragment(Context context) {
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
    View v =inflater.inflate(R.layout.fragment_com,container,false);
    contactList = new ArrayList<>();
    hideNavigationBar();
    lv = (ListView)v.findViewById(R.id.compresult);

    new GetContacts().execute();
    return v;
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
      // Showing progress dialog
      pDialog = new ProgressDialog(getActivity());
      pDialog.setMessage("Please wait...");
      pDialog.setCancelable(false);
      pDialog.show();
    }
    @Override
    protected Void doInBackground(Void... arg0) {
      Httpforcompresult hp = new Httpforcompresult(getActivity());
      // Making a request to url and getting response
      String jsonStr = hp.makeServiceCall(url);
      Log.e(TAG, "Response from url: " + jsonStr);
      if (jsonStr != null) {
        try {
          JSONObject jsonObj = new JSONObject(jsonStr);

          // Getting JSON Array node
          JSONArray contacts = jsonObj.getJSONArray("data");

          // looping through All Contacts
          for (int i = 0; i < contacts.length(); i++) {
            JSONObject c = contacts.getJSONObject(i);

            String id = c.getString("bc_name");
            String name = c.getString("bc_reg_date");
            String email = c.getString("bc_reg_result");

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

//                        contact.put("tcid", tcid);
//                        contact.put("tcregion", tcregion);

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
      if (pDialog.isShowing())

        pDialog.dismiss();
      /**
       * Updating parsed JSON data into ListView
       * */
      ListAdapter adapter = new SimpleAdapter(
              getActivity(), contactList,
              R.layout.listforcompresult, new String[]{"id", "name",
              "email"}, new int[]{R.id.id,
              R.id.name, R.id.email});
      lv.setAdapter(adapter);


      if(lv.getCount()==0) {
        //empty, show alertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("No event for current time")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                  }
                });
        AlertDialog alert = builder.create();
        alert.show();
      }

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
