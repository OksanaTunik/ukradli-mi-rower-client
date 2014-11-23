package com.example.ukradlimirower;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by shybovycha on 23.11.14.
 */
public class CreateFoundAlertActivity extends BaseActivity {
    protected static int INTENT_ID = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_found_alert);

        Button btnDone = (Button) findViewById(R.id.btnDone);
        Button btnAddImage = (Button) findViewById(R.id.btnAddImage);

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, INTENT_ID);
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

                Bundle bundle = getIntent().getExtras();
                String alertId = bundle.getString("alertId");

                setContentView(R.layout.waiting);

                new CreateFoundAlertTask(readApiKey(), alertId, title, description, lat, lon);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultcode, Intent intent)
    {
        super.onActivityResult(requestCode, resultcode, intent);

        if (requestCode == INTENT_ID) {
            if (intent != null) {
                Log.d("IMAGE_PICKER", "idButSelPic Photopicker: " + intent.getDataString());
                Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
                cursor.moveToFirst();  //if not doing this, 01-22 19:17:04.564: ERROR/AndroidRuntime(26264): Caused by: android.database.CursorIndexOutOfBoundsException: Index -1 requested, with a size of 1

                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String fileSrc = cursor.getString(idx);
                Log.d("IMAGE_PICKER", "Picture:" + fileSrc);
                //m_Tv.setText("Image selected:"+fileSrc);

                Bitmap bitmapPreview = BitmapFactory.decodeFile(fileSrc); //load preview image
                //BitmapDrawable bmpDrawable = new BitmapDrawable(bitmapPreview);
                //m_Image.setBackgroundDrawable(bmpDrawable);
            }
            else {
                Log.d("IMAGE_PICKER", "idButSelPic Photopicker canceled");
                //m_Tv.setText("Image selection canceled!");
            }
        }
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
            return AlertsApiClient.createFoundAlert(apiKey, alertId, title, description, lat, lon);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showLostAlert(alertId);
        }
    }
}