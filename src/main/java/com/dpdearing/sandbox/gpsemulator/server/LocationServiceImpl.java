/**
 * Copyright (c) 2010 David Dearing
 */

package com.dpdearing.sandbox.gpsemulator.server;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import com.dpdearing.sandbox.gpsemulator.common.LocationService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mud.telnet.TelnetWrapper;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LocationServiceImpl extends RemoteServiceServlet implements
      LocationService {
   /**
    * The telnet wrapper.
    */
   private TelnetWrapper _telnet;

   /**
    * Constructor.
    */
   public LocationServiceImpl() {
      _telnet = new TelnetWrapper();
   }
   
   /**
    * {@inheritDoc}
    */
   public void setPort(final int port) throws IOException {
      // disconnect from any previous connection
      _telnet.disconnect();
      // reconnect to the new port
      _telnet.connect("localhost", port);
   }
   
   /**
    * {@inheritDoc}
    */
   /*public void setLocation(final double latitude, final double longitude)
         throws IOException {
      // telnet the geo fix location: longitude first!
      _telnet.send("geo fix " + longitude + " " + latitude);
   }*/

   public void setLocation(final double latitude, final double longitude)
           throws IOException {
      Calendar c = Calendar.getInstance();
      String format = "geo nmea $GPGGA,%1$02d%2$02d%3$02d.%4$03d,%5$03d%6$09.6f,%7$c,%8$03d%9$09.6f,%10$c,1,10,0.0,20.0,0,0.0,0,0.0,0000";
      //String format = "geo nmea $GPRMC,A,%1$02d%2$02d%3$02d.%4$03d,%5$03d%6$09.6f,%7$c,%8$03d%9$09.6f,%10$c,000.5,054.7,191194,020.3,0000";
      //String format = "geo nmea $GPRMC,A,%1$02d%2$02d%3$02d.%4$03d,%5$03d%6$09.6f,%7$c,%8$03d%9$09.6f,%10$c,000.0,360.0,131115,011.3,0000";

      double absLong = Math.abs(longitude);
      int longDegree = (int) Math.floor(absLong);
      char longDirection = 'E';
      if (longitude < 0D) {
         longDirection = 'W';
      }
      double longMinute = (absLong - Math.floor(absLong)) * 60.0D;

      double absLat = Math.abs(latitude);
      int latDegree = (int) Math.floor(absLat);
      char latDirection = 'N';
      if (latitude < 0D) {
         latDirection = 'S';
      }
      double latMinute = (absLat - Math.floor(absLat)) * 60.0D;

      String command = String.format(Locale.US, format,
              c.get(Calendar.HOUR_OF_DAY),
              c.get(Calendar.MINUTE),
              c.get(Calendar.SECOND),
              c.get(Calendar.MILLISECOND),
              latDegree,
              latMinute,
              latDirection,
              longDegree,
              longMinute,
              longDirection);
      System.out.println(command);
      _telnet.send(command);
   }
}
