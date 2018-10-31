package com.example.vrushali.kungfu123;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AboutPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public AboutPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AboutFragment();
        } else if(position == 1){
            return new about_profile();
        }else{
            return new Contact_detail();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return mContext.getString(R.string.about_us);
            case 1:
                return mContext.getString(R.string.profile);
            case 2:
                return mContext.getString(R.string.contact_detail);
            default:
                return null;
        }
    }

}

