package com.example.vrushali.kungfu123;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class about_profile extends Fragment {
    public WebView profile1,profile2,profile3,profile4;

    public about_profile() {
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
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_about_profile,container,false);

        profile1=(WebView)v.findViewById(R.id.profile1);
        profile2=(WebView)v.findViewById(R.id.profile2);
        profile3=(WebView)v.findViewById(R.id.profile3);
        profile4=(WebView)v.findViewById(R.id.profile4);

        profile1.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        profile1.getSettings().setJavaScriptEnabled(true);
        profile1.loadUrl("http://www.aikfwsa.com/app/values/");
        profile2.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        profile2.getSettings().setJavaScriptEnabled(true);
        profile2.loadUrl("http://www.aikfwsa.com/app/goals/");
        profile3.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        profile3.getSettings().setJavaScriptEnabled(true);
        profile3.loadUrl("http://www.aikfwsa.com/app/mission/");
        profile4.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        profile4.getSettings().setJavaScriptEnabled(true);
        profile4.loadUrl("http://www.aikfwsa.com/app/vision/");
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
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }
}
