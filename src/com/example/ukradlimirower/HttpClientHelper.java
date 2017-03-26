package com.example.ukradlimirower;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.JsonReader;

public class HttpClientHelper {
    public static JSONObject post(String url, HttpEntity entity, String boundary) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        httppost.setHeader("Content-type", "multipart/form-data, boundary=" + boundary);
        httppost.setEntity(entity);

        JSONObject jObject = null;

        try {
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder body = new StringBuilder();
            String l = reader.readLine();

            while (l != null) {
                body.append(l);
                l = reader.readLine();
            }

            jObject = new JSONObject(body.toString());
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObject;
    }

    public static JSONObject post(String url, List<NameValuePair> data) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        JSONObject jObject = null;

        try {
            httppost.setEntity(new UrlEncodedFormEntity(data));
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder body = new StringBuilder();
            String l = reader.readLine();

            while (l != null) {
                body.append(l);
                l = reader.readLine();
            }

            jObject = new JSONObject(body.toString());
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObject;
    }

    public static JSONObject get(String url) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        JSONObject jObject = null;

        try {
            HttpResponse response = httpclient.execute(httpget);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder body = new StringBuilder();
            String l = reader.readLine();

            while (l != null) {
                body.append(l);
                l = reader.readLine();
            }

            jObject = new JSONObject(body.toString());
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObject;
    }
}
