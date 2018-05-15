package com.example.asus.vca;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldSdk;
import com.integreight.onesheeld.sdk.ShieldFrame;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

public class MicActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextView voiceInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private byte voiceShieldId = OneSheeldSdk.getKnownShields().VOICE_RECOGNIZER_SHIELD.getId();
    private static final byte SEND_RESULT = 0x01;
    String text;
    String et;
    TextToSpeech tts;
    String txt = "";
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_recognition_view);
        voiceInput = (TextView) findViewById(R.id.voiceInput);
        askSpeechInput();
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
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
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please say your command");
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
//                    rec = "calendar read";
                    try {
                        String[] recArray = rec.split("\\s+");

                        //verify that user has called out calendar
                        if (recArray[0].equals("calendar")) {
                            //verify user is adding new record
                            if (recArray[1].equals("add") || recArray[1].equals("and") || recArray[1].equals("at")) {
                                txt = "Adding new calendar record.";
                                textToSpeech();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Do something after 100ms
                                        Intent intentLoadMicTitleActivity = new Intent(getApplicationContext(), MicTitleActivity.class);
                                        startActivity(intentLoadMicTitleActivity);
                                    }
                                }, 3000);

                            } else if (recArray[1].equals("read") || recArray[1].equals("reed")) {
                                txt = "Tell me the date please.";
                                textToSpeech();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Do something after 100ms
                                        Intent intentLoadMicreadActivity = new Intent(getApplicationContext(), MicReadActivity.class);
                                        startActivity(intentLoadMicreadActivity);
                                    }
                                }, 3000);
                            }

                        } else {
                            OneSheeldManager manager = OneSheeldSdk.getManager();
                            String recognized = result.get(0);
                            if (recognized.toLowerCase().contains("time")) {
                                Calendar calendar = new GregorianCalendar();
                                Date trialTime = new Date();
                                calendar.setTime(trialTime);

                                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                                int minute = calendar.get(Calendar.MINUTE);

                                txt = "The current time is " + hourOfDay + " " + minute;
                                textToSpeech();
                            } else if (recognized.toLowerCase().contains("weather")) {
                                try {
                                    txt = "The weather in Cork is " +
                                            new WeatherTask().execute("http://api.openweathermap.org/data/2.5/weather?q=Cork,IE&appid=43d95b4cf5d0573e2dfe5186c160017a").get();
                                    textToSpeech();
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            } else if (manager != null) {
                                ShieldFrame sf = new ShieldFrame(voiceShieldId, SEND_RESULT);

                                Log.d("Home activity", recognized);
                                sf.addArgument(recognized.toLowerCase());
                                manager.broadcastShieldFrame(sf, true);
                            }
                        }
                    } catch (PatternSyntaxException ex) {
                        // error
                        Log.e("Error", "Speech recognize error: " + ex.getMessage());
                    }

                }
                break;
            }
        }
    }

    /* TextToSpeech */
    public void textToSpeech() {
        tts = new TextToSpeech(MicActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
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
        text = et;
        if (text == null || "".equals(text)) {
            tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
        } else
            tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
    }
}