package com.example.ukradlimirower;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean checkLogIn(String apiKey) {
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("api_key", apiKey));

        JSONObject res = HttpClientHelper.post(getUrl("/users/restore_session"), data);
        boolean result = false;

        try {
            result = res.getBoolean("success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean addBike(String apiKey, String title, String description, List<Bitmap> images) {
        String url = getUrl("/bikes");
        boolean result = false;
        String boundary = "-------------" + System.currentTimeMillis();

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

            for (Bitmap image : images) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                byte[] data = bos.toByteArray();
                byte[] encoded_data = Base64.encodeBase64(data);

                entityBuilder.addTextBody("images[]", new String(encoded_data));
            }

            entityBuilder.setBoundary(boundary);
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            entityBuilder.addTextBody("api_key", apiKey);
            entityBuilder.addTextBody("title", title);
            entityBuilder.addTextBody("description", description);

            HttpEntity entity = entityBuilder.build();

            JSONObject res = HttpClientHelper.post(url, entity, boundary);

            result = res.getBoolean("success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
