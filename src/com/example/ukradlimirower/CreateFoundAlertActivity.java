package com.example.ukradlimirower;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by shybovycha on 23.11.14.
 */
public class CreateFoundAlertActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_found_alert);

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

                Bundle bundle = getIntent().getExtras();
                String alertId = bundle.getString("alertId");

                setContentView(R.layout.waiting);

                new CreateFoundAlertTask(readApiKey(), alertId, title, description, lat, lon);
            }
        });
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