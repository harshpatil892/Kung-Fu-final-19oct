package com.example.vrushali.kungfu123;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class Lang extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_lang);

//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle(getResources().getString(R.string.app_name));

        Button ChangeLang = findViewById(R.id.ChangeLang);
        ChangeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChangeLanguageDialog();

          }
        });
    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"Hindi","English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Lang.this);
        mBuilder.setTitle("select");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
           if(i == 0){
               setLocale("hi");
               recreate();
           }else {
               setLocale("en");
               recreate();
           }

            }

        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    private void setLocale(String hi) {
        Locale locale = new Locale(hi);
        Locale.setDefault(locale);
        Configuration cofig = new Configuration();
        cofig.locale = locale;
        getBaseContext().getResources().updateConfiguration(cofig,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",hi);
        editor.apply();

    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Lang.this,MainActivity.class);
        startActivity(intent);
    }

}
