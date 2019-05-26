android-gps-emulator
====================

android-gps-emulator is a GPS location emulator for changing/setting/simulating the GPS location of the Android emulator through a simple map-based interface, using the geo fix command.

Using GWT and Google Maps in combination with [JTA (Java telnet support)](http://javassh.org/space/start), this is a simple tool for emulating the geospatial (GPS) location of your local android emulator using the telnet geo fix command.


**NOTE:** You must have a Google account _with billing enabled_ to generate an API key for the Google Maps JavaScript API. 
See Google's documentation at [Get API Key](https://developers.google.com/maps/documentation/javascript/get-api-key)

Quick Start
-----------
1. Download the [latest release](https://github.com/dpdearing/android-gps-emulator/releases/latest)
2. Create a file named **`google_maps_api_key`** containing your API key
3. Run the executable war: **`java -jar android-gps-emulator-0.3.war`**
4. http://localhost:8080/gpsemulator/

![android-gps-emulator](http://dpdearing.github.io/android-gps-emulator/img/android-gps-emulator.png)


Running on a Different Port
---------------------------

android-gps-emulator runs by default on port 8080.  The port can be changed by passing the desired port as a parameter when running the executable war.  For example, to run on port 9001:

&nbsp;&nbsp;&nbsp;**`java -jar android-gps-emulator-0.3.war 9001`**


GWT Hosted Mode (from source)
-----------------------------
1. Clone the repository
2. Create a file named **`google_maps_api_key`** containing your API key
3. From the command line execute **`mvn gwt:run`**
4. Click `Launch Default Browser` from the GWT Development Mode

