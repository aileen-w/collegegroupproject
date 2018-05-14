#define CUSTOM_SETTINGS
#define INCLUDE_VOICE_RECOGNIZER_SHIELD
#define INCLUDE_MUSIC_PLAYER_SHIELD
#define INCLUDE_TERMINAL_SHIELD
#define INCLUDE_CLOCK_SHIELD
#define INCLUDE_TEXT_TO_SPEECH_SHIELD

#include <OneSheeld.h>

int lightLedPin = 13;
int heatingLedPin = 12;
int autoHeatingLedPin = 11;

bool running = 0;

int i = 36;
const byte voice_shield_id = (byte) i;

const char on[] = "on";
const char off[] = "off";
const char lights[] = "lights";
const char heating[] = "heating";

int ThermistorPin = 0;
int Vo;
float R1 = 20000;
float logR2, R2, T, tempHome;
float c1 = 1.009249522e-03, c2 = 2.378405444e-04, c3 = 2.019202697e-07;

void setup() {
  OneSheeld.begin();
  pinMode(lightLedPin, OUTPUT);
  pinMode(heatingLedPin, OUTPUT);
  pinMode(autoHeatingLedPin, OUTPUT);
  VoiceRecognition.setOnNewCommand(&mainApplication);
}

void loop() {
  // put your main code here, to run repeatedly:
Vo = analogRead(ThermistorPin);
  R2 = R1 * (1023.0 / (float)Vo - 1.0);
  logR2 = log(R2);
  T = (1.0 / (c1 + c2*logR2 + c3*logR2*logR2*logR2));
  tempHome = T - 273.15;

  if (tempHome > 30) {
        turnOffLED(autoHeatingLedPin);  /* Turn the 'autoHeating' off */
      } else {
        turnOnLED(autoHeatingLedPin);
      }

  delay(2000);
}

void mainApplication(char *commandSpoken)
{
  if (strstr(VoiceRecognition.getLastCommand(), lights))
  {
    if (strstr(VoiceRecognition.getLastCommand(), on)) {
        MusicPlayer.setVolume(5);
       MusicPlayer.play();
      turnOnLED(lightLedPin); /* Turn the 'lights' on */
    }
    else if (strstr(VoiceRecognition.getLastCommand(), off)) {
      turnOffLED(lightLedPin);  /* Turn the 'lights' off */
    }
    else if (strstr(VoiceRecognition.getLastCommand(), "change")) {
      TextToSpeech.say("time in cairo is");
      if (digitalRead(lightLedPin)) {
        turnOffLED(lightLedPin);  /* Turn the 'lights' off */
      } else {
        turnOnLED(lightLedPin);
      }
    }
  }

  if (strstr(VoiceRecognition.getLastCommand(), heating))
  {
    if (strstr(VoiceRecognition.getLastCommand(), on)) {
      turnOnLED(heatingLedPin); /* Turn the 'heating' on */
    }
    else if (strstr(VoiceRecognition.getLastCommand(), off)) {
      turnOffLED(heatingLedPin);  /* Turn the 'heating' off */
    }
    else if (strstr(VoiceRecognition.getLastCommand(), "change")) {
      if (digitalRead(heatingLedPin)) {
        turnOffLED(heatingLedPin);
      } else {
        turnOnLED(heatingLedPin);
      }
    }
  }
}

void turnOnLED(int pin) {
  digitalWrite(pin, HIGH);
}

void turnOffLED(int pin) {
  digitalWrite(pin, LOW);
}
