package com.example.asus.vca;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import android.content.Intent;
import android.view.View;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {                    //loads main activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        public void onDeviceFind (OneSheeldDevice device) {
             // Cancel scanning before connecting
            OneSheeldSdk.getManager().cancelScanning();
            // Connect to the found device
            device.connect();

        }

        };


        // Construct a new OneSheeldConnectionCallback callback and override onConnect method
        OneSheeldConnectionCallback connectionCallback = new OneSheeldConnectionCallback() {
                @Override
                public void onConnect (OneSheeldDevice device){
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
