#define CUSTOM_SETTINGS
#define INCLUDE_VOICE_RECOGNIZER_SHIELD
#define INCLUDE_MUSIC_PLAYER_SHIELD
#define INCLUDE_TERMINAL_SHIELD
#define INCLUDE_CLOCK_SHIELD
#define INCLUDE_TEXT_TO_SPEECH_SHIELD

#include <OneSheeld.h>

int lightLedPin = 13;
int heatingLedPin = 13;

bool running = 0;

int i = 36;
const byte voice_shield_id = (byte) i;

const char on[] = "on";
const char off[] = "off";
const char lights[] = "lights";
const char heating[] = "heating";
const char mainCommand[] = "alexa";

void setup() {
  OneSheeld.begin();
  pinMode(lightLedPin,OUTPUT);
  VoiceRecognition.setOnNewCommand(&mainApplication);
}

void loop() {
  // put your main code here, to run repeatedly:

}

void mainApplication(char *commandSpoken)
{
//  if(!strcmp(VoiceRecognition.getLastCommand(), mainCommand))
//  {
//    running = 1;
//  }
   
  if(strstr(VoiceRecognition.getLastCommand(), lights))
  {
    if(strstr(VoiceRecognition.getLastCommand(), on))
    {
      turnOnLED(lightLedPin); /* Turn the 'lights' on */
    }
    else if (strstr(VoiceRecognition.getLastCommand(), off)) {
      turnOffLED(lightLedPin);  /* Turn the 'lights' off */
    }
  }

  if(strstr(VoiceRecognition.getLastCommand(), heating))
  {
    if(strstr(VoiceRecognition.getLastCommand(), on))
    {
      turnOnLED(heatingLedPin); /* Turn the 'heating' on */
    }
    else if (strstr(VoiceRecognition.getLastCommand(), off)) {
      turnOffLED(heatingLedPin);  /* Turn the 'heating' off */
    }
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
