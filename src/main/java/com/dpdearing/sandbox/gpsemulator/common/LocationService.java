/**
 * Copyright (c) 2010-17 David Dearing
 */

package com.dpdearing.sandbox.gpsemulator.common;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("location")
public interface LocationService extends RemoteService {
   /**
    * Connect to the hostname and port
    *
    * @param hostname
    *           the hostname
    * @param port
    *           the port number
    * @throws EmulatorConnectionException
    */
   void connect(String hostname, int port) throws EmulatorConnectionException;

   /**
    * Set the geospatial location
    * 
    * @param latitude
    *           latitude
    * @param longitude
    *           longitude
    * @throws EmulatorCommandException
    */
   void setLocation(double latitude, double longitude) throws EmulatorCommandException;

   /**
    * @return the Google Maps API Key
    * @throws EmulatorCommandException
    */
   String getApiKey() throws EmulatorCommandException;
}
