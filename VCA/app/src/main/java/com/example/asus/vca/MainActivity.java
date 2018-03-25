package com.example.asus.vca;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;


import com.integreight.onesheeld.sdk.OneSheeldConnectionCallback;
import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldScanningCallback;
import com.integreight.onesheeld.sdk.OneSheeldSdk;


public class MainActivity extends AppCompatActivity {


    Button Speaker;
    Button Home;
    Button Services;
    Button Miscellaneous;
    BluetoothAdapter btAdapter;
    Button Bluetooth;




    BroadcastReceiver bluetoothState = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String prevStateExtra=BluetoothAdapter.EXTRA_PREVIOUS_STATE;
            String stateExtra = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(prevStateExtra, -1);
            //int previousState = intent.getInExtra(prevStateExtra,-1);
            String toastText="";
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
                    //setupUI();
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
                    //setupUI();
                    break;
                }


            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {                    //loads main activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
       // enableBluetooth();
        setupOneSheeld();


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
                    String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
                    String actionRequestEnable = BluetoothAdapter.ACTION_REQUEST_ENABLE;
                    IntentFilter filter = new IntentFilter(actionStateChanged);
                    registerReceiver(bluetoothState, filter);
                    startActivityForResult(new Intent(actionRequestEnable),0);

                }
            });



        }


    }

    public void setupOneSheeld() {
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
            public void onDeviceFind(OneSheeldDevice device) {
                // Cancel scanning before connecting
                OneSheeldSdk.getManager().cancelScanning();
                // Connect to the found device
                device.connect();

            }

        };


        // Construct a new OneSheeldConnectionCallback callback and override onConnect method
        OneSheeldConnectionCallback connectionCallback = new OneSheeldConnectionCallback() {
            @Override
            public void onConnect(OneSheeldDevice device) {
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


}












