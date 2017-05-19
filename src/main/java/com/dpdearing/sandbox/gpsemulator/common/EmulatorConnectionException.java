/**
 * Copyright (c) 2017 David Dearing
 */

package com.dpdearing.sandbox.gpsemulator.common;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A GWT-IsSerializable exception for unexpected events when making a connection to the android emulator.
 */
public class EmulatorConnectionException extends Exception implements IsSerializable {
    public EmulatorConnectionException() {
        super();
    }

    public EmulatorConnectionException(String message) {
        super(message);
    }
}
