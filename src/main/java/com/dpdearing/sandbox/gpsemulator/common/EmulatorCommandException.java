/**
 * Copyright (c) 2017 David Dearing
 */

package com.dpdearing.sandbox.gpsemulator.common;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A GWT-IsSerializable exception for unexpected events when sending a command to the android emulator.
 */
public class EmulatorCommandException extends Exception implements IsSerializable {
    public EmulatorCommandException() {
        super();
    }

    public EmulatorCommandException(String message) {
        super(message);
    }
}
