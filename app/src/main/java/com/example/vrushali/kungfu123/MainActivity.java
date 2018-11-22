package com.example.vrushali.kungfu123;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    CardView martial_arts_card, health_tips_card, aboutus_card;
    DrawerLayout drawer;
    NavigationView navigationView;
    SharedPreferences log_out,result;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (InitApplication.getInstance().isNightModeEnabled()) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        martial_arts_card = findViewById(R.id.martial_arts);
        health_tips_card = findViewById(R.id.health_tips);
        aboutus_card = findViewById(R.id.about_us_card);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPage);
        ImageAdapter adapterView = new ImageAdapter(this);
        mViewPager.setAdapter(adapterView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

       martial_arts_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MartialArts.class);
                startActivity(intent);
            }
        });

        health_tips_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HealthTips.class);
                startActivity(intent);
            }
        });

        aboutus_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutUs_home.class);
                startActivity(intent);
            }
        });


        result = getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        final String temp = result.getString("userrole", "");

        if (temp.equals("admin")) {


            Menu menu = navigationView.getMenu();
            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
                MenuItem menuItem= menu.getItem(menuItemIndex);
                if(menuItem.getItemId() == R.id.training_centre || menuItem.getItemId() == R.id.up_event
                        || menuItem.getItemId() == R.id.suggestion || menuItem.getItemId() == R.id.about_us
                        || menuItem.getItemId() == R.id.gallery || menuItem.getItemId() == R.id.inbox
                        || menuItem.getItemId() == R.id.logout){
                    menuItem.setVisible(true);
                }


                if(menuItem.getItemId() == R.id.login){
                    menuItem.setVisible(false);
                }
            }

        }

        if (temp.equals("trainer")) {


            Menu menu = navigationView.getMenu();
            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
            MenuItem menuItem= menu.getItem(menuItemIndex);
            if(menuItem.getItemId() == R.id.profile || menuItem.getItemId() == R.id.attendance_trainer
                    || menuItem.getItemId() == R.id.stud_detail || menuItem.getItemId() == R.id.notify
                    || menuItem.getItemId() == R.id.attend_record || menuItem.getItemId() == R.id.bank_detail
                    || menuItem.getItemId() == R.id.training_centre || menuItem.getItemId() == R.id.suggestion
                    || menuItem.getItemId() == R.id.logout){
                menuItem.setVisible(true);
            }


            if(menuItem.getItemId() == R.id.login){
                menuItem.setVisible(false);
            }
         }


        }


        if (temp.equals("parent")) {



            Menu menu1 = navigationView.getMenu();
            for (int menuItemIndex = 0; menuItemIndex < menu1.size(); menuItemIndex++) {
                MenuItem menuItem= menu1.getItem(menuItemIndex);
                if(menuItem.getItemId() == R.id.profile || menuItem.getItemId() == R.id.attendance
                || menuItem.getItemId() == R.id.fees || menuItem.getItemId() == R.id.training_centre
                || menuItem.getItemId() == R.id.suggestion|| menuItem.getItemId() == R.id.feedback
                || menuItem.getItemId() == R.id.logout || menuItem.getItemId() == R.id.trainer_profile
                        || menuItem.getItemId() == R.id.result ){
                    menuItem.setVisible(true);
                }


                if(menuItem.getItemId() == R.id.login){
                    menuItem.setVisible(false);
                }
            }


        }

        if (temp.equals("student")) {



            Menu menu2 = navigationView.getMenu();
            for (int menuItemIndex = 0; menuItemIndex < menu2.size(); menuItemIndex++) {
                MenuItem menuItem= menu2.getItem(menuItemIndex);
                if(menuItem.getItemId() == R.id.profile || menuItem.getItemId() == R.id.attendance
                        || menuItem.getItemId() == R.id.fees || menuItem.getItemId() == R.id.training_centre
                        || menuItem.getItemId() == R.id.suggestion|| menuItem.getItemId() == R.id.feedback
                        || menuItem.getItemId() == R.id.logout || menuItem.getItemId() == R.id.trainer_profile
                        || menuItem.getItemId() == R.id.result){
                    menuItem.setVisible(true);
                }


                if(menuItem.getItemId() == R.id.login){
                    menuItem.setVisible(false);
                }
            }
        }


    }

    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.login) {
            Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent1);
        } else if (id == R.id.training_centre) {
            Intent intent7 = new Intent(MainActivity.this, TrainingCenters.class);
            startActivity(intent7);
        } else if (id == R.id.up_event) {
            Intent intent8 = new Intent(MainActivity.this, UpcomingEvents.class);
            startActivity(intent8);
        } else if (id == R.id.suggestion) {
            Intent intent13 = new Intent(MainActivity.this, Suggestions.class);
            startActivity(intent13);
        } else if (id == R.id.about_us) {
            Intent intent10 = new Intent(MainActivity.this, Aboutus.class);
            startActivity(intent10);
        } else if (id == R.id.gallery) {
            Intent intent11 = new Intent(MainActivity.this, Gallery.class);
            startActivity(intent11);
        } else if (id == R.id.inbox) {
            Intent intent12 = new Intent(MainActivity.this, Inbox.class);
            startActivity(intent12);
        } else if (id == R.id.trainer_profile) {
            Intent intent12 = new Intent(MainActivity.this, TrainerProfile.class);
            startActivity(intent12);
        }else if (id == R.id.profile) {
            Intent intent1 = new Intent(MainActivity.this, Profile.class);
            startActivity(intent1);
        }else if (id == R.id.attendance_trainer) {
            Intent intent2 = new Intent(MainActivity.this, Attendance.class);
            startActivity(intent2);
        }else if (id == R.id.attendance) {
            Intent intent2 = new Intent(MainActivity.this, AttendanceParent.class);
            startActivity(intent2);
        } else if (id == R.id.stud_detail) {
            Intent intent3 = new Intent(MainActivity.this, Student_details.class);
            startActivity(intent3);
        }else if (id == R.id.result) {
            Intent intent3 = new Intent(MainActivity.this, Result.class);
            startActivity(intent3);
        }else if (id == R.id.notify) {
            Intent intent4 = new Intent(MainActivity.this, Notification.class);
            startActivity(intent4);
        } else if (id == R.id.attend_record) {
            Intent intent5 = new Intent(MainActivity.this, Attendance_record.class);
            startActivity(intent5);
        } else if (id == R.id.bank_detail) {
            Intent intent6 = new Intent(MainActivity.this, BankDetails.class);
            startActivity(intent6);
        } else if (id == R.id.training_centre) {
            Intent intent7 = new Intent(MainActivity.this, TrainingCenters.class);
            startActivity(intent7);
        }else if (id == R.id.fees) {
            Intent intent7 = new Intent(MainActivity.this, Fees1.class);
            startActivity(intent7);
        }else if (id == R.id.up_event) {
            Intent intent8 = new Intent(MainActivity.this, UpcomingEvents.class);
            startActivity(intent8);
        } else if (id == R.id.feedback) {
            Intent intent9 = new Intent(MainActivity.this, Feedback.class);
            startActivity(intent9);
        } else if (id == R.id.suggestion) {
            Intent intent13 = new Intent(MainActivity.this, Suggestions.class);
            startActivity(intent13);
        } else if (id == R.id.about_us) {
            Intent intent10 = new Intent(MainActivity.this, Aboutus.class);
            startActivity(intent10);
        } else if (id == R.id.gallery) {
            Intent intent11 = new Intent(MainActivity.this, Gallery.class);
            startActivity(intent11);
        } else if (id == R.id.inbox) {
            Intent intent12 = new Intent(MainActivity.this, Inbox.class);
            startActivity(intent12);

        }
        else if (id == R.id.ChangeLang) {
            Intent intent13 = new Intent(MainActivity.this, Lang.class);
            startActivity(intent13);

        }
        else if (id == R.id.logout) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Are you sure want to logout?");
                        builder.setCancelable(true);
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        });

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                log_out = getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
                                SharedPreferences.Editor e = log_out.edit();
                                e.remove("userrole");
                                e.commit();

                                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                                startActivity(intent);


                            }
                        });

          final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onShow(DialogInterface arg0) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.darkblue);
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.darkblue);

                }
            });

        alertDialog.show();
     }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
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

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Exit the app?");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                System.exit(0);

            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.darkblue);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.darkblue);

            }
        });
        alertDialog.show();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        result = getSharedPreferences("usersinfos", Context.MODE_PRIVATE);
        final String temp = result.getString("userrole", "");

        getMenuInflater().inflate(R.menu.main,menu);

        if (temp.equals("trainer")) {
            menu.setGroupVisible (R.id.menu_group, true);
        }
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_registration:
                Intent intent1 = new Intent(MainActivity.this, Registration.class);
                startActivity(intent1);
                return true;
            case R.id.menu_event:
                Intent intent2 = new Intent(MainActivity.this, Event.class);
                startActivity(intent2);
                return true;
            case R.id.menu_fees:
                Intent intent3 = new Intent(MainActivity.this, Fees.class);
                startActivity(intent3);
                return true;
            case R.id.refresh:
                finish();
                startActivity(getIntent());

                return true;
            case R.id.mode:
                Intent intent4 = new Intent(MainActivity.this, Mode.class);
                startActivity(intent4);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}




