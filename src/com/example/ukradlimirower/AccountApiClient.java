package com.example.ukradlimirower;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;


public class AccountApiClient extends BaseApiClient {
    public static String signUp(String email, String password, String displayName) {
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("name", displayName));
        data.add(new BasicNameValuePair("email", email));
        data.add(new BasicNameValuePair("password", password));
        data.add(new BasicNameValuePair("password_confirmation", password));

        String url = getUrl("/users/sign_up");

        JSONObject res = HttpClientHelper.post(url, data);
        String result = null;

        try {
            if (res.getBoolean("success")) {
                result = res.getString("api_key");
            } else {
                result = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String logIn(String email, String password) {
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("email", email));
        data.add(new BasicNameValuePair("password", password));

        String url = getUrl("/users/sign_in");

        JSONObject res = HttpClientHelper.post(url, data);
        String result = null;

        try {
            if (res.getBoolean("success")) {
                result = res.getString("api_key");
            } else {
                result = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean checkLogIn(String apiKey) {
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("api_key", apiKey));

        JSONObject res = HttpClientHelper.post(getUrl("/account/restore_session"), data);
        boolean result = false;

        try {
            result = res.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean addBike(String apiKey, String title, String description) {
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("api_key", apiKey));
        data.add(new BasicNameValuePair("title", title));
        data.add(new BasicNameValuePair("description", description));

        String url = getUrl("/bikes");

        JSONObject res = HttpClientHelper.post(url, data);
        boolean result = false;

        try {
            result = res.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
