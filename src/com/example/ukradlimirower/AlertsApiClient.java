package com.example.ukradlimirower;

import android.graphics.Bitmap;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shybovycha on 23.11.14.
 */
public class AlertsApiClient extends BaseApiClient {
    public static boolean createLostAlert(String apiKey, String title, String description, Double lat, Double lon, List<File> images) {
        String lat_s = lat.toString();
        String lon_s = lon.toString();
        String url = getUrl("/alerts/lost");
        boolean result = false;
        String boundary = "-------------" + System.currentTimeMillis();

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

            for (File image : images) {
                /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                byte[] data = bos.toByteArray();
                ByteArrayBody bab = new ByteArrayBody(data, "pic.jpeg");*/

                entityBuilder.addPart("images[]", new FileBody(image, "image/jpeg"));
            }

            entityBuilder.setBoundary(boundary);
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            entityBuilder.addTextBody("api_key", apiKey);
            entityBuilder.addTextBody("title", title);
            entityBuilder.addTextBody("description", description);
            entityBuilder.addTextBody("lat", lat_s);
            entityBuilder.addTextBody("lon", lon_s);

            HttpEntity entity = entityBuilder.build();

            JSONObject res = HttpClientHelper.post(url, entity, boundary);

            result = res.getBoolean("success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean createFoundAlert(String apiKey, String lostAlertId, String title, String description, Double lat, Double lon) {
        String lat_s = lat.toString();
        String lon_s = lon.toString();

        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("api_key", apiKey));
        data.add(new BasicNameValuePair("lost_alert_id", lostAlertId));
        data.add(new BasicNameValuePair("title", title));
        data.add(new BasicNameValuePair("description", description));
        data.add(new BasicNameValuePair("lat", lat_s));
        data.add(new BasicNameValuePair("lon", lon_s));

        String url = getUrl("/alerts/found");

        JSONObject res = HttpClientHelper.post(url, data);
        boolean result = false;

        try {
            result = res.getBoolean("success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<LostAlert> getAllLostAlerts() {
        String url = getUrl(String.format("/alerts/lost"));

        JSONObject res = HttpClientHelper.get(url);
        List<LostAlert> result = new ArrayList<LostAlert>();

        try {
            JSONArray lostAlerts = res.getJSONArray("alerts");

            for (int i = 0; i < lostAlerts.length(); i++) {
                JSONObject alert = (JSONObject) lostAlerts.get(i);

                LostAlert resultListItem = new LostAlert(
                        alert.getInt("id"),
                        alert.getString("title"),
                        alert.getString("description"),
                        alert.getJSONObject("author").getString("name"),
                        alert.getDouble("lat"),
                        alert.getDouble("lon")
                );

                result.add(resultListItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static LostAlert getLostAlert(String alertId) {
        String url = getUrl(String.format("/alerts/lost/%s", alertId));

        JSONObject res = HttpClientHelper.get(url);
        LostAlert result = null;

        try {
            JSONObject alert = res.getJSONObject("alert");

            result = new LostAlert(
                    alert.getInt("id"),
                    alert.getString("title"),
                    alert.getString("description"),
                    alert.getJSONObject("author").getString("name"),
                    alert.getDouble("lat"),
                    alert.getDouble("lon")
            );

            JSONArray foundAlerts = alert.getJSONArray("found_alerts");

            for (int i = 0; i < foundAlerts.length(); i++) {
                JSONObject childAlert = (JSONObject) foundAlerts.get(i);
                result.foundAlerts.add(new FoundAlert(
                        childAlert.getInt("id"),
                        childAlert.getString("title"),
                        childAlert.getString("description"),
                        childAlert.getJSONObject("author").getString("name"),
                        childAlert.getDouble("lat"),
                        childAlert.getDouble("lon")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
