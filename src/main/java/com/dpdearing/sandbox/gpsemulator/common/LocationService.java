/**
 * Copyright (c) 2010 David Dearing
 */

package com.dpdearing.sandbox.gpsemulator.common;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("location")
public interface LocationService extends RemoteService {
   /**
    * Set the Telnet port
    * 
    * @param port
    *           the port number
    * @throws IOException
    */
   void setPort(int port) throws IOException;

   /**
    * Set the geospatial location
    * 
    * @param latitude
    *           latitude
    * @param longitude
    *           longitude
    * @throws IOException
    */
   void setLocation(double latitude, double longitude) throws IOException;
}
