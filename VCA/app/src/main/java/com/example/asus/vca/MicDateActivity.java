package com.example.asus.vca;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldSdk;
import com.integreight.onesheeld.sdk.ShieldFrame;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

public class MicDateActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextView voiceInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private byte voiceShieldId = OneSheeldSdk.getKnownShields().VOICE_RECOGNIZER_SHIELD.getId();
    private static final byte SEND_RESULT = 0x01;
    String device = Build.MODEL;
    String text;
    String et;
    TextToSpeech tts;
    String prefix = "calendar add title";
    String title = "";
    String txt = "";
    String date = "";
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;
    String calendarTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_recognition_view);
        voiceInput = (TextView) findViewById(R.id.voiceInput);
        calendarTitle = getIntent().getExtras().getString("calendarTitle");

        txt = "What is the date? Acceptable format is : 13th June 2019";
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
        }, 5000);
    }

    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            myTTS.setLanguage(Locale.US);
        }else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    // Showing google speech input dialog
    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What is the date? eg. 13th June 2019");
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
                    Log.e("SpeechRec", rec );

                    try {
                        String[] recArray = rec.split("\\s+");
                        String date = "";

                        if(recArray.length>=3)
                        {
                            if(recArray.length>3)
                            {
                                if(recArray[1].equals("of"))
                                {
                                    String dateM = getMonthNumber(recArray[2]);
                                    String dateD = getDayNumber(recArray[0]);
                                    String dateY = (recArray[3]);
                                    date = dateY + "-" + dateM + "-" + dateD;
                                }
                            }
                            else
                            {
                                String dateM = getMonthNumber(recArray[1]);
                                String dateD = getDayNumber(recArray[0]);
                                String dateY = (recArray[2]);
                                date = dateY + "-" + dateM + "-" + dateD;
                            }


//                            Log.e("action", "add to db" );
                            txt = "Record saved. Have a nice day.";
                            textToSpeech();
                            try {

                                String manufacturer = Build.MANUFACTURER;
                                device = Build.MODEL;
                                device = (manufacturer) + "-" + device;
                                device = device.toUpperCase();
                                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                device = device + "-"+android_id;

                                JSONObject subObj = new JSONObject();
                                subObj.put("action" , "new");
                                subObj.put("title" , calendarTitle);
                                subObj.put("desc" , "Voice input record");
                                subObj.put("t_from" , "00:00");
                                subObj.put("t_to" , "00:00");
                                subObj.put("user" , device);
                                subObj.put("date" , date);


                                JSONObject obj = new JSONObject();
                                obj.put("svc" , "calendar");
                                obj.put("dev" , device);
                                obj.put("msg" , subObj.toString());
//                            new PostData().execute(obj.toString());

                                PostData postData = new PostData();
                                postData.execute(obj.toString());
//            postData.onPostExecute(obj.toString());
//            Log.e("ResponseMainActivity", "" + postData.server_response);

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Do something after 100ms
                                        Intent mainA = new Intent(MicDateActivity.this, MainActivity.class);
                                        //run services activity intent
                                        startActivity(mainA);
                                    }
                                }, 3000);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                e.getMessage();
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

    /**
     * TextToSpeeach
     */
    public void textToSpeech(){

        tts=new TextToSpeech(MicDateActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        ConvertTextToSpeech();
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });

    }
    private void ConvertTextToSpeech() {
        // TODO Auto-generated method stub
        text = et;
        if(text==null||"".equals(text))
        {
//            text = "Hi, it's good to see you!";
            tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
//            Log.e("txt", text);

        }else
            tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
//        Log.e("txt", text);

    }

    public String getMonthNumber(String name){
        String monthId = "";
        name = name.toLowerCase();
        String[][] months = {{"January", "01"}, {"February", "02"}, {"March", "03"}, {"April", "04"}, {"May", "05"}, {"June", "06"},
                {"July", "07"}, {"August", "08"}, {"September", "09"},
                {"October", "10"}, {"November", "11"}, {"December", "12"}};

        for(int i =0; i<months.length; i++){
            if((months[i][0].toLowerCase()).equals(name)){
                monthId = months[i][1];
            }
        }


        return monthId;
    }

    public String getDayNumber(String name){
        String dayId = "";
        name = name.toLowerCase();
        String[][] suffixes =
            {       {"0th", "00"},  {"1st", "01"},  {"2nd", "02"},  {"3rd", "03"},
                    {"4th", "04"},  {"5th", "05"},  {"6th", "06"},  {"7th", "07"},
                    {"8th", "08"},  {"9th", "09"}, {"10th", "10"}, {"11th", "11"},
                    {"12th", "12"}, {"13th", "13"}, {"14th", "14"}, {"15th", "15"},
                    {"16th", "16"}, {"17th", "17"}, {"18th", "18"}, {"19th", "19"},
                    {"20th", "20"}, {"21st", "21"}, {"22nd", "22"}, {"23rd", "23"},
                    {"24th", "24"}, {"25th", "25"}, {"26th", "26"}, {"27th", "27"},
                    {"28th", "28"}, {"29th", "29"}, {"30th", "30"}, {"31st", "31"}
            };

        for(int i =0; i<suffixes.length; i++){
            if((suffixes[i][0].toLowerCase()).equals(name)){
                dayId = suffixes[i][1];
            }
        }


        return dayId;
    }

}