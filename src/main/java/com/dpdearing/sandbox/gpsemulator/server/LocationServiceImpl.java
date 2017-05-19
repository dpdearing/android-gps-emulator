/**
 * Copyright (c) 2010-17 David Dearing
 */

package com.dpdearing.sandbox.gpsemulator.server;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dpdearing.sandbox.gpsemulator.common.EmulatorCommandException;
import com.dpdearing.sandbox.gpsemulator.common.EmulatorConnectionException;
import com.dpdearing.sandbox.gpsemulator.common.LocationService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mud.telnet.TelnetWrapper;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LocationServiceImpl extends RemoteServiceServlet implements LocationService {

   private static Logger logger =
           Logger.getLogger(LocationServiceImpl.class.getName());

   /**
    * The telnet wrapper.
    */
   final private TelnetWrapper _telnet;

   private Boolean _isTelnetActive = false;

   /**
    * The emulator authentication token
    */
   private String _emulator_console_auth_token = null;

   /**
    * Constructor.
    */
   public LocationServiceImpl() {
      _telnet = new TelnetWrapper();

      //TODO does this work on all platforms?
      final String home = System.getProperty("user.home");
      final File authTokenFile = new File(home, ".emulator_console_auth_token");
      try {
         // read the entire contents of the
         _emulator_console_auth_token = new Scanner(authTokenFile).useDelimiter("\\Z").next();
         logger.info("Loaded emulator_console_auth_token " + _emulator_console_auth_token);
      } catch (IOException ioe) {
         logger.log(Level.SEVERE,
                 "Failed to read the emulator_console_auth_token from " +
                         authTokenFile.getAbsolutePath(),
                 ioe);
      }
   }
   
   /**
    * {@inheritDoc}
    */
   public void connect(final String hostname, final int port)
         throws EmulatorConnectionException {
      // disconnect from any previous connection
      try {
         _telnet.disconnect();
      } catch (IOException ioe) {
         throw new EmulatorConnectionException(ioe.getMessage());
      }
      _isTelnetActive = false;

      // connect to the specified host
      logger.info("Connecting to emulator at "+ hostname + ":" + port);

      try {
         _telnet.connect(hostname, port);
         _isTelnetActive = true;
      } catch (IOException ioe) {
         throw new EmulatorConnectionException(ioe.getMessage());
      }

      if (_emulator_console_auth_token == null) {
         logger.info("No emulator_console_auth_token; skipping authentication");
      } else {
         logger.info("Authenticating with emulator_console_auth_token: " + _emulator_console_auth_token);
         try {
            _telnet.send("auth " + _emulator_console_auth_token);
         } catch (IOException ioe) {
            throw new EmulatorConnectionException(ioe.getMessage());
         }
      }
   }
   
   /**
    * {@inheritDoc}
    */
   public void setLocation(final double latitude, final double longitude)
         throws EmulatorCommandException {
      if (_isTelnetActive) {
         try {
            // telnet the geo fix location: longitude first!
            String geo = "geo fix " + longitude + " " + latitude;
            logger.info("Sending command: " + geo);
            _telnet.send(geo);
         } catch (IOException ioe) {
            throw new EmulatorCommandException(ioe.getMessage());
         } catch (Exception npe) {
            throw new EmulatorCommandException("Unexpected error within telnet connection.");
         }
      }
      else {
         // throw an exception or client will think that setLocation() was a success
         throw new EmulatorCommandException("Not connected to the emulator.");
      }
   }
}
