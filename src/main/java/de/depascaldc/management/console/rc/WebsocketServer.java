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
package de.depascaldc.management.console.rc;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler.Simple;

import de.depascaldc.management.logger.ConsoleColors;
import de.depascaldc.management.main.ServerManager;

public class WebsocketServer {
	private boolean running;
	static String address;
	static int port;

	private Server server;

	public WebsocketServer() {
		running = false;
	}

	public void runServer() {
		if (!isRunning()) {
			address = "0.0.0.0";
			port = Integer.valueOf(ServerManager.getProperties().getProperty("rcon-port"));
			if (port < 0) {
				port = 2300;
			}
			ServerManager.runAsync(new Runnable() {
				@Override
				public void run() {
					try {
						server = new Server(port);
						server.setHandler(new Simple(MessagingEndpoint.class));
						server.start();
						ServerManager.getLogger().out("Endpoint_Push setup done... running on --> "
								+ ConsoleColors.BLUE_BOLD + "ws://" + address + ":" + port + "/");
					} catch (Exception e) {
						ServerManager.getLogger().error("SOCKET SERVER COULD NOT BE INITIALIZED...");
					}
				}
			});
		} else {
			stopServer();
		}
	}

	public void stopServer() {
		running = false;
		try {
			server.stop();
		} catch (Exception e) {
		}
	}

	public boolean isRunning() {
		return running;
	}

}
