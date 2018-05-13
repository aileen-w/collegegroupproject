package com.example.asus.vca;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AudioActivity extends AppCompatActivity {
    String text;
    String et;
    TextToSpeech tts;
    String txt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {    //loads audio activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
    }

    public void onClickTellTime(View v) {
        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        txt = "The current time is " + hourOfDay + " " + minute;
        textToSpeech();

    }

    public void onClickTellWeather(View v) {
        try {
            txt = "The weather in Cork is " +
                    new WeatherTask().execute("http://api.openweathermap.org/data/2.5/weather?q=Cork,IE&appid=43d95b4cf5d0573e2dfe5186c160017a").get();
            textToSpeech();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    /* TextToSpeech */
    public void textToSpeech() {
        tts = new TextToSpeech(AudioActivity.this, new TextToSpeech.OnInitListener() {

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

