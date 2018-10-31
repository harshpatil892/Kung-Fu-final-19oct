package com.example.vrushali.kungfu123;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NotifyPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public NotifyPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new Batch_notify();
        } else {
            return new Individual_notify();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return mContext.getString(R.string.batch);
            case 1:
                return mContext.getString(R.string.individual);
            default:
                return null;
        }
    }

}

