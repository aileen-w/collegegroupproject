package com.example.asus.vca;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherTask extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... urls) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream stream = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }
            System.out.println("test " + buffer.toString());


            JSONObject jsonObject = new JSONObject(buffer.toString());
            JSONArray weather = jsonObject.getJSONArray("weather");
            String weatherString = weather.toString();
            String[] weatherSplit = weatherString.split(",");
            String desciption = weatherSplit[2];
            String[] descriptionSplit = desciption.split(":");
            return descriptionSplit[1];

        } catch (Exception e) {
            System.out.println(e);


        } finally {
            // regardless of success or failure, we will disconnect from the URLConnection.
            urlConnection.disconnect();
        }
        return "default";
    }


    protected void onPostExecute(String text) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}

