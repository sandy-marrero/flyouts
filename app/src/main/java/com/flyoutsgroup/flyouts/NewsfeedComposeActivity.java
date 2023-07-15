package com.flyoutsgroup.flyouts;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewsfeedComposeActivity extends AppCompatActivity {
    TextView etUsername;
    TextView etScreenName;
    EditText etDescription;
    Button postBtn;
    ImageView profIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed_compose);

        etUsername = findViewById(R.id.compose_newsfeed_userNameET);
        etScreenName = findViewById(R.id.compose_newsfeed_screenNameET);
        etDescription = findViewById(R.id.compose_newsfeed_etDescription);
        profIv = findViewById(R.id.profileIvNewsFeedCompose);
        postBtn = findViewById(R.id.compose_btnPost);
        etUsername.setText("@" +ParseUser.getCurrentUser().getUsername());
        etScreenName.setText(ParseUser.getCurrentUser().getString("screenname"));
        if (ParseUser.getCurrentUser().getParseFile("Profilepicture") != null) {
            Glide.with(this).load(ParseUser.getCurrentUser().getParseFile("Profilepicture").getUrl()).into(profIv);
        }else {
            Glide.with(this).load("https://i.pinimg.com/1200x/3f/94/70/3f9470b34a8e3f526dbdb022f9f19cf7.jpg").into(profIv);
        }
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post post = new Post();
                post.setCaption(etDescription.getText().toString());
                post.setUser(ParseUser.getCurrentUser());
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e("NewsFeedCompose", "Error while saving", e);
                            Toast.makeText(NewsfeedComposeActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                        }
                        Log.i("NewsFeedCompose", "Post saved");
                        etDescription.setText("");
                        finish();
                    }
                });
            }
        });
    }
}