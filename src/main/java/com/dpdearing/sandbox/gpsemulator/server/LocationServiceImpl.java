/**
 * Copyright (c) 2010-17 David Dearing
 */

package com.dpdearing.sandbox.gpsemulator.server;

import java.io.IOException;

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
   public void setLocation(final double latitude, final double longitude)
         throws IOException {
      // telnet the geo fix location: longitude first!
      _telnet.send("geo fix " + longitude + " " + latitude);
   }
}
