package com.example.asus.vca;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.integreight.onesheeld.sdk.OneSheeldConnectionCallback;
import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldScanningCallback;
import com.integreight.onesheeld.sdk.OneSheeldSdk;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    public Button Speaker;
    public Button Home;
    public Button Services;
    public Button Miscellaneous;
    public Button Mic;
    private ArrayList<String> connectedDevicesNames;
    private ArrayList<String> scannedDevicesNames;
    private ArrayList<OneSheeldDevice> oneSheeldScannedDevices;
    private ArrayList<OneSheeldDevice> oneSheeldConnectedDevices;
    private ArrayAdapter<String> connectedDevicesArrayAdapter;
    private ArrayAdapter<String> scannedDevicesArrayAdapter;
    private Handler uiThreadHandler = new Handler();

    String model = Build.MODEL;
    String device = Build.MODEL;
    LocationManager locationManager;
    String provider;
    Activity parent = this;
    private byte voiceShieldId = OneSheeldSdk.getKnownShields().VOICE_RECOGNIZER_SHIELD.getId();
    private static final byte SEND_RESULT = 0x01;
    private OneSheeldManager manager;

    String text;
    String et;
    TextToSpeech tts;

    protected static final int RESULT_SPEECH = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {                    //loads main activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();                    //set up user interface
        setupOneSheeld();               //connect to onesheeld board

        appIsUp();                  // send notification to website that app is running
        startGeolocation();         //start geolocation tracking
        textToSpeech();             // greet user at the start of app


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    private void setupOneSheeld() {
        connectedDevicesNames = new ArrayList<>();
        scannedDevicesNames = new ArrayList<>();
        oneSheeldScannedDevices = new ArrayList<>();
        oneSheeldConnectedDevices = new ArrayList<>();
        connectedDevicesArrayAdapter = new ArrayAdapter<>(this, 0);
        scannedDevicesArrayAdapter = new ArrayAdapter<>(this, 0);

        //Init the SDK with context
        OneSheeldSdk.init(this);
        //Optional, enable debugging messages.
        OneSheeldSdk.setDebugging(true);
        // Get the manager instance
        manager = OneSheeldSdk.getManager();
        // Set the connection failing retry count to 1
        manager.setConnectionRetryCount(1);
        // Set the automatic connecting retries to true, this will use 3 different methods for connecting
        manager.setAutomaticConnectingRetriesForClassicConnections(true);


            //Construct a new OneSheeldScanningCallback callback and override onDeviceFind method
            //finds nearby device and connects to it
            OneSheeldScanningCallback scanningCallback = new OneSheeldScanningCallback() {

                    @Override
                    public void onDeviceFind ( final OneSheeldDevice device){
                    uiThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            oneSheeldScannedDevices.add(device);
                            scannedDevicesNames.add(device.getName());
                            scannedDevicesArrayAdapter.notifyDataSetChanged();
                            manager.cancelScanning();
                            device.connect();
                        }

                    });
                }
            };



        // Construct a new OneSheeldConnectionCallback callback and override onConnect method
        //removes device from scanned devices array and adds it to connected devices array
        //updates both scanned devices and connected devices array
        OneSheeldConnectionCallback connectionCallback = new OneSheeldConnectionCallback() {
            @Override
            public void onConnect(final OneSheeldDevice device) {
                oneSheeldScannedDevices.remove(device);
                oneSheeldConnectedDevices.add(device);
                final String deviceName = device.getName();
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (scannedDevicesNames.indexOf(deviceName) >= 0) {
                            scannedDevicesNames.remove(deviceName);
                            connectedDevicesNames.add(deviceName);
                            connectedDevicesArrayAdapter.notifyDataSetChanged();
                            scannedDevicesArrayAdapter.notifyDataSetChanged();
                        }
                    }

                });
            }
        };


        // Add the connection and scanning callbacks
        manager.addConnectionCallback(connectionCallback);
        manager.addScanningCallback(scanningCallback);

        // Initiate the Bluetooth scanning
        manager.scan();

    }


    private void setupUI() {

        //find id of speaker button
        Speaker = findViewById(R.id.buttonSpeaker);
        {
            //set listener on speaker button
            Speaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create audio activity intent in context to main activity
                    Intent intentLoadAudioActivity = new Intent(MainActivity.this, AudioActivity.class);
                    //run audio activity intent
                    startActivity(intentLoadAudioActivity);
                }
            });

        }

        //find id of home button
        Home = findViewById(R.id.buttonHome);
        {
            //set listener on home buton
            Home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create home activity intent in context to main activity
                    Intent intentLoadHomeActivity = new Intent(MainActivity.this, HomeActivity.class);
                    //run home activity intent
                    startActivity(intentLoadHomeActivity);
                }
            });

        }

        //find id of services button
        Services = findViewById(R.id.buttonServices);
        {
            //set listener on services button
            Services.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create services activity intent in context to main activity
                    Intent intentLoadServicesActivity = new Intent(MainActivity.this, ServicesActivity.class);
                    //run services activity intent
                    startActivity(intentLoadServicesActivity);
                }
            });

        }

        // find id of miscellaneous button
        Miscellaneous = findViewById(R.id.buttonMisc);
        {
            //set listener on miscellaneous button
            Miscellaneous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create miscellaneous activity intent in context to main activity
                    Intent intentLoadMiscActivity = new Intent(MainActivity.this, MiscellaneousActivity.class);
                    //run services activity intent
                    startActivity(intentLoadMiscActivity);
                }
            });

        }



        Mic = findViewById(R.id.buttonMic);
        {
            //set listener on mic button
            Mic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentLoadMicActivity = new Intent(MainActivity.this, MicActivity.class);
                    startActivity(intentLoadMicActivity);
                }
            });
        }
    }

    /**
     * Helper method to send data to server.
     * Currently supports notifications and errors.
     * To send data, just create JSON object like below and input required attributes
     * - svc: service (error, notification)
     * - dev: device (1sheeld or something else)
     * - msg: message (whatever message you want to send)
     */

   public void appIsUp() {
        try {

            String manufacturer = Build.MANUFACTURER;
            device = Build.MODEL;
            device = (manufacturer) + "-" + device;
            device = device.toUpperCase();
            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            device = device + "-"+android_id;

            JSONObject obj = new JSONObject();
            obj.put("svc" , "notification");
            obj.put("dev" , device);
            obj.put("msg" , "Android app is up and running");
            PostData postData = new PostData();
            postData.execute(obj.toString());
//            postData.onPostExecute(obj.toString());
//            Log.e("ResponseMainActivity", "" + postData.server_response);

        } catch (JSONException e) {
            e.printStackTrace();
            e.getMessage();
        }

    }

    /*
     * Geolocation method
     * Code will trigger time loop which will send JSON message to server's api with coordinates of the device
     * Timeout method is calling external class responsible for making the call
     */

   public void startGeolocation() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
            // Getting LocationManager object
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // Creating an empty criteria object
            Criteria criteria = new Criteria();

            // Getting the name of the provider that meets the criteria
            provider = locationManager.getBestProvider(criteria, false);

            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                //Declare the timer
                Timer myTimer = new Timer();
                //Set the schedule function and rate
                myTimer.scheduleAtFixedRate(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    //Called at every 3000 milliseconds (3 second)
                                                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                                        ActivityCompat.requestPermissions(parent, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

                                                    }

                                                    // Creating an empty criteria object
                                                    Criteria criteria = new Criteria();

                                                    Location location = locationManager.getLastKnownLocation(provider);
                                                    location = locationManager.getLastKnownLocation(provider);
                                                    new GPSTracker().onLocationChanged(location);

                                                }
                                            },
                        //set the amount of time in milliseconds before first execution
                        0,
                        //Set the amount of time between each execution (in milliseconds)
                        20000);
            } else {
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }


    /**
     * TextToSpeeach
     */
    public void textToSpeech(){

        tts=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        ConvertTextToSpeech();
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });

    }
    private void ConvertTextToSpeech() {
        // TODO Auto-generated method stub
        text = et;
        if(text==null||"".equals(text))
        {
            text = "Hi, it's good to see you!";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            Log.e("txt", text);

        }else
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        Log.e("txt", text);

    }

}
