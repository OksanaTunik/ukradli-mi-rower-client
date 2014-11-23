package com.example.ukradlimirower;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shybovycha on 22.11.14.
 */
public class CreateLostAlertActivity extends BaseActivity {
    protected List<File> imagesToUpload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagesToUpload = new ArrayList<File>();

        setContentView(R.layout.new_lost_alert);

        Button btnDone = (Button) findViewById(R.id.btnDone);
        Button btnAddImage = (Button) findViewById(R.id.btnAddImage);

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageIntent();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtTitle = (EditText) findViewById(R.id.txtTitle);
                EditText txtDescription = (EditText) findViewById(R.id.txtDescription);

                String title = txtTitle.getText().toString();
                String description = txtDescription.getText().toString();

                double lat = getLat();
                double lon = getLon();

                setContentView(R.layout.waiting);

                new CreateLostAlertTask(readApiKey(), title, description, lat, lon).execute();
            }
        });
    }

    @Override
    protected int getIntentId() {
        return 2;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK || requestCode != getIntentId()) {
            return;
        }

        final boolean isCamera;

        if (data == null) {
            isCamera = true;
        } else {
            final String action = data.getAction();

            if (action == null) {
                isCamera = false;
            } else {
                isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            }
        }

        Uri selectedImageUri;

        if (isCamera) {
            selectedImageUri = outputFileUri;
        } else {
            selectedImageUri = (data == null) ? null : data.getData();
        }

        Bitmap bmp = null;
        File fbmp = null;

        try {
            bmp = BitmapFactory.decodeStream(new java.net.URL(selectedImageUri.toString()).openStream());
            fbmp = new File(new URI(selectedImageUri.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(this);
        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(new AbsListView.LayoutParams(200, 200));
        LinearLayout imageList = (LinearLayout) findViewById(R.id.images);
        imageView.setImageBitmap(bmp);
        imageList.addView(imageView, 1);

        imagesToUpload.add(fbmp);
    }

    public class CreateLostAlertTask extends AsyncTask<Void, Void, Boolean> {
        protected String apiKey;
        protected String title;
        protected String description;
        protected Double lat;
        protected Double lon;

        public CreateLostAlertTask(String apiKey, String title, String description, Double lat, Double lon) {
            super();

            this.apiKey = apiKey;
            this.title = title;
            this.description = description;
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return AlertsApiClient.createLostAlert(apiKey, title, description, lat, lon, imagesToUpload);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showLostAlertsList();
        }
    }
}