package com.example.vrushali.kungfu123;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GalleryPagerAdapter extends FragmentPagerAdapter{
    private Context mContext;

    public GalleryPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new Photos();
        } else {
            return new Videos();
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
                return mContext.getString(R.string.photos);
            case 1:
                return mContext.getString(R.string.videos);
            default:
                return null;
        }
    }
}

