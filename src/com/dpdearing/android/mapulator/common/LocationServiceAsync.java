/**
 * Copyright (c) 2010 David Dearing
 */

package com.dpdearing.android.mapulator.common;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LocationService</code>.
 */
public interface LocationServiceAsync {
   void setPort(int port, AsyncCallback<Void> callback);

   void setLocation(double latitude, double longitude,
         AsyncCallback<Void> callback);
}
