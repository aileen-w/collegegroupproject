package com.example.asus.vca;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class GPSTracker extends Service implements LocationListener {

    String model = Build.MANUFACTURER + " " + Build.MODEL;
    String manufacturer = Build.MANUFACTURER;
    String device = "";
    String androidId;

    @Override
    public void onLocationChanged(Location location) {

//        Log.i("geo", "Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        try {

            device = Build.MODEL;
            device = (manufacturer) + "-" + device;
            device = device.toUpperCase();
//            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            device = device + "-" + androidId;
            device = device.replace(' ', '_');//replaces all occurrences of 'a' to 'e'

            JSONObject subObj = new JSONObject();
            subObj.put("latitude", location.getLatitude());
            subObj.put("longitude", location.getLongitude());

            JSONObject obj = new JSONObject();
            obj.put("svc", "geolocation");
            obj.put("dev", device);
            obj.put("msg", subObj.toString());
            new PostData().execute(obj.toString());

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setAndroidId(String id) {
        this.androidId = id;
    }
}