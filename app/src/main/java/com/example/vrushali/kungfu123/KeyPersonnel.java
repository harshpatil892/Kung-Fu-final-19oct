package com.example.vrushali.kungfu123;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class KeyPersonnel extends AppCompatActivity {
    public WebView keypersonal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (InitApplication.getInstance().isNightModeEnabled()) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_personnel);


        keypersonal=(WebView)findViewById(R.id.keypersonal);

        keypersonal.getSettings().setTextSize(WebSettings.TextSize.NORMAL);

        keypersonal.getSettings().setJavaScriptEnabled(true);
        keypersonal.loadUrl("http://www.aikfwsa.com/app/key-personnel/");
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
