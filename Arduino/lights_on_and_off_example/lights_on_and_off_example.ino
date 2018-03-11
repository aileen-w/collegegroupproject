/*

Voice Recognition Shield Example

This example shows an application on 1Sheeld's voice recognition shield.

By using this example, you can play, pause and stop your smartphone's 
music using voice commands.

OPTIONAL:
To reduce the library compiled size and limit its memory usage, you
can specify which shields you want to include in your sketch by
defining CUSTOM_SETTINGS and the shields respective INCLUDE_ define. 

*/

#define CUSTOM_SETTINGS
#define INCLUDE_VOICE_RECOGNIZER_SHIELD
#define INCLUDE_MUSIC_PLAYER_SHIELD
#define INCLUDE_TERMINAL_SHIELD
#define INCLUDE_CLOCK_SHIELD

/* Include 1Sheeld library. */
#include <OneSheeld.h>

/* Voice commands set by the user. */
const char on[] = "on";
const char off[] = "off";
const char lights[] = "lights";
int ledPin = 13;

void setup()
{
  /* Start Communication. */
  OneSheeld.begin();
  /* Error Commands handiling. */
  VoiceRecognition.setOnError(error);
  
  VoiceRecognition.start();
  pinMode(ledPin,OUTPUT);
  Clock.queryDateAndTime();
}

void loop () 
{
  /* Check if new command received. */
  if(VoiceRecognition.isNewCommandReceived())
  {
    /* Compare the play command. */
    if(strstr(VoiceRecognition.getLastCommand(), lights) && strstr(VoiceRecognition.getLastCommand(), on))
    {
      /* Play the track. */
      digitalWrite(ledPin,HIGH);
    }
    /* Compare the pause command. */
    else if (strstr(VoiceRecognition.getLastCommand(), lights) && strstr(VoiceRecognition.getLastCommand(), off))
    {
      /* Pause the track. */
      digitalWrite(ledPin,LOW);
    }
    else if (!strcmp("time",VoiceRecognition.getLastCommand())) 
    {
      TextToSpeech.say(Clock.getHours() + "" + Clock.getMinutes());
    }
  }
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
