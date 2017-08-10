android-gps-emulator
====================

android-gps-emulator is a GPS location emulator for changing/setting/simulating the GPS location of the Android emulator through a simple map-based interface, using the geo fix command.

Using GWT and Google Maps in combination with [JTA (Java telnet support)](http://javassh.org/space/start), this is a simple tool for emulating the geospatial (GPS) location of your local android emulator using the telnet geo fix command.

Quick Start
-----------
1. Download the [latest release](https://github.com/dpdearing/android-gps-emulator/releases/latest)
2. Run the executable war: **`java -jar android-gps-emulator-0.2.war`**
3. http://localhost:8080/gpsemulator/

![android-gps-emulator](http://dpdearing.github.io/android-gps-emulator/img/android-gps-emulator.png)


GWT Hosted Mode
---------------
1. Clone the repository
2. From the command line execute **`mvn gwt:run`**
3. Click `Launch Default Browser` from the GWT Development Mode

Switching Port
--------------

android-gps-emulator runs by default on port 8080.

The port can be changed by passing the desired port as a parameter when running the `.war`.

e.g. to run on port 9001:

**`java -jar android-gps-emulator-0.2.war 9001`**
