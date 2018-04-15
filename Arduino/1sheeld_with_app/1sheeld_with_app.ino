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
  // put your setup code here, to run once:
  OneSheeld.begin();
  pinMode(lightLedPin,OUTPUT);
 // Serial.begin(115200);
  //OneSheeld.setOnNewShieldFrame(&onNewShieldFrame);
 // OneSheeld.setOnNewSerialData(&onNewSerialData);
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
      running = 0;
    }
    else if (strstr(VoiceRecognition.getLastCommand(), off)) {
      turnOffLED(lightLedPin);  /* Turn the 'lights' off */
      running = 0;
    }
  }

   if(strstr(VoiceRecognition.getLastCommand(), heating))
  {
    if(strstr(VoiceRecognition.getLastCommand(), on))
    {
      turnOnLED(heatingLedPin); /* Turn the 'lights' on */
      running = 0;
    }
    else if (strstr(VoiceRecognition.getLastCommand(), off)) {
      turnOffLED(heatingLedPin);  /* Turn the 'lights' off */
      running = 0;
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

//void onNewShieldFrame(byte shieldId, byte functionId, byte argsNumber, byte *argsLength,byte **argsData){
//  String alexa = "Alexa";
//
//  Serial.print(alexa);
//  Serial.println((const char *)argsData[0]);
//  
//  if(shieldId==voice_shield_id){
//      digitalWrite(lightLedPin, HIGH);
//  }
//}
//
//void onNewSerialData(byte data){
//  
//      digitalWrite(lightLedPin, HIGH);
//}
