package com.example.vrushali.kungfu123;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        GalleryAdapter adapter = new GalleryAdapter(this);

        ImageView imageView = (ImageView)findViewById(R.id.imageView3);
        imageView.setImageResource(adapter.images[position]);

    }
}
