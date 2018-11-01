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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EventFeedbackfragment extends Fragment {

    private String TAG = Feedback.class.getSimpleName();
    public RatingBar rt1, rt2, rt3, rt4;
    public EditText ed1, ed2, ed3, ed4;
    public Button btn;
    String evid = "2";
    String evtype = "tour";
    String studid = "12";
    private ProgressDialog pDialog;


    public EventFeedbackfragment() {

        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_feedbackfragment, container, false);
        hideNavigationBar();
        rt1 = (RatingBar) v.findViewById(R.id.rating1);
        ed1 = (EditText) v.findViewById(R.id.note1);
        btn = (Button) v.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String rate1 = String.valueOf(Math.round(rt1.getRating()));

                String nt1 = ed1.getText().toString();


                new JsonPost().execute(evid,evtype,studid,rate1, nt1);
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
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_IMMERSIVE
                );
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
            URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=event_feedback_submit");
            connection = (HttpURLConnection) url.openConnection();

            String urlparam = "{\"event_id\":\""+evid+"\",\"event_type\":\""+ evtype+"\"," +
                    "\"student_id\":\""+studid +"\",\"rating_5\":\""+params[3] +"\"," +
                    "\"note_5\":\""+params[4] +"\"}";

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
