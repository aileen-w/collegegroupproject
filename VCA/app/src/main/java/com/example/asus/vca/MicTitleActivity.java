package com.example.asus.vca;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.integreight.onesheeld.sdk.OneSheeldSdk;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

public class MicTitleActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextView voiceInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private byte voiceShieldId = OneSheeldSdk.getKnownShields().VOICE_RECOGNIZER_SHIELD.getId();
    private static final byte SEND_RESULT = 0x01;
    String device = Build.MODEL;
    String text;
    String et;
    TextToSpeech tts;
    String prefix = "calendar add";
    String title = "";
    String txt = "";
    String date = "";
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_recognition_view);
        voiceInput = (TextView) findViewById(R.id.voiceInput);

        txt = "What is the event you wish to add?";
        textToSpeech();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                askSpeechInput();
                Intent checkTTSIntent = new Intent();
                checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
            }
        }, 3000);

    }

    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            myTTS.setLanguage(Locale.US);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    // Showing google speech input dialog
    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What is the event you wish to add?");
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
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    voiceInput.setText(result.get(0));

                    String rec = (result.get(0)).toLowerCase();

                    try {

                        Intent intentLoadMicDataActivity = new Intent(getApplicationContext(), MicDateActivity.class);
                        intentLoadMicDataActivity.putExtra("calendarTitle", rec);
                        startActivity(intentLoadMicDataActivity);

                    } catch (PatternSyntaxException ex) {
                        // error
                        Log.e("Error", "Speech recognize error: " + ex.getMessage());
                    }

                }
                break;
            }
        }
    }

    /**
     * TextToSpeeach
     */
    public void textToSpeech() {

        tts = new TextToSpeech(MicTitleActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    } else {
                        ConvertTextToSpeech();
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });

    }

    private void ConvertTextToSpeech() {
        // TODO Auto-generated method stub
        text = et;
        if (text == null || "".equals(text)) {
            tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
//            Log.e("txt", text);

        } else
            tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
//        Log.e("txt", text);

    }
}