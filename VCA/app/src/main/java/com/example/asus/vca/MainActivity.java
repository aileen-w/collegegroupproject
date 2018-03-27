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

import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {


    protected static final int DISCOVERY_REQUEST = 1 ;
    public Button Speaker;
    public Button Home;
    public Button Services;
    public Button Miscellaneous;
    private BluetoothAdapter btAdapter;
    public Button Bluetooth;
    public String toastText="";
    private BluetoothDevice remoteDevice;

    private ArrayList<OneSheeldDevice> oneSheeldScannedDevices;
    private ArrayList<OneSheeldDevice> oneSheeldConnectedDevices;

    String model = Build.MODEL;
    LocationManager locationManager;
    String provider;
    Activity parent = this;



    BroadcastReceiver bluetoothState = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String prevStateExtra=BluetoothAdapter.EXTRA_PREVIOUS_STATE;
            String stateExtra = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(prevStateExtra, -1);
            //int previousState = intent.getInExtra(prevStateExtra,-1);
            switch(state){
                case(BluetoothAdapter.STATE_TURNING_ON):
                {

                    toastText = "Bluetooth Turning On";
                    Toast.makeText(MainActivity.this,toastText,Toast.LENGTH_SHORT).show();
                    break;

                }
                case(BluetoothAdapter.STATE_ON):{

                    toastText = "Bluetooth On";
                    Toast.makeText(MainActivity.this,toastText,Toast.LENGTH_SHORT).show();
                    setupUI();
                    break;

                }
                case(BluetoothAdapter.STATE_TURNING_OFF):{

                    toastText = "Bluetooth Turning Off";
                    Toast.makeText(MainActivity.this,toastText,Toast.LENGTH_SHORT).show();
                    break;

                }
                case(BluetoothAdapter.STATE_OFF):{

                    toastText = "Bluetooth Off";
                    Toast.makeText(MainActivity.this,toastText,Toast.LENGTH_SHORT).show();
                    setupUI();
                    break;
                }


            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {                    //loads main activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init the SDK with context
        OneSheeldSdk.init(this);
        //Optional, enable debugging messages.
        OneSheeldSdk.setDebugging(true);

        setupUI();
        appIsUp(); // send message to server with information that app was launched
        startGeolocation(); // start geolocation tracking

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


                        // Cancel scanning before connecting
                        OneSheeldSdk.getManager().cancelScanning();
                        // Connect to the found device
                        device.connect();




                }


        };

                // Construct a new OneSheeldConnectionCallback callback and override onConnect method
                OneSheeldConnectionCallback connectionCallback = new OneSheeldConnectionCallback() {
                    @Override
                    public void onConnect(final OneSheeldDevice device) {

                        oneSheeldScannedDevices.remove(device);
                        oneSheeldConnectedDevices.add(device);
                        final String deviceName = device.getName();



                        // Output high on pin 13
                       device.digitalWrite(13, true);

                        // Read the value of pin 12
                        boolean isHigh = device.digitalRead(12);

                    }
                };



        // Add the connection and scanning callbacks
        manager.addConnectionCallback(connectionCallback);
        manager.addScanningCallback(scanningCallback);

        // Initiate the Bluetooth scanning
        manager.scan();



    }
    private void setupUI() {

        //create text view stauts update
        btAdapter = BluetoothAdapter.getDefaultAdapter();
             /*if(btAdapter.isEnabled()) {
                String address = btAdapter.getAddress();
                String name = btAdapter.getName();
                String statusText= address+" : "+name;
                //statusUpdate.setText(statusText);
             }

        else {

                 //statusUpdate.setText("Bluetooth is disabled");
             }*/


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

        Bluetooth = findViewById(R.id.buttonBluetooth);
        {
            Bluetooth.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    // String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
                    // String actionRequestEnable = BluetoothAdapter.ACTION_REQUEST_ENABLE;
                    // IntentFilter filter = new IntentFilter(actionStateChanged);
                    // registerReceiver(bluetoothState, filter);
                    // startActivityForResult(new Intent(actionRequestEnable),0);

                    //register for discovery events
                    String scanModeChanged = BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
                    String beDiscoverable = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
                    IntentFilter filter = new IntentFilter(scanModeChanged);
                    registerReceiver(bluetoothState,filter);
                    startActivityForResult(new Intent(beDiscoverable), DISCOVERY_REQUEST);

                }
            });



        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == DISCOVERY_REQUEST){
            Toast.makeText(MainActivity.this,"Discovery in progress",Toast.LENGTH_SHORT).show();
            setupUI();
            findDevices();

        }

    }


    private void findDevices(){                             //looks for previous paired devices
        String lastUsedRemoteDevice = getLastUsedRemoteBTDevice();
        if(lastUsedRemoteDevice!=null){
            toastText="Checking for known paired devices, namely: "+lastUsedRemoteDevice;
            Toast.makeText(MainActivity.this,toastText,Toast.LENGTH_SHORT).show();
            //see if this device is in a list of currently visible (?), paired devices
            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
            for(BluetoothDevice pairedDevice : pairedDevices){
                if(pairedDevice.getAddress().equals(lastUsedRemoteDevice)){
                    toastText="Found Device: "+ pairedDevice.getName()+ "@" + lastUsedRemoteDevice;
                    Toast.makeText(MainActivity.this,toastText,Toast.LENGTH_SHORT).show();
                    remoteDevice = pairedDevice;


                }

            }

        }//end if

        if (remoteDevice == null) {
            toastText="Start discovering for remote devices...";
            Toast.makeText(MainActivity.this,toastText,Toast.LENGTH_SHORT).show();
            //start discovery
            if(btAdapter.startDiscovery()) {
                toastText="Discovery thread started....Scanning for devices";
                registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));          //creating another broadcast receiver
            }

        }

    }//end find devices

    //create a broadcast receiver to receive device discovery
    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice;
            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            toastText="Discovered"+ remoteDeviceName;
            Toast.makeText(MainActivity.this,toastText,Toast.LENGTH_SHORT).show();

        }
    };


    private String getLastUsedRemoteBTDevice(){
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String result = prefs.getString("LAST_REMOTE_DEVICE_ADDRESS",null);
        return result;
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
            obj.put("dev" , "1sheeld");
            obj.put("msg" , "Android app is up and running");
            new PostData().execute(obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            e.getMessage();
        }

    }


    /**
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
                        3000);
            } else {
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }

}
































