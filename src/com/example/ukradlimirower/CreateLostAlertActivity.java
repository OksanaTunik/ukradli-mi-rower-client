package com.example.ukradlimirower;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by shybovycha on 22.11.14.
 */
public class CreateLostAlertActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_lost_alert);

        Button btnDone = (Button) findViewById(R.id.btnDone);

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
            return AlertsApiClient.createLostAlert(apiKey, title, description, lat, lon);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showLostAlertsList();
        }
    }
}