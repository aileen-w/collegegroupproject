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

import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldSdk;
import com.integreight.onesheeld.sdk.ShieldFrame;

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

        txt = "What is the date? Acceptable format is: year month day, all numeric values.";
        textToSpeech();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        askSpeechInput();
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
//        textToSpeech();
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == MY_DATA_CHECK_CODE) {
//            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
//                myTTS = new TextToSpeech(this, this);
//            }
//            else {
//                Intent installTTSIntent = new Intent();
//                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
//                startActivity(installTTSIntent);
//            }
//        }
//    }
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

//        if (requestCode == MY_DATA_CHECK_CODE) {
//            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
//                myTTS = new TextToSpeech(this, this);
//            }
//            else {
//                Intent installTTSIntent = new Intent();
//                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
//                startActivity(installTTSIntent);
//            }
//        }
        textToSpeech();
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    voiceInput.setText(result.get(0));

                    final Handler handler = new Handler();
//                    textToSpeech();

                    String rec = (result.get(0)).toLowerCase();
//                    Log.e("recognizedArray1", rec);
//                    myTTS.speak("hello", TextToSpeech.QUEUE_FLUSH, null);

//                    rec = prefix + rec;
//                    rec = "calendar add";
                    try {
                        String[] recArray = rec.split("\\s+");
                        Log.e("rec", rec );
                        Log.e("calendarTitle", calendarTitle );

//                        Log.e("recognizedArray2", recArray[0]);
//                        tts.speak("Adding new calendar record.", TextToSpeech.QUEUE_FLUSH, null);


                        //verify that user has called out calendar
//                        if(recArray[0].equals("calendar"))
//                        {


                            //verify user is adding new record
//                            if(recArray[1].equals("add"))
//                            {
//                                txt = "Adding new calendar record.";
//                                Log.e("recognizedArray3", rec);
//                                Log.e("title", "title:" + title.length());
//                                Log.e("date", "date:"+date.length());
//                                if((title.length())==0 && (date.length())==0)
//                                {
//                                    Log.e("recognizedArray4", "here");
//                                    ConvertTextToSpeech("Adding new calendar record.");
//                                    txt = "Adding new calendar record.";
//                                    textToSpeech();
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            // Do something after 5s = 5000ms
//                                            Log.e("recognizedArray4", "here");
//
////                                            Intent intentLoadMicActivity = new Intent(getApplicationContext(), MicActivity.class);
////                                            startActivity(intentLoadMicActivity);
//                                        }
//                                    }, 3000);
//                                    try {
//                                        Thread.sleep(3000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                    Log.e("txt", txt);
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                        tts.speak("Adding new calendar record.",TextToSpeech.QUEUE_FLUSH,null,null);
//                                    } else {
//                                        tts.speak("Adding new calendar record.", TextToSpeech.QUEUE_FLUSH, null);
//                                    }
//                                    tts.speak("Adding new calendar record.", TextToSpeech.QUEUE_FLUSH, null);
//                                }

//                                if(recArray.length>=3 && recArray[2].equals("title"))
//                                {
//                                    int x = 2;
//                                    while (x < recArray.length)
//                                    {
//                                        title = title + " " + recArray[x];
//                                        x++;
//                                    }
//                                    Log.e("title", title);
//
//                                }
//                                if(title.equals(""))
//                                {
                                    //ask for title
//                                    tts.speak("What is the event you wish to add?", TextToSpeech.QUEUE_FLUSH, null);
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                        tts.speak("What is the event you wish to add?",TextToSpeech.QUEUE_FLUSH,null,null);
//                                    } else {
//                                        tts.speak("What is the event you wish to add?", TextToSpeech.QUEUE_FLUSH, null);
//                                    }
//                                    txt = "What is the event you wish to add?";
//                                    textToSpeech();
                                    //get the answer
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            // Do something after 5s = 5000ms
//
//                                            Intent intentLoadMicActivity = new Intent(getApplicationContext(), MicActivity.class);
//                                            startActivity(intentLoadMicActivity);
//                                        }
//                                    }, 3000);
//                                    try {
//                                        Thread.sleep(3000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                    prefix = "calendar add title";
//                                    Intent intentLoadMicActivity = new Intent(getApplicationContext(), MicDateActivity.class);
//                                    startActivity(intentLoadMicActivity);
//                                }
//                                Log.e("txt", txt);

//                                if(recArray.length>=4 && recArray[3].equals("date") && title.length()>0)
//                                {
//                                    int x = 3;
//                                    while (x < recArray.length)
//                                    {
//                                        date = date + "-" + recArray[x];
//                                        x++;
//                                    }
//                                    Log.e("date", date );
//                                }
//                                if(date.equals("") && title.length()>0)
//                                {
//                                    tts.speak("Adding new calendar record.", TextToSpeech.QUEUE_FLUSH, null);

                                    //ask for title
//                                    tts.speak("What is the date? Acceptable format is: year month day, all numeric values.", TextToSpeech.QUEUE_FLUSH, null);
//                                    txt = "What is the date? Acceptable format is: year month day, all numeric values.";
//                                    textToSpeech();
                                    //get the answer
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            // Do something after 5s = 5000ms
//                                            prefix = "calendar add title date";
//                                            Intent intentLoadMicActivity = new Intent(getApplicationContext(), MicActivity.class);
//                                            startActivity(intentLoadMicActivity);
//                                        }
//                                    }, 5000);
//                                    try {
//                                        Thread.sleep(5000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                    prefix = "calendar add title date";
//                                    Intent intentLoadMicActivity = new Intent(getApplicationContext(), MicDateActivity.class);
//                                    startActivity(intentLoadMicActivity);
//                                }
//                                Log.e("txt", txt);

//                                if(date.length()>0 && title.length()>0)
//                                {
                                    Log.e("action", "add to db" );
                                    txt = "Record saved";
                                    textToSpeech();
//                                }
                                Log.e("txt", txt);

//                            }

//                            try {
//
//                                String manufacturer = Build.MANUFACTURER;
//                                device = Build.MODEL;
//                                device = (manufacturer) + "-" + device;
//                                device = device.toUpperCase();
//                                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//                                device = device + "-"+android_id;
//
//                                JSONObject obj = new JSONObject();
//                                obj.put("svc" , "calendar");
//                                obj.put("dev" , device);
//
//                                JSONObject objIn = new JSONObject();
//                                objIn.put("action" , "new");
//                                objIn.put("title" , "new");
//                                objIn.put("user" , device);
//                                objIn.put("desc" , "new");
//                                objIn.put("date" , "new");
//                                objIn.put("t_from" , "00:00");
//                                objIn.put("t_to" , "00:00");
//                                obj.put("msg" , objIn);
//                                PostData postData = new PostData();
////                                postData.execute(obj.toString());
//                                postData.onPostExecute(obj.toString());
//                                Log.e("ResponseMainActivity", "" + postData.server_response);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                e.getMessage();
//                            }

//                        }
//                        else
//                        {
//                            OneSheeldManager manager = OneSheeldSdk.getManager();
//                            if (manager != null) {
//                                ShieldFrame sf = new ShieldFrame(voiceShieldId, SEND_RESULT);
//                                String recognized = result.get(0);
//                                Log.d("Lights", recognized);
//                                sf.addArgument(recognized.toLowerCase());
//                                manager.broadcastShieldFrame(sf, true);
//                            }
//                        }
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
}