package com.example.vrushali.kungfu123;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {

    private Context context;
    public Integer[] images = {
            R.drawable.background_7,R.drawable.backdrawer,
            R.drawable.background_7,R.drawable.backdrawer,
            R.drawable.background_7,R.drawable.backdrawer,
            R.drawable.background_7,R.drawable.backdrawer,
            R.drawable.background_7,R.drawable.backdrawer,
            R.drawable.background_7,R.drawable.backdrawer,
            R.drawable.background_7,R.drawable.backdrawer,
            R.drawable.background_7,R.drawable.backdrawer

    };

    public GalleryAdapter(Context c){

        context=c;

    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(images[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        return imageView;
    }
}
