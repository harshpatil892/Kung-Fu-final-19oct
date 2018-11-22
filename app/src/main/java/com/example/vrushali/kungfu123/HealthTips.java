package com.example.vrushali.kungfu123;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class HealthTips extends AppCompatActivity {

    public CardView hcard1,hcard11,hcard2,hcard22,hcard3,hcard33;
    public WebView health1,health2,health3,health11,health22,health33;
    public TextView click1,click2,click3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

        health1=(WebView)findViewById(R.id.health1);
        health2=(WebView)findViewById(R.id.health2);
        health3=(WebView)findViewById(R.id.health3);
        health11=(WebView)findViewById(R.id.health11);
        health22=(WebView)findViewById(R.id.health22);
        health33=(WebView)findViewById(R.id.health33);

        click1=(TextView)findViewById(R.id.healthclick1);
        click2=(TextView)findViewById(R.id.healthclick2);
        click3=(TextView)findViewById(R.id.healthclick3);

        hcard1=(CardView)findViewById(R.id.hcard1);
        hcard11=(CardView)findViewById(R.id.hcard11);
        hcard2=(CardView)findViewById(R.id.hcard2);
        hcard22=(CardView)findViewById(R.id.hcard22);
        hcard3=(CardView)findViewById(R.id.hcard3);
        hcard33=(CardView)findViewById(R.id.hcard33);

        health1.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        health1.getSettings().setJavaScriptEnabled(true);
        health1.loadUrl("http://www.aikfwsa.com/app/2017/04/17/top-12-health-and-beauty-benefits-of-green-tea/");
        health2.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        health2.getSettings().setJavaScriptEnabled(true);
        health2.loadUrl("http://www.aikfwsa.com/app/2017/04/12/womens-health-tips-for-heart-mind-and-body/");
        health3.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        health3.getSettings().setJavaScriptEnabled(true);
        health3.loadUrl("http://www.aikfwsa.com/app/2017/04/12/5-exercise-benefits-of-martial-arts/");

        click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                health11.getSettings().setJavaScriptEnabled(true);
                health11.loadUrl("http://www.aikfwsa.com/app/2017/04/17/top-12-health-and-beauty-benefits-of-green-tea/");
                hcard1.setVisibility(View.INVISIBLE);
                hcard11.setVisibility(View.VISIBLE);
                hcard2.setVisibility(View.INVISIBLE);
                hcard22.setVisibility(View.INVISIBLE);
                hcard3.setVisibility(View.INVISIBLE);
                hcard33.setVisibility(View.INVISIBLE);
            }
        });

        click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                health22.getSettings().setJavaScriptEnabled(true);
                health22.loadUrl("http://www.aikfwsa.com/app/2017/04/12/womens-health-tips-for-heart-mind-and-body/");
                hcard1.setVisibility(View.INVISIBLE);
                hcard11.setVisibility(View.INVISIBLE);
                hcard2.setVisibility(View.INVISIBLE);
                hcard22.setVisibility(View.VISIBLE);
                hcard3.setVisibility(View.INVISIBLE);
                hcard33.setVisibility(View.INVISIBLE);
            }
        });

        click3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                health3.getSettings().setJavaScriptEnabled(true);
                health33.loadUrl("http://www.aikfwsa.com/app/2017/04/12/5-exercise-benefits-of-martial-arts/");
                hcard1.setVisibility(View.INVISIBLE);
                hcard11.setVisibility(View.INVISIBLE);
                hcard2.setVisibility(View.INVISIBLE);
                hcard22.setVisibility(View.INVISIBLE);
                hcard3.setVisibility(View.INVISIBLE);
                hcard33.setVisibility(View.VISIBLE);
            }
        });

    }

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
}
