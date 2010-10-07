/**
 * Copyright (c) 2010 David Dearing
 */

package com.dpdearing.sandbox.gpsemulator.client;

import com.dpdearing.sandbox.gpsemulator.common.LocationServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GpsEmulator implements EntryPoint, MapClickHandler {
   /**
    * The default emulator port
    */
   static private final int DEFAULT_PORT = 5554;
   static private final String SUCCESS_STYLE = "success";
   static private final String ERROR_STYLE = "error";
   
   /**
    * Create a remote service proxy to talk to the server-side Location service.
    */
   private final LocationServiceAsync _service =
         LocationServiceAsync.Util.getInstance();

   /**
    * The last marker placed on the map
    */
   private Marker _lastMarker = null;
   
   /**
    * The textbox, button, and info label for configuring the port.
    */
   private TextBox _text;
   private Button _button;
   private Label _info;
   
   /**
    * This is the entry point method.
    */
   public void onModuleLoad() {
      /*
       * Asynchronously loads the Maps API.
       *
       * The first parameter should be a valid Maps API Key to deploy this
       * application on a public server, but a blank key will work for an
       * application served from localhost.
      */
      Maps.loadMapsApi("", "2", false, new Runnable() {
         public void run() {
            // initialize the UI
            buildUi();
            // initialize the default port
            new PortAsyncCallback(DEFAULT_PORT).execute();
         }
      });
   }
   
   /**
    * Initialize the UI
    */
   private void buildUi() {
      // Create a textbox and set default port
      _text = new TextBox();
      _text.setText(String.valueOf(DEFAULT_PORT));
      // Create button to change default port 
      _button = new Button("Change Emulator Port");
      // Create the info/status label
      _info = new InlineLabel();
      
      // enable button when port number is changed
      _text.addChangeHandler(new ChangeHandler() {
         public void onChange(final ChangeEvent event) {
            _button.setEnabled(true);
         }
      });
      
      // register the button action
      _button.addClickHandler(new ClickHandler() {
         public void onClick(final ClickEvent event) {
            final int port = Integer.valueOf(_text.getText());
            new PortAsyncCallback(port).execute();
         }
      });
      
      
      // Open a map centered on Cawker City, KS USA
      final LatLng cawkerCity = LatLng.newInstance(39.509, -98.434);
      final MapWidget map = new MapWidget(cawkerCity, 2);
      map.setSize("100%", "100%");
      
      // Workaround for bug with click handler & setUItoDefaults() - see issue 260
      final MapUIOptions opts = map.getDefaultUI();
      opts.setDoubleClick(false);
      map.setUI(opts);
      
      // Register map click handler
      map.addMapClickHandler(this);
      
      
      // Create panel for textbox, button and info label
      final FlowPanel div = new FlowPanel();
      div.add(_text);
      div.add(_button);
      div.add(_info);
      // Dock the map
      final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
      dock.addNorth(map, 500);
      
      // Add the map dock to the div panel
      div.add(dock);
      RootLayoutPanel.get().add(div);
   }

   /**
    * Handle a map click event
    */
   public void onClick(final MapClickEvent e) {
      final MapWidget sender = e.getSender();
      final Overlay overlay = e.getOverlay();
      final LatLng point = e.getLatLng();

      if (overlay != null && overlay instanceof Marker) {
         // clear a marker that has been clicked on
         sender.removeOverlay(overlay);
      } else {
         // clear the last-placed marker ...
         if (_lastMarker != null && _lastMarker instanceof Marker) {
           sender.removeOverlay(_lastMarker);
           _lastMarker = null;
         }
         
         // ... and add the new marker
         final Marker mark = new Marker(point);
         _lastMarker = mark;
         sender.addOverlay(mark);
         
         // set the location
         new GeoFixAsyncCallback(point.getLatitude(), point.getLongitude()).execute();
      }
   }
   
   /**
    * Asynchronous callback for setting the telnet port.
    */
   private class PortAsyncCallback implements AsyncCallback<Void>, Command {
      private final int _port;
      
      /**
       * Constructor
       * 
       * @param port the port
       */
      public PortAsyncCallback(final int port) {
         _port = port;
      }
      
      /**
       * Success!
       * 
       * @param result void
       */
      public void onSuccess(final Void result) {
         _button.setEnabled(false);
         _info.addStyleDependentName(SUCCESS_STYLE);
         _info.setText("Connected to port " + _port);
      }

      /**
       * Oh no!
       */
      public void onFailure(final Throwable caught) {
         _button.setEnabled(true);
         _info.addStyleDependentName(ERROR_STYLE);
         _info.setText("Error making connection on port " + _port + ": "
               + caught.getLocalizedMessage());
      }
      
      /**
       * Execute service method
       */
      public void execute() {
         // clear info message
         _info.setText("");
         _info.removeStyleDependentName(ERROR_STYLE);
         _info.removeStyleDependentName(SUCCESS_STYLE);
         
         _service.setPort(_port, this);
      }
   }
   
   /**
    * Asynchronous callback for changing the emulator's location
    */
   private class GeoFixAsyncCallback implements AsyncCallback<Void>, Command {
      private final double _latitude;
      private final double _longitude;

      /**
       * Constructor.
       * 
       * @param latitude
       *           latitude
       * @param longitude
       *           longitude
       */
      public GeoFixAsyncCallback(final double latitude, final double longitude) {
         _latitude = latitude;
         _longitude = longitude;
      }
      
      /**
       * Success!
       * 
       * @param result void
       */
      public void onSuccess(final Void result) {
         _info.addStyleDependentName(SUCCESS_STYLE);
         _info.setText("geo fix " + _longitude + " " + _latitude);
      }
      
      /**
       * Oh no!
       */
      public void onFailure(final Throwable caught) {
         _info.addStyleDependentName(ERROR_STYLE);
         _info.setText("Error setting location: " + caught.getLocalizedMessage());
      }

      /**
       * Execute service method
       */
      public void execute() {
         // clear info message
         _info.setText("");
         _info.removeStyleDependentName(ERROR_STYLE);
         _info.removeStyleDependentName(SUCCESS_STYLE);

         _service.setLocation(_latitude, _longitude, this);
      }
   }
}
