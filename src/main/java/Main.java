/**
 * Copyright (c) 2014 David Dearing
 */

import java.net.URL;
import java.security.ProtectionDomain;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
 
/**
 * Embedded Jetty xecutable WAR main entry point.
 * 
 * See http://uguptablog.blogspot.com/2012/09/embedded-jetty-executable-war-with.html
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		Server server = new Server();
		
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(8080);
		server.addConnector(connector);
		
		ProtectionDomain domain = Main.class.getProtectionDomain();
		URL location = domain.getCodeSource().getLocation();
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setWar(location.toExternalForm());
		server.setHandler(webapp);
		
		server.start();
		server.join();
	}
}
