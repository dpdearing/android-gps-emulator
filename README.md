android-gps-emulator
====================

android-gps-emulator is a GPS location emulator for changing/setting/simulating the GPS location of the Android emulator through a simple map-based interface, using the geo fix command.

Using GWT and Google Maps in combination with [JTA (Java telnet support)](http://javassh.org/space/start), this is a simple tool for emulating the geospatial (GPS) location of your local android emulator using the telnet geo fix command.

I found it cumbersome to use command line telnet and manually enter specific location coordinates when testing Android applications in the emulator, therefore I created this tool.

Getting Started
===============

1. Clone the repository
2. From the command line execute **`mvn gwt:run`**
3. Click `Launch Default Browser` from the GWT Development Mode


![android-gps-emulator](http://dpdearing.github.io/android-gps-emulator/img/android-gps-emulator.png)

