#define CUSTOM_SETTINGS
#define INCLUDE_VOICE_RECOGNIZER_SHIELD
#define INCLUDE_MUSIC_PLAYER_SHIELD
#define INCLUDE_TERMINAL_SHIELD
#define INCLUDE_CLOCK_SHIELD
#define INCLUDE_TEXT_TO_SPEECH_SHIELD

/* Include 1Sheeld library. */
#include <OneSheeld.h>

/* create an initial value for the millis counter. */
unsigned long previousMillis = 0;
/* create an intial boolean for app running */
bool running = 0;

/* define the variables that will hold the hours and minutes. */
int hour, minute;
String strhr, strmin;

/* Voice commands set by the user. */
const char on[] = "on";
const char off[] = "off";
const char lights[] = "lights";
const char heating[] = "heating";
int lightLedPin = 13;
int heatingLedPin = 12;
const char mainCommand[] = "alexa";
const char playCommand[] = "play music";
const char timeCommand[] = "what time is it";

void setup()
{
  OneSheeld.begin(); /* Start Communication. */
  VoiceRecognition.setOnError(error); /* Error Commands handiling. */
  
  VoiceRecognition.start();
  pinMode(lightLedPin,OUTPUT);
  Clock.queryDateAndTime();
  VoiceRecognition.setOnNewCommand(&mainApplication);
}

void loop () 
{
   unsigned long currentMillis = millis();

  if (currentMillis - previousMillis >= 5000) {

    previousMillis = currentMillis;

    /* get the current time in your phone. */
     Clock.queryDateAndTime();
     /* start voice recognition. */
    VoiceRecognition.start();
  }
  hour = Clock.getHours();
  minute = Clock.getMinutes();

  /* save the hour and minutes as string. */
  strhr = String(hour);
  strmin = String(minute);
  
}


void mainApplication(char *commandSpoken)
{
   Terminal.println("Main loop");
  /* If new command matches key word then enable 'running' and the rest of the commands. */
  if(!strcmp(VoiceRecognition.getLastCommand(), mainCommand))
  {
    running = 1;
  }
   
  if(strstr(VoiceRecognition.getLastCommand(), lights) && voiceHasBeenActivated())
  {
    if(strstr(VoiceRecognition.getLastCommand(), on))
    {
      Terminal.println("Lights on");
      turnOnLED(lightLedPin); /* Turn the 'lights' on */
      running = 0;
    }
    else if (strstr(VoiceRecognition.getLastCommand(), off)) {
      Terminal.println("Lights off");
      turnOffLED(lightLedPin);  /* Turn the 'lights' off */
      running = 0;
    }
  }

  if(strstr(VoiceRecognition.getLastCommand(), heating) && voiceHasBeenActivated())
  {
    if(strstr(VoiceRecognition.getLastCommand(), on))
    {
      Terminal.println("Heating on");
      turnOnLED(heatingLedPin); /* Turn the 'heating' on */
      running = 0;
    }
    else if (strstr(VoiceRecognition.getLastCommand(), off)) {
      Terminal.println("Heating off");
      turnOffLED(heatingLedPin);  /* Turn the 'heating' off */
      running = 0;
    }
  }

  if (!strcmp(timeCommand, VoiceRecognition.getLastCommand()) && voiceHasBeenActivated()) 
  {
    Terminal.println("Time");
    TextToSpeech.say(strhr + "" + strmin);
    running = 0;
  }
  else if (!strcmp(playCommand, VoiceRecognition.getLastCommand())) 
  {
    Terminal.println("Music");
    MusicPlayer.play();
    delay(10000);
    MusicPlayer.stop();
    running = 0;
  }
}


boolean voiceHasBeenActivated() {
  return running == 1;
}

void turnOnLED(int pin) {
  digitalWrite(pin, HIGH);
}

void turnOffLED(int pin) {
  digitalWrite(pin, LOW);
}

/* Error checking function. */
void error(byte errorData)
{
  /* Switch on error and print it on the terminal. */
  switch(errorData)
  {
    case NETWORK_TIMEOUT_ERROR: Terminal.println("Network timeout");break;
    case NETWORK_ERROR: Terminal.println("Network Error");break;
    case AUDIO_ERROR: Terminal.println("Audio error");break;
    case SERVER_ERROR: Terminal.println("No Server");break;
    case SPEECH_TIMEOUT_ERROR: Terminal.println("Speech timeout");break;
    case NO_MATCH_ERROR: Terminal.println("No match");break;
    case RECOGNIZER_BUSY_ERROR: Terminal.println("Busy");break;
  }
}
