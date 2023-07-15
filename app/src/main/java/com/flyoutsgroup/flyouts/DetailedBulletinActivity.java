package com.flyoutsgroup.flyouts;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailedBulletinActivity extends AppCompatActivity {
    ImageView banner;
    TextView title;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_bulletin);
        banner = findViewById(R.id.detailedImage);
        title = findViewById(R.id.detailedTitle);
        description = findViewById(R.id.detailedDescription);
        title.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("description"));
        String imageUrl = getIntent().getStringExtra("image");
        Glide.with(this).load(imageUrl).into(banner);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}