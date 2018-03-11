# Arduino/1Sheeld Instructions
### Basic Setup
Below are the instructions specific to our equipment.
1. Ensure the switch on the 1Sheeld board is set to 5V.
1. Connect the pins on the 1Sheeld and the Arduino
1. Plug the power cable from the Arduino into your laptop/PC.
1. Download the 1Sheeld Application onto your smart phone - see step 3 of [the getting started tutorial.](https://1sheeld.com/tutorials/getting-started/)  
1. The 1Sheeld library is already imported into the project which is in github.

### Coding for the Arduino/1Sheeld
1. Download an IDE for editing your code - They have a web editor but I just downloaded the Arduino IDE - https://www.arduino.cc/en/Main/Software (2nd on the list)
1. Open your IDE and go to File --> Preferences. Update the sketchbook location to the Arduino folder in the Group Project folder you've pulled from github - e.g. C:\Users\Aileen\Documents\College\Group Project\Arduino  This should make all files you create save here (if not just choose this location when saving).
1. Selecting the verify icon will compile the code to ensure it works and the upload button will upload the code to the arduino.
1. The board needs to be switched to SW when uploading software to it and needs to be switched to HW when using the arduino

### Using 1Sheeld Application
1. Open the 1Sheeld application on your iOS or Android smart phone. The application will first scan over bluetooth for your 1Sheeld, it will take a few seconds and the phone will find it. Once it appears on your screen as 1Sheeld #xxxx, you will be required to enter the pairing code (the default pairing code is 1234) and connect to 1Sheeld via bluetooth.
1. Select the shields you would like to use in your Arduino sketch (project) and press on the multiple shields icon at the top right of the app. 

If you encounter any difficulties please refer to https://1sheeld.com/tutorials/getting-started/

### About the example code
1. To try the voice recognition example open the file lights_on_and_off_example.ino and upload it to the Arduino.
1. Once you've uploaded the code you need to select the Voice Recognition Shield to run the code.
1. At the bottom of the examples list you'll find the OneSheeld section which contains sample code for each of the shields available.