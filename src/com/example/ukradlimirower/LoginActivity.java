package com.example.ukradlimirower;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
    EditText txtUserName;
    EditText txtPassword;
    Button btnLogin;
    Button btnSignUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtUserName = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPwd);

        btnLogin = (Button) this.findViewById(R.id.btnLogin);
        btnSignUp = (Button) this.findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtUserName = (EditText) findViewById(R.id.txtEmail);
                EditText txtPassword = (EditText) findViewById(R.id.txtPwd);

                if (txtUserName.getText().length() == 0) {
                    txtUserName.setError("please enter the email");

                } else if (!isEmailValid(txtUserName.getText())) {
                    txtUserName.setError("email is not Valid");
                }

                if (txtPassword.getText().length() == 0) {
                    txtPassword.setError("please enter the password");

                }

                if (txtUserName.getError() == null && txtPassword.getError() == null) {
                    String username = txtUserName.getText().toString();
                    String password = txtPassword.getText().toString();

                    new LoginTask().execute(username, password);
                }
            }
        });

        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showSignUp();
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return AccountApiClient.logIn(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                storeApiKey(result);
                showLostAlertsList();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
                showLogin();
            }
        }
    }
}
