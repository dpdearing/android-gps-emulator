/**
 * Copyright (c) 2010-17 David Dearing
 */

package com.dpdearing.sandbox.gpsemulator.client;

import com.dpdearing.sandbox.gpsemulator.common.LocationServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GpsEmulator implements EntryPoint, ClickMapHandler {
   /**
    * The default emulator hostname port
    */
   static private final String DEFAULT_HOST = "localhost";
   static private final int DEFAULT_PORT = 5554;

   static private final String SUCCESS_STYLE = "success";
   static private final String ERROR_STYLE = "error";

   /**
    * Create a remote service proxy to talk to the server-side Location service.
    */
   private final LocationServiceAsync _service =
         LocationServiceAsync.Util.getInstance();

   /**
    * The map and current marker shown
    */
   private MapWidget _map;
   private Marker _currentMarker = null;

   /**
    * The textboxes, button, and info label for configuring the host and port.
    */
   private TextBox _hostname;
   private TextBox _port;
   private Button _button;
   private Label _info;
   
   /**
    * This is the entry point method.
    */
   public void onModuleLoad() {
      GWT.setUncaughtExceptionHandler(
            new GWT.UncaughtExceptionHandler() {
               public void onUncaughtException(Throwable t) {
                  GWT.log("Client-side uncaught exception", t);
               }
           }
      );

      initializeUI();
      loadMapApi();
   }

   /**
    * Retrieve the Google Maps API key and load the API
    */
   private void loadMapApi() {
      // retrieve the Google Maps API Key and then initialize the UI
      _service.getApiKey(new AsyncCallback<String>() {

         @Override
         public void onFailure(Throwable caught) {
            setErrorMessage(caught.getLocalizedMessage());
         }

         @Override
         public void onSuccess(String apiKey) {
            loadMapApi(apiKey);
         }
      });
   }

   /**
    * Load the GWT Google Maps API using the provided API key.
    *
    * @param apiKey
    */
   private void loadMapApi(String apiKey) {
      final StringBuilder otherParameters = new StringBuilder();
      if (apiKey != null) {
         otherParameters.append("key=");
         otherParameters.append(apiKey);
      }
      GWT.log("LoadApi otherParameters: "+otherParameters.toString());

      LoadApi.go(this::initializeMap, null, false, otherParameters.toString());
   }
   
   /**
    * Initialize the Emulator UI
    */
   private void initializeUI() {
      // Create textboxes and set default hostname and port
      _hostname = new TextBox();
      _hostname.setText(DEFAULT_HOST);
      _hostname.getElement().setPropertyString("placeholder", "hostname");
      _port = new TextBox();
      _port.setText(String.valueOf(DEFAULT_PORT));
      _port.getElement().setPropertyString("placeholder", "port");

      // Create button to connect
      _button = new Button("Connect");
      // Create the info/status label, initially not visible
      _info = new InlineLabel();
      _info.setVisible(false);

      // register the button action
      _button.addClickHandler(new ClickHandler() {
         public void onClick(final ClickEvent event) {
            final String hostname = _hostname.getText();
            final int port = Integer.valueOf(_port.getText());
            new PortAsyncCallback(hostname, port).execute();
         }
      });
      
      // Create panel for textbox, button and info label
      final FlowPanel div = new FlowPanel();
      div.setStylePrimaryName("emulator-controls");
      _hostname.setStyleName("emulator-hostname");
      _port.setStyleName("emulator-port");
      _button.setStyleName("emulator-connect");
      _info.setStyleName("emulator-info");
      div.add(_hostname);
      div.add(_port);
      div.add(_button);
      div.add(_info);

      // add the controls before the map so that the div doesn't cover the map
      RootLayoutPanel.get().add(div);
   }

   /**
    * Initialize the Map widget and default zoom/position
    */
   private void initializeMap() {
      // Create a map centered on Cawker City, KS USA
      final MapOptions opts = MapOptions.newInstance();
      if (opts == null) {
         GWT.log("MapOptions was null");
      }
      final LatLng center = LatLng.newInstance(30.0, 0.00);
      opts.setCenter(center);
      opts.setZoom(2);

      _map = new MapWidget(opts);

      // Register map click handler
      _map.addClickHandler(this);
      RootLayoutPanel.get().add(_map);
   }

      /**
       * Handle a map click event
       */
   public void onEvent(ClickMapEvent clickMapEvent) {
      final LatLng point = clickMapEvent.getMouseEvent().getLatLng();

      // set the location
      new GeoFixAsyncCallback(point.getLatitude(), point.getLongitude()).execute();
   }

   /**
    * Create and add a new marker
    *
    * @param latitude
    * @param longitude
    * @return
    */
   private Marker addMarker(double latitude, double longitude) {
      return addMarker(LatLng.newInstance(latitude, longitude));
   }

   /**
    * Create and add a new marker
    *
    * @param point
    * @return
    */
   private Marker addMarker(LatLng point) {
      MarkerOptions opts = MarkerOptions.newInstance();
      opts.setMap(_map);
      opts.setPosition(point);
      return Marker.newInstance(opts);
   }

   /**
    * Clear the last-placed marker
    */
   private void clearMarker() {
      if (_currentMarker != null) {
         _currentMarker.clear();
      }
   }

   private void setSuccessMessage(String message) {
      _info.addStyleDependentName(SUCCESS_STYLE);
      _info.setText(message);
      _info.setVisible(true);
   }

   private void setErrorMessage(String message) {
      _info.addStyleDependentName(ERROR_STYLE);
      _info.setText(message);
      _info.setVisible(true);
      flash();
   }

   private void clearMessage() {
      _info.setText("");
      _info.removeStyleDependentName(ERROR_STYLE);
      _info.removeStyleDependentName(SUCCESS_STYLE);
      _info.setVisible(false);
      resetFlash();
   }

   private void flash() {
      getFlashElement().addClassName("active");
   }

   private void resetFlash() {
      getFlashElement().removeClassName("active");
   }

   private Element getFlashElement() {
      return Document.get().getElementById("error-flash");
   }

   /**
    * Asynchronous callback for setting the telnet port.
    */
   private class PortAsyncCallback implements AsyncCallback<Void>, Command {
      private final String _hostname;
      private final int _port;
      
      /**
       * Constructor
       *
       * @param hostname the hostname
       * @param port the port
       */
      public PortAsyncCallback(final String hostname, final int port) {
         _hostname = hostname;
         _port = port;
      }
      
      /**
       * Success!
       * 
       * @param result void
       */
      public void onSuccess(final Void result) {
         setSuccessMessage("Connected to " + _hostname + ":" + _port);
      }

      /**
       * Oh no!
       */
      public void onFailure(final Throwable caught) {
         setErrorMessage("Error connecting to " + _hostname + ":" + _port + " - "
               + caught.getLocalizedMessage());
      }
      
      /**
       * Execute service method
       */
      public void execute() {
         // clear the marker and message when (re-)connecting
         clearMarker();
         clearMessage();
         _service.connect(_hostname, _port, this);
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
         // add the new marker
         _currentMarker = addMarker(_latitude, _longitude);
         setSuccessMessage("geo fix " + _longitude + " " + _latitude);
      }
      
      /**
       * Oh no!
       */
      public void onFailure(final Throwable caught) {
         setErrorMessage("Error setting location: " + caught.getLocalizedMessage());
      }

      /**
       * Execute service method
       */
      public void execute() {
         // clear the marker and message before setting new location
         clearMarker();
         clearMessage();
         _service.setLocation(_latitude, _longitude, this);
      }
   }
}
