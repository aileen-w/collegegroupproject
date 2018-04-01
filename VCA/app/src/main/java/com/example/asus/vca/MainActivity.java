package com.example.asus.vca;




import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;



import com.integreight.onesheeld.sdk.OneSheeldConnectionCallback;
import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldScanningCallback;
import com.integreight.onesheeld.sdk.OneSheeldSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    protected static final int DISCOVERY_REQUEST = 1;
    public Button Speaker;
    public Button Home;
    public Button Services;
    public Button Miscellaneous;
    private BluetoothAdapter btAdapter;
    public Button Bluetooth;
    public String toastText = "";
    private BluetoothDevice remoteDevice;

    private ArrayList<String> connectedDevicesNames;
    private ArrayList<String> scannedDevicesNames;
    private ArrayList<OneSheeldDevice> oneSheeldScannedDevices;
    private ArrayList<OneSheeldDevice> oneSheeldConnectedDevices;
    private ArrayAdapter<String> connectedDevicesArrayAdapter;
    private ArrayAdapter<String> scannedDevicesArrayAdapter;


    private Handler uiThreadHandler = new Handler();
    String model = Build.MODEL;
    LocationManager locationManager;
    String provider;
    Activity parent = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {                    //loads main activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        setupOneSheeld();
        appIsUp();
        startGeolocation();         //start geolocation tracking

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
        OneSheeldManager manager = OneSheeldSdk.getManager();
        // Set the connection failing retry count to 1
        manager.setConnectionRetryCount(1);
        // Set the automatic connecting retries to true, this will use 3 different methods for connecting
        manager.setAutomaticConnectingRetriesForClassicConnections(true);

        //Construct a new OneSheeldScanningCallback callback and override onDeviceFind method
        OneSheeldScanningCallback scanningCallback = new OneSheeldScanningCallback() {
            @Override
            public void onDeviceFind(final OneSheeldDevice device) {
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {


                        oneSheeldScannedDevices.add(device);
                        scannedDevicesNames.add(device.getName());
                        scannedDevicesArrayAdapter.notifyDataSetChanged();
                    }
                });
            }


        };

        // Construct a new OneSheeldConnectionCallback callback and override onConnect method
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

                        // Output high on pin 13
                        // device.digitalWrite(13, true);

                        // Read the value of pin 12
                        //boolean isHigh = device.digitalRead(12);

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

            JSONObject obj = new JSONObject();
            obj.put("svc" , "notification");
            obj.put("dev" , model);
            obj.put("msg" , "Android app is up and running");
            new PostData().execute(obj.toString());

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

}
