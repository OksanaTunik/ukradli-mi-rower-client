package com.example.ukradlimirower;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewBicycleActivity  extends BaseActivity {
    protected static int INTENT_ID = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_bicycle);

        Button btnAddImg = (Button) findViewById(R.id.btnAddImage);
        Button btnDone = (Button) findViewById(R.id.btnDone);

        btnAddImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, INTENT_ID);
            }
        });

        btnDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtBicycle = (EditText) findViewById(R.id.txtText);
                EditText txtText = (EditText) findViewById(R.id.txtText);

                if (txtBicycle.getText().length() == 0) {
                    txtBicycle.setError("please enter bike name");
                }

                if (txtText.getText().length() == 0) {
                    txtText.setError("please enter bike description");

                }

                if (txtBicycle.getError() == null && txtText.getError() == null) {

                    String title = txtBicycle.getText().toString();
                    String description = txtText.getText().toString();

                    setContentView(R.layout.waiting);

                    new CreateBikeTask().execute(title, description);
                }
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

    public class CreateBikeTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String apiKey = readApiKey();
            return AccountApiClient.addBike(apiKey, params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                showLostAlertsList();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                showSignUp();
            }
        }
    }
}
