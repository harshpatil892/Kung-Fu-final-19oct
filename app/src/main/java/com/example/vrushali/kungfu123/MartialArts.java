package com.example.vrushali.kungfu123;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MartialArts extends AppCompatActivity {

    public WebView webview,web,webview3,webview4,webview5,webview7,webview6,webview8;
    public TextView txt,txt1,txt2,txt3;
    public CardView cardView,cardView1,card2,card22,card3,card33,card4,card44;
    private TextView mTextMessage;


    SharedPreferences store_id,reg_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (InitApplication.getInstance().isNightModeEnabled()) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        setContentView(R.layout.activity_martial_arts);
        txt = (TextView) findViewById(R.id.textclick);
        txt1 = (TextView)findViewById(R.id.textclick1);
        txt2=(TextView)findViewById(R.id.textclick2);
        txt3=(TextView)findViewById(R.id.textclick3);

        webview = (WebView) findViewById(R.id.webview);
        web = (WebView) findViewById(R.id.webview2);
        webview3=(WebView)findViewById(R.id.webview3);
        webview4=(WebView)findViewById(R.id.webview4);
        webview5=(WebView)findViewById(R.id.webview5);
        webview7=(WebView)findViewById(R.id.webview7);
        webview6=(WebView)findViewById(R.id.webview6);
        webview8=(WebView)findViewById(R.id.webview8);

        cardView = (CardView) findViewById(R.id.card_view_inner);
        cardView1 = (CardView) findViewById(R.id.card_view_inner1);
        card2 = (CardView) findViewById(R.id.card2);
        card22 = (CardView) findViewById(R.id.card22);
        card3 = (CardView) findViewById(R.id.card3);
        card33 = (CardView) findViewById(R.id.card33);
        card4 = (CardView) findViewById(R.id.card4);
        card44 = (CardView) findViewById(R.id.card44);

        webview.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("http://www.aikfwsa.com/app/2017/04/12/the-importance-of-martial-arts-to-youth/");
        webview3.getSettings().setJavaScriptEnabled(true);
        webview3.loadUrl("http://www.aikfwsa.com/app/2017/04/12/importance-and-benefits-of-martial-arts-in-real-life/");
        webview5.getSettings().setJavaScriptEnabled(true);
        webview5.loadUrl("http://www.aikfwsa.com/app/2017/04/12/25-benefits-of-martial-arts-for-your-child/");
        webview7.getSettings().setJavaScriptEnabled(true);
        webview7.loadUrl("http://www.aikfwsa.com/app/2017/04/11/second-test/");




        store_id = getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        final String id = store_id.getString("userid","");

        reg_token = getSharedPreferences("reg_token", Context.MODE_PRIVATE);
        final String token = reg_token.getString("token","");

        Log.e("Stored token:",token);

        new GenerateToken().execute(id,token);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl("http://www.aikfwsa.com/app/2017/04/12/the-importance-of-martial-arts-to-youth/");
                cardView.setVisibility(View.INVISIBLE);
                cardView1.setVisibility(View.VISIBLE);
                card2.setVisibility(View.INVISIBLE);
                card22.setVisibility(View.INVISIBLE);
                card3.setVisibility(View.INVISIBLE);
                card33.setVisibility(View.INVISIBLE);
                card4.setVisibility(View.INVISIBLE);
                card44.setVisibility(View.INVISIBLE);

            }
        });

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                webview4.getSettings().setJavaScriptEnabled(true);
                webview4.loadUrl("http://www.aikfwsa.com/app/2017/04/12/importance-and-benefits-of-martial-arts-in-real-life/");
                cardView.setVisibility(View.INVISIBLE);
                cardView1.setVisibility(View.INVISIBLE);
                card2.setVisibility(View.INVISIBLE);
                card22.setVisibility(View.VISIBLE);
                card3.setVisibility(View.INVISIBLE);
                card33.setVisibility(View.INVISIBLE);
                card4.setVisibility(View.INVISIBLE);
                card44.setVisibility(View.INVISIBLE);

            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview6.getSettings().setJavaScriptEnabled(true);
                webview6.loadUrl("http://www.aikfwsa.com/app/2017/04/12/25-benefits-of-martial-arts-for-your-child/");
                cardView.setVisibility(View.INVISIBLE);
                cardView1.setVisibility(View.INVISIBLE);
                card2.setVisibility(View.INVISIBLE);
                card22.setVisibility(View.INVISIBLE);
                card3.setVisibility(View.INVISIBLE);
                card33.setVisibility(View.VISIBLE);
                card4.setVisibility(View.INVISIBLE);
                card44.setVisibility(View.INVISIBLE);
            }
        });

        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview8.getSettings().setJavaScriptEnabled(true);
                webview8.loadUrl("http://www.aikfwsa.com/app/2017/04/11/second-test/");

                cardView.setVisibility(View.INVISIBLE);
                cardView1.setVisibility(View.INVISIBLE);
                card2.setVisibility(View.INVISIBLE);
                card22.setVisibility(View.INVISIBLE);
                card3.setVisibility(View.INVISIBLE);
                card33.setVisibility(View.INVISIBLE);
                card4.setVisibility(View.INVISIBLE);
                card44.setVisibility(View.VISIBLE);

            }
        });

        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
    }



    class GenerateToken extends AsyncTask<String ,String,String> {


        @Override
        protected String doInBackground(String... params) {

            String response=PostJson1(params);
            Log.e("params",""+params);
            Log.e("response",""+response);
            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Toast.makeText(.this,"Registration successful",Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(SigninActivity.this,LoginActivity.class));

//            textView.setText(result);
        }
    }

////////////////////
private void hideNavigationBar() {
    this.getWindow().getDecorView()
            .setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
}
    private String PostJson1(String[] params)  {

        Log.e("postjson method invoke","");

        HttpURLConnection connection = null;
        BufferedReader br=null;
        String data="";


        try {
            URL url =new URL("http://10.0.43.1/kungfu2/api/v1/user.php?data=save_token");
            connection = (HttpURLConnection) url.openConnection();
            String urlparam = "{\"uid\":\""+params[0]+"\",\"gcmid\":\""+ params[1]+"\"}";
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
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(MartialArts.this,MainActivity.class);
        startActivity(intent);

    }
}
