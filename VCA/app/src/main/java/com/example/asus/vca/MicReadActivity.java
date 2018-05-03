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

import com.integreight.onesheeld.sdk.OneSheeldSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

public class MicReadActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextView voiceInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private byte voiceShieldId = OneSheeldSdk.getKnownShields().VOICE_RECOGNIZER_SHIELD.getId();
    private static final byte SEND_RESULT = 0x01;
    String device = Build.MODEL;
//    String text;
    String et;
    TextToSpeech tts;
    String prefix = "calendar add title";
    String title = "";
    String txt = "";
    String date = "";
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;
    String calendarTitle = "";
    JSONArray msg;
    String text2 = "";
    String text3 = "";
    PostData postData = new PostData();
    JSONObject text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_recognition_view);
        voiceInput = (TextView) findViewById(R.id.voiceInput);
//        calendarTitle = getIntent().getExtras().getString("calendarTitle");

                askSpeechInput();
                Intent checkTTSIntent = new Intent();
                checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
//            }
//        }, 5000);
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
//                    rec = "3rd May 2018";
//                    Log.e("SpeechRec", rec );

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

                            try {

                                String manufacturer = Build.MANUFACTURER;
                                device = Build.MODEL;
                                device = (manufacturer) + "-" + device;
                                device = device.toUpperCase();
                                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                device = device + "-"+android_id;

                                JSONObject subObj = new JSONObject();
                                subObj.put("action" , "view");
                                subObj.put("user" , device);
//                                subObj.put("date" , "2018-05-03");
                                subObj.put("date" , date);


                                JSONObject obj = new JSONObject();
                                obj.put("svc" , "calendar");
                                obj.put("dev" , device);
                                obj.put("msg" , subObj.toString());
                                postData.execute(obj.toString());
                                postData.onPostExecute(obj.toString());

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Do something after 100ms
                                        String res = postData.returnServerRespond();
                                        JSONObject data = null;
                                        try {
                                            if(res!=null && res.length()>2)
                                            {
                                                String replaceString=res.replace('"','\"');//replaces all occurrences of 'a' to 'e'
                                                data = new JSONObject(replaceString);
                                                // get a JSONArray from inside an object
                                                msg = data.getJSONArray("msg");
                                                // get the String contained in the object -> logs Bonjour tout le monde
                                                if(msg.length()>0)
                                                {
                                                    txt = "here are your events for selected day:";
                                                    textToSpeech();

                                                    final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //Do something after 100ms

                                                            for(int i=0; i<msg.length(); i++)
                                                            {

                                                                try {
                                                                    text = msg.getJSONObject(i);
//                                                                    Log.d("***>Text", text.getString("title"));
                                                                    text3 = text3 + ", " + text.getString("title");

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }

                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                //Do something after 100ms
                                                                txt = text3;
                                                                textToSpeech();

                                                                final Handler handler = new Handler();
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        //Do something after 100ms
                                                                        Intent mainA = new Intent(MicReadActivity.this, MainActivity.class);
                                                                        //run services activity intent
                                                                        startActivity(mainA);
                                                                    }
                                                                }, 2000);

                                                                }
                                                            }, 2000);

                                                        }
                                                    }, 2000);

                                                }
                                                else
                                                {
                                                    txt = "There is no records available for that date.";
                                                    textToSpeech();
                                                }
                                            }
                                            else
                                            {
                                                txt = "There is no records available for that date.";
                                                textToSpeech();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 2000);
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

        tts=new TextToSpeech(MicReadActivity.this, new TextToSpeech.OnInitListener() {

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
        text2 = et;
        if(text2==null||"".equals(text2))
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
//        String[][] months = {{"January", "01"}, {"February", "02"}, {"March", "03"}, {"April", "04"}, {"May", "05"}, {"June", "06"},
//                {"July", "07"}, {"August", "08"}, {"September", "09"},
//                {"October", "10"}, {"November", "11"}, {"December", "12"}};

        String[][] months = {{"January", "1"}, {"February", "2"}, {"March", "3"}, {"April", "4"}, {"May", "5"}, {"June", "6"},
                {"July", "7"}, {"August", "8"}, {"September", "9"},
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
//        String[][] suffixes =
//                {       {"0th", "00"},  {"1st", "01"},  {"2nd", "02"},  {"3rd", "03"},
//                        {"4th", "04"},  {"5th", "05"},  {"6th", "06"},  {"7th", "07"},
//                        {"8th", "08"},  {"9th", "09"}, {"10th", "10"}, {"11th", "11"},
//                        {"12th", "12"}, {"13th", "13"}, {"14th", "14"}, {"15th", "15"},
//                        {"16th", "16"}, {"17th", "17"}, {"18th", "18"}, {"19th", "19"},
//                        {"20th", "20"}, {"21st", "21"}, {"22nd", "22"}, {"23rd", "23"},
//                        {"24th", "24"}, {"25th", "25"}, {"26th", "26"}, {"27th", "27"},
//                        {"28th", "28"}, {"29th", "29"}, {"30th", "30"}, {"31st", "31"}
//                };

        String[][] suffixes =
                {       {"0th", "0"},  {"1st", "1"},  {"2nd", "2"},  {"3rd", "3"},
                        {"4th", "4"},  {"5th", "5"},  {"6th", "6"},  {"7th", "7"},
                        {"8th", "8"},  {"9th", "9"}, {"10th", "10"}, {"11th", "11"},
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

    ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }
}