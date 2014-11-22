package com.example.ukradlimirower;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shybovycha on 23.11.14.
 */
public class AlertsApiClient extends BaseApiClient {
    public static boolean createLostAlert(String apiKey, String title, String description, Float lat, Float lon) {
        String lat_s = lat.toString();
        String lon_s = lon.toString();

        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("api_key", apiKey));
        data.add(new BasicNameValuePair("title", title));
        data.add(new BasicNameValuePair("description", description));
        data.add(new BasicNameValuePair("lat", lat_s));
        data.add(new BasicNameValuePair("lon", lon_s));

        String url = getUrl("/alerts/lost");

        JSONObject res = HttpClientHelper.post(url, data);
        boolean result = false;

        try {
            result = res.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean createFoundAlert(String apiKey, String lostAlertId, String title, String description, Float lat, Float lon) {
        String lat_s = lat.toString();
        String lon_s = lon.toString();

        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("api_key", apiKey));
        data.add(new BasicNameValuePair("lost_alert_id", lostAlertId));
        data.add(new BasicNameValuePair("title", title));
        data.add(new BasicNameValuePair("description", description));
        data.add(new BasicNameValuePair("lat", lat_s));
        data.add(new BasicNameValuePair("lon", lon_s));

        String url = getUrl("/alerts/lost");

        JSONObject res = HttpClientHelper.post(url, data);
        boolean result = false;

        try {
            result = res.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static LostAlert getLostAlert(String alertId) {
        String url = getUrl(String.format("/alerts/lost?id={0}", alertId));

        JSONObject res = HttpClientHelper.get(url);
        LostAlert result = null;

        try {
            JSONObject alert = res.getJSONObject("alert");

            result = new LostAlert(
                    alert.getInt("id"),
                    alert.getString("title"),
                    alert.getString("description"),
                    alert.getString("author"),
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
                        childAlert.getString("author"),
                        childAlert.getDouble("lat"),
                        childAlert.getDouble("lon")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
