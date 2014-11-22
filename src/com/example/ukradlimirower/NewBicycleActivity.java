package com.example.ukradlimirower;

import com.example.ukradlimirower.LoginActivity.LoginTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewBicycleActivity  extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_bicycle);
        
        Button btnDone = (Button) findViewById(R.id.btnDone);
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
                
                if (txtBicycle.getError() == null
                        && txtText.getError() == null) {

                    String title = txtBicycle.getText().toString();
                    String description = txtText.getText().toString();
                    
                    new CreateBikeTask().execute(title, description);
                }
            }
        });
    }
    
    public class CreateBikeTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String apiKey = ReadApiKey();
            return AccountApiClient.addBike(apiKey, params[0], params[1]);
        }
        
        @Override
        protected void onPostExecute(Boolean result) {
            if (result == true) {
                showHome();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show(); 
                showSignUp();
            }
        }
    }
}
