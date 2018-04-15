package com.example.asus.vca;

        import android.content.ActivityNotFoundException;
        import android.content.Intent;
        import android.speech.RecognizerIntent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.TextView;

        import com.integreight.onesheeld.sdk.OneSheeldManager;
        import com.integreight.onesheeld.sdk.OneSheeldSdk;
        import com.integreight.onesheeld.sdk.ShieldFrame;

        import java.util.ArrayList;
        import java.util.Locale;

public class MicActivity extends AppCompatActivity {

    private TextView voiceInput;
    private TextView speakButton;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private byte voiceShieldId = OneSheeldSdk.getKnownShields().VOICE_RECOGNIZER_SHIELD.getId();
    private static final byte SEND_RESULT = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_recognition_view);

        voiceInput = (TextView) findViewById(R.id.voiceInput);
        askSpeechInput();

    }

    // Showing google speech input dialog

    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    // Receiving speech input

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    voiceInput.setText(result.get(0));

                    OneSheeldManager manager = OneSheeldSdk.getManager();
                    if (manager != null) {
                        ShieldFrame sf = new ShieldFrame(voiceShieldId, SEND_RESULT);
                        String recognized = result.get(0);
                        Log.d("Lights", recognized);
                        sf.addArgument(recognized.toLowerCase());
                        manager.broadcastShieldFrame(sf, true);
                    }
                }
                break;
            }

        }
    }
}