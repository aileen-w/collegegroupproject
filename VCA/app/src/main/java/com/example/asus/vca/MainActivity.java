package com.example.asus.vca;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.integreight.onesheeld.sdk.OneSheeldConnectionCallback;
import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldScanningCallback;
import com.integreight.onesheeld.sdk.OneSheeldSdk;




public class MainActivity extends AppCompatActivity {

    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OneSheeldSdk.init(this);
        OneSheeldSdk.setDebugging(true);

        // Get the manager instance
        OneSheeldManager manager = OneSheeldSdk.getManager();
// Set the connection failing retry count to 1
        manager.setConnectionRetryCount(1);
// Set the automatic connecting retries to true, this will use 3 different methods for connecting
        manager.setAutomaticConnectingRetriesForClassicConnections(true);


        scanButton = findViewById(R.id.buttonScan);
        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                }

                catch (Exception ex) {
                    System.out.println("CANNOT FIND DEVICE");

                }
            }



        });

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
