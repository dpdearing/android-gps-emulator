/**
 * Copyright (c) 2014 David Dearing
 */

import java.net.URL;
import java.security.ProtectionDomain;
 
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
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

		HttpConfiguration config = new HttpConfiguration();
		ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(config));

		if (args.length > 0) {
			int port = Integer.parseInt(args[0]);
			http.setPort(port);
		}
		server.addConnector(http);
		
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
