package com.flyoutsgroup.flyouts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BulletinComposeActivity extends AppCompatActivity {
    public static final String TAG = "Bulletin Compose Activity";
    public final static int PICK_PHOTO_CODE = 55;
    public String photoFileName = "photo.png";

    Button btnUpload;
    Button btnPost;
    EditText etTitle;
    EditText etDescription;
    ImageView ivFlyerPreview;
    ParseFile photoFile;
    //EditText etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_compose);

        btnUpload = findViewById(R.id.btnUploadItemImageFlyer);
        btnPost = findViewById(R.id.btnPostItemFlyer);
        etTitle = findViewById(R.id.etItemNameFlyer);
        etDescription = findViewById(R.id.etDescriptionFlyer);
        ivFlyerPreview = findViewById(R.id.ivItemPicturePreviewFlyer);
        Glide.with(this).load("https://png.pngtree.com/png-vector/20190820/ourmid/pngtree-no-image-vector-illustration-isolated-png-image_1694547.jpg").into(ivFlyerPreview);
        //etDate = findViewById(R.id.etDate);


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(BulletinComposeActivity.this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(BulletinComposeActivity.this, "Description cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //photoFile = ivFlyerPreview.getDrawable();
                if (photoFile == null) {
                    Toast.makeText(BulletinComposeActivity.this, "There is no image", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveFlyer(title, description, currentUser, photoFile);
            }
        });


    }

    private void pickFromGallery() {
        // Create intent for picking a photo from the gallery

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // photoFile = getPhotoFileUri(photoFileName);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadFromUri(photoUri);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] scaledData = bos.toByteArray();
            photoFile = new ParseFile("image_to_be_saved.jpg", scaledData);

            photoFile.saveInBackground(new SaveCallback() {

                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(BulletinComposeActivity.this, "Error saving: \" + e.getMessage()", Toast.LENGTH_LONG).show();
                    } else {
                        //filed saved successfully :)
                    }
                }
            });

            // Load the selected image into a preview
            ImageView ivPreview = (ImageView) findViewById(R.id.ivItemPicturePreviewFlyer);
            ivPreview.setImageBitmap(selectedImage);
        }
    }

    private void saveFlyer(String title, String description, ParseUser currentUser, ParseFile photoFile) {
        Flyer flyer = new Flyer();
        flyer.setTitle(title);
        flyer.setDescription(description);
        flyer.setUser(currentUser);
        flyer.setImage(photoFile);
        flyer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error saving flyer", e);
                    Toast.makeText(BulletinComposeActivity.this, "Error saving flyer", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Flyer saved");
                etTitle.setText("");
                etDescription.setText("");
            }
        });
        finish();
    }
}