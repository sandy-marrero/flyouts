package com.flyoutsgroup.flyouts;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FullImageActivity extends AppCompatActivity {
    ImageView fullIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        fullIv = findViewById(R.id.fullImageIv);

        String imageUrl = getIntent().getStringExtra("image");
        Glide.with(this).load(imageUrl).into(fullIv);
    }
}