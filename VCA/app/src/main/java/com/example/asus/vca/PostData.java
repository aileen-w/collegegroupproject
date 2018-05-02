package com.example.asus.vca;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kamilp on 23/03/2018.
 */

public class PostData extends AsyncTask<String , Void ,String> {

    String server_response;
    String server_response_2;

    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("http://cit-project.hopto.org:15000/api.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream ());

            try {
                wr.writeBytes(strings[0]);
                Log.e("JSON Input", strings[0]);
                wr.flush();
                wr.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        Log.e("Response", "" + server_response);
        Log.e("ResponseFromServer", "" + server_response);
        if (server_response!=null){
            this.server_response_2 = server_response;
        }else{
            this.server_response_2 = null;
        }
//
//        JSONObject data = null;
//            try {
//
//                if(server_response!=null)
//                {
//                    String replaceString=server_response.replace('"','\"');//replaces all occurrences of 'a' to 'e'
//                    data = new JSONObject(replaceString);
//                    // get a JSONArray from inside an object
//                    JSONArray translations = data.getJSONArray("msg");
//                    Log.e("ResponseFromServer", translations.toString());
//                    Log.e("Size", "" + translations.length());
//                    // get the first JSONObject from the array
//
//                    // get the String contained in the object -> logs Bonjour tout le monde
//                    if(translations.length()>0)
//                    {
//                        for(int i=0; i<translations.length(); i++)
//                        {
//                            JSONObject text = translations.getJSONObject(i);
//                            Log.d("***>Text", text.getString("title"));
//                        }
//                    }
//                    else
//                    {
//                        String txt = "There is no records available for that date.";
//                    }
//                }
//
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

    }

//    @Override
//    public void onPostExecute(String s) {
//        super.onPostExecute(s);
//        Log.e("Response", "" + server_response);
//        Log.e("ResponseFromServer", "" + server_response);
//        return server_response;
//    }

    public String returnServerRespond(){
        return this.server_response_2;
    }

    public static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}


