package com.example.asus.vca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.integreight.onesheeld.sdk.OneSheeldSdk;
import com.integreight.onesheeld.sdk.ShieldFrame;
import com.integreight.onesheeld.sdk.OneSheeldManager;

public class HomeActivity extends AppCompatActivity {

    private byte voiceShieldId = OneSheeldSdk.getKnownShields().VOICE_RECOGNIZER_SHIELD.getId();
    private static final byte SEND_RESULT = 0x01;
    private String recognized = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onClickChangeLights(View v) {
        Log.d("Lights", "Lights Request");

        OneSheeldManager manager = OneSheeldSdk.getManager();
        if (manager != null) {
            ShieldFrame sf = new ShieldFrame(voiceShieldId, SEND_RESULT);
            recognized = "Lights change";
            Log.d("Lights", "change");

            sf.addArgument(recognized.toLowerCase());
            manager.broadcastShieldFrame(sf, true);
        }
    }

    public void onClickChangeHeating(View v) {
        Log.d("Heating", "Heating Request");

        OneSheeldManager manager = OneSheeldSdk.getManager();
        if (manager != null) {
            ShieldFrame sf = new ShieldFrame(voiceShieldId, SEND_RESULT);
            recognized = "Heating change";
            Log.d("Heating", "change");
            sf.addArgument(recognized.toLowerCase());
            manager.broadcastShieldFrame(sf, true);
        }
    }
}
