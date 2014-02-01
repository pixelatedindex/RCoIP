RCoIP (RC over IP)
=====

###Overview
A WiFi-based remote control system that can be used to control any PWM-based device. The transmitter is modeled as an Android application, while the receiver is a WiFi module that accepts UDP packets. 

###Current Implementation
The project was implemented as a way to control a quadcopter over WiFi, rather than the traditional Radio Control (RC) method. The Android application is a simple view of dual joysticks that mimic a flight transmitter. The application is responsible for generating a data string that is sent over WiFi at a predetermined rate (since this varies from build to build, some testing is required to determine this rate). 

The data string is a concatenation of 4 substrings that represent the values of right pan, right tilt, left tilt and left pan, each corresponding to channels 1 through 4 respectively. Each substring is a concatenation of the letter 'X', the channel number, and a hex value between 00 and FF. 

A sample data string would be 'X17fX2DfX36eX47f', which decodes to 7F for Channel 1 (right pan), DF for Channel 2 (right tilt),  6E for Channel 3 (left tilt) and finally 7F again for left pan.

We limit the hex range to 00..FF because the joysticks have their X and Y axes divided into 256 steps, with origin at (128, 128) for both joysticks. This ensures that any values that decode to outside the range can be ignored safely. In case of multiple bad values being decoded, the receiver reverts to a "last known good value". It then times out and slowly decreases powers to the motors to ensure that the copter does not fall like a rock. 
