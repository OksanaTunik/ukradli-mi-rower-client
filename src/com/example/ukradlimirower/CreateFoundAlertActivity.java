package com.example.ukradlimirower;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shybovycha on 23.11.14.
 */
public class CreateFoundAlertActivity extends BaseActivity {
    protected List<Bitmap> imagesToUpload;

    protected int getIntentId() {
        return 2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_found_alert);

        imagesToUpload = new ArrayList<Bitmap>();

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
                // EditText txtTitle = (EditText) findViewById(R.id.txtTitle);
                EditText txtDescription = (EditText) findViewById(R.id.txtDescription);

                String title = "Found"; // txtTitle.getText().toString();
                String description = txtDescription.getText().toString();

                double lat = getLat();
                double lon = getLon();

                Bundle bundle = getIntent().getExtras();
                String alertId = bundle.getString("alertId");

                setContentView(R.layout.waiting);

                new CreateFoundAlertTask(readApiKey(), alertId, title, description, lat, lon).execute();
            }
        });
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

        try {
            bmp = BitmapFactory.decodeStream(new java.net.URL(selectedImageUri.toString()).openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(this);
        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(new AbsListView.LayoutParams(200, 200));
        LinearLayout imageList = (LinearLayout) findViewById(R.id.images);
        imageView.setImageBitmap(bmp);
        imageList.addView(imageView, 1);

        imagesToUpload.add(bmp);
    }

    public class CreateFoundAlertTask extends AsyncTask<Void, Void, Boolean> {
        protected String apiKey;
        protected String title;
        protected String description;
        protected String alertId;
        protected Double lat;
        protected Double lon;

        public CreateFoundAlertTask(String apiKey, String alertId, String title, String description, Double lat, Double lon) {
            super();

            this.apiKey = apiKey;
            this.alertId = alertId;
            this.title = title;
            this.description = description;
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return AlertsApiClient.createFoundAlert(apiKey, alertId, title, description, lat, lon, imagesToUpload);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showLostAlert(alertId);
        }
    }
}