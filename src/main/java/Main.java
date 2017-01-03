/**
 * Copyright (c) 2014-17 David Dearing
 */

import java.net.URL;
import java.security.ProtectionDomain;
 
import org.eclipse.jetty.server.Server;
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
//FIXME update embedded jetty code after resolving GWT 2.8.0 update
//		Server server = new Server();
//
//		SelectChannelConnector connector = new SelectChannelConnector();
//		int port = 8080;
//		if (args.length > 0) {
//			port = Integer.parseInt(args[0]);
//		}
//		connector.setPort(port);
//		server.addConnector(connector);
//
//		ProtectionDomain domain = Main.class.getProtectionDomain();
//		URL location = domain.getCodeSource().getLocation();
//		WebAppContext webapp = new WebAppContext();
//		webapp.setContextPath("/");
//		webapp.setWar(location.toExternalForm());
//		server.setHandler(webapp);
//
//		server.start();
//		server.join();
	}
}
