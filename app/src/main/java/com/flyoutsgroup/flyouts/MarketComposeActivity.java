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

public class MarketComposeActivity extends AppCompatActivity {


    public static final String TAG = "Market Compose Activity";
    public final static int PICK_PHOTO_CODE = 44;
    public String photoFileName = "photo.png";

    Button btnUpload;
    Button btnPost;
    EditText etItemName;
    EditText etItemDescription;
    EditText etPrice;
    EditText etContactInfo;
    EditText etCondition;
    ImageView ivItemPicturePreview;
    ParseFile photoFile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_compose);
        btnUpload = findViewById(R.id.btnUploadFlyerImage);
        btnPost = findViewById(R.id.btnPost);
        etItemName = findViewById(R.id.etItemName);
        etItemDescription = findViewById(R.id.etItemDescription);
        etPrice = findViewById(R.id.etPrice);
        etContactInfo = findViewById(R.id.etContactInfo);
        etCondition = findViewById(R.id.etCondition);
        ivItemPicturePreview = findViewById(R.id.ivItemPreview);
        Glide.with(this).load("https://png.pngtree.com/png-vector/20190820/ourmid/pngtree-no-image-vector-illustration-isolated-png-image_1694547.jpg").into(ivItemPicturePreview);


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etItemName.getText().toString();
                if(title.isEmpty()){
                    Toast.makeText(MarketComposeActivity.this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String description = etItemDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(MarketComposeActivity.this, "Description cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }


                double price = Double.parseDouble(etPrice.getText().toString());
                if (description.isEmpty()){
                    Toast.makeText(MarketComposeActivity.this, "Price cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String contactInfo = etContactInfo.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(MarketComposeActivity.this, "Contact info cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String condition = etCondition.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(MarketComposeActivity.this, "Item condition cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }





                //photoFile = ivFlyerPreview.getDrawable();
                if(photoFile == null){
                    Toast.makeText(MarketComposeActivity.this, "There is no image", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveItem(title,description,price,contactInfo,condition,currentUser, photoFile);
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
                        Toast.makeText(MarketComposeActivity.this, "Error saving: \" + e.getMessage()", Toast.LENGTH_LONG).show();
                    } else {
                        //filed saved successfully :)
                    }
                }
            });

            // Load the selected image into a preview
            ImageView ivItemPreview = (ImageView)findViewById(R.id.ivItemPreview);
            ivItemPreview.setImageBitmap(selectedImage);
        }
    }

    private void saveItem(String title, String description, double price, String contactInfo, String condition, ParseUser currentUser, ParseFile photoFile) {
        Item item = new Item();
        item.setItemName(title);
        item.setItemDescription(description);
        item.setPrice(price);
        item.setCondition(condition);
        item.setContact(contactInfo);
        item.setUser(currentUser);
        item.setImage(photoFile);
        item.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error saving item", e);
                    Toast.makeText(MarketComposeActivity.this, "Error saving item", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Item saved");

            }
        });
        finish();
    }


    }
