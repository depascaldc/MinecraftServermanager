/**
 *   Copyright © 2020 | depascaldc | Discord: [depascaldc]#4093
 *   __  __                                                   _   
 *  |  \/  | __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_ 
 *  | |\/| |/ _` | '_ \ / _` |/ _` |/ _ \ '_ ` _ \ / _ \ '_ \| __|
 *  | |  | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_ 
 *  |_|  |_|\__,_|_| |_|\__,_|\__, |\___|_| |_| |_|\___|_| |_|\__|
 *                           |___/                               
 * 
 *   Copyright © 2020 | depascaldc | Discord: [depascaldc]#4093
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 *   Copyright © 2020 | depascaldc | Discord: [depascaldc]#4093
 *   
 */
package de.depascaldc.management.rest;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;

import de.depascaldc.management.main.ServerManager;

public class RESTApi extends Thread {

	private HttpServer server;

	private ServerConfiguration config;

	private APIConfigurationHandler configHandler;

	private static String AUTH_KEY;
	private static String AUTHORIZATION;

	public static boolean SSL_ENABLED = false;
	private static String cert_pem;
	private static String privkey_pem;

	public SSLContextConfigurator sslCon;

	@Override
	public void run() {
		AUTH_KEY = ServerManager.getProperties().getProperty("api-key");
		AUTHORIZATION = ServerManager.getProperties().getProperty("api-auth");
		if (ServerManager.getProperties().getProperty("ssl-enabled").equalsIgnoreCase("enabled")
				|| ServerManager.getProperties().getProperty("ssl-enabled").equalsIgnoreCase("on")
				|| ServerManager.getProperties().getProperty("ssl-enabled").equalsIgnoreCase("true")) {
			SSL_ENABLED = true;
			cert_pem = ServerManager.getProperties().getProperty("cert-path");
			privkey_pem = ServerManager.getProperties().getProperty("privkey-path");
		}
		try {
			server = createHttpsServer();
			server.start();
			ServerManager.runAsync(new Runnable() {
				@Override
				public void run() {
					ServerManager.getLogger().debug("Disabled HttpServer debug Logging...");
					Enumeration<String> loggers = LogManager.getLogManager().getLoggerNames();
					while (loggers.hasMoreElements()) {
						String loggerName = loggers.nextElement();
						if (loggerName.toLowerCase().contains("glassfish")) {
							Logger logger = LogManager.getLogManager().getLogger(loggerName);
							logger.setLevel(Level.OFF);
						} else if (loggerName.toLowerCase().contains("grizzly")) {
							Logger logger = LogManager.getLogManager().getLogger(loggerName);
							logger.setLevel(Level.OFF);
						}
					}
				}
			});
		} catch (Exception e) {
			ServerManager.getLogger().error("Could not create REST API Server...", e);
		}
	}

	private HttpServer createHttpsServer() {
		HttpServer server = new HttpServer();
		this.sslCon = new SSLContextConfigurator();
		if (SSL_ENABLED) {
			sslCon.setKeyStoreFile(cert_pem);
			sslCon.setKeyStorePass(privkey_pem);
		}
		server.addListener(new APIListener(ServerManager.getProperties().getProperty("api-ip"),
				Integer.valueOf(ServerManager.getProperties().getProperty("api-port")), this));
		server.removeListener("grizzly");
		this.config = server.getServerConfiguration();
		configHandler = new APIConfigurationHandler(AUTH_KEY, AUTHORIZATION);
		configHandler.setConfigurationOptions(config);

		String ServerKind = SSL_ENABLED ? "HTTPS" : "HTTP";
		ServerManager.getLogger()
				.info("API Server is Listening to: " + ServerManager.getProperties().getProperty("api-ip") + ":"
						+ ServerManager.getProperties().getProperty("api-port") + " ServerART: " + ServerKind);
		return server;
	}

	public HttpServer getServer() {
		return server;
	}

	public APIConfigurationHandler getConfigHandler() {
		return configHandler;
	}

}
