package com.example.vrushali.kungfu123;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RegisterFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public RegisterFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new Student_registration();
        } else if (position == 1){
            return new Parent_registration();
        } else {
            return new Existing();
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
                return mContext.getString(R.string.Stud_registration);
            case 1:
                return mContext.getString(R.string.parent_registration);
            case 2:
                return mContext.getString(R.string.Existing);
            default:
                return null;
        }
    }

}
