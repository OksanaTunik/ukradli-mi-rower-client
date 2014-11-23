package com.example.ukradlimirower;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;

public abstract class BaseActivity extends Activity {
    protected Uri outputFileUri;

    protected int getIntentId() {
        return 1;
    }

    protected void openImageIntent() {
        try {
            // Determine Uri of camera image to save.
            final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "ukradli-mi-rower-data" + File.separator);
            root.mkdirs();
            //final String fname = "image.jpg";
            final File sdImageMainDirectory = File.createTempFile("camimg", "jpg", root);
            outputFileUri = Uri.fromFile(sdImageMainDirectory);

            // Camera.
            final List<Intent> cameraIntents = new ArrayList<Intent>();
            final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            final PackageManager packageManager = getPackageManager();
            final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
            for (ResolveInfo res : listCam) {
                final String packageName = res.activityInfo.packageName;
                final Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(packageName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                cameraIntents.add(intent);
            }

            // Filesystem.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

            // Chooser of filesystem options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

            // Add the camera options.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

            startActivityForResult(chooserIntent, getIntentId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showNewLostAlert() {
        Intent intent = getIntent(CreateLostAlertActivity.class, true);
        startActivity(intent);
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
        Intent intent = getIntent(ShowLostActivity.class, true);
        intent.putExtra("alertId", alertId);
        startActivity(intent);
    }

    public void showCreateFoundAlert(String alertId) {
        Intent intent = getIntent(CreateFoundAlertActivity.class, true);
        intent.putExtra("alertId", alertId);
        startActivity(intent);
    }

    public double getLat() {
        if (getLocation() == null) {
            return 0.0;
        } else {
            return getLocation().getLatitude();
        }
    }

    public double getLon() {
        if (getLocation() == null) {
            return 0.0;
        } else {
            return getLocation().getLongitude();
        }
    }

    /*protected Location getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return location;
    }*/

    private Location getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = lm.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }

            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                //ALog.d("found best last known location: %s", l);
                bestLocation = l;
            }
        }

        if (bestLocation == null) {
            return null;
        }

        return bestLocation;
    }

    public Bitmap getBitmapFromUrl(String url) {
        Bitmap res = null;

        try {
            URL ulrn = new URL(BaseApiClient.publicUrl + url);
            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
            InputStream is = con.getInputStream();
            res = BitmapFactory.decodeStream(is);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return res;
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

    protected File getApiKeyFile() {
        File root = new File(Environment.getExternalStorageDirectory() + File.separator + "ukradli-mi-rower-data" + File.separator);
        root.mkdirs();
        String fname = "APIKEY";

        return new File(root, fname);
    }

    protected void storeApiKey(String apiKey) {
        File file = getApiKeyFile();
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(file);
            fos.write(apiKey.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String readApiKey() {
        String result = null;
        File file = getApiKeyFile();

        try {
            FileInputStream fis = new FileInputStream(file);
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
