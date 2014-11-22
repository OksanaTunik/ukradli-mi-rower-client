package com.example.ukradlimirower;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public abstract class BaseActivity extends Activity {
    public void showNewLostAlert() {
        Intent intent = getIntent(LostAlertActivity.class);
    }

    public void showLogin() {
        Intent intent = getIntent(LoginActivity.class);
        startActivity(intent);
    }

    public void showLostAlertsList() {
        Intent intent = getIntent(LostAlertsListActivity.class);
        startActivity(intent);
    }

    public void showSignUp() {
        Intent intent = getIntent(SignUpActivity.class, true);
        startActivity(intent);
    }

    public void showNewBicycle() {
        Intent intent = getIntent(NewBicycleActivity.class);
        startActivity(intent);
    }

    public void showLostAlert(String alertId) {
        Intent intent = getIntent(ShowLostActivity.class);
        intent.putExtra("alertId", alertId);
        startActivity(intent);
    }

    protected Intent getIntent(Class goTo) {
        return getIntent(goTo, false);
    }

    protected Intent getIntent(Class goTo, boolean allowGoBack) {
        Intent intent = new Intent(this, goTo);

        if (!allowGoBack) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        return intent;
    }

    protected void storeApiKey(String apiKey) {
        String FILENAME = "APIKEY";

        // File f = new File(FILENAME);

        FileOutputStream fos;

        try {
            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(apiKey.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String ReadApiKey() {
        String FILENAME = "APIKEY";
        String result = null;

        // File f = new File(FILENAME);

        FileInputStream fis;

        try {
            fis = openFileInput(FILENAME);
            Scanner reader = new Scanner(fis);
            result = reader.next();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
