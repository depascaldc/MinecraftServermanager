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

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import de.depascaldc.management.console.JLineTerminalCLI;
import de.depascaldc.management.main.ServerManager;
import de.depascaldc.management.rest.APIConfigurationHandler;

@WebSocket
public class MessagingEndpoint {

	private Session session;
	private static Set<Session> sessions = new CopyOnWriteArraySet<>();

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) throws IOException, EncodeException {
		try {
			ServerManager.getLogger().info("SocketServer.onClose::" + session.getRemoteAddress());
		} catch (Exception e) {
			ServerManager.getLogger().info("SocketServer.onClose... Websocketserver closing");
			broadcast("Closed RCON Websocketserver");
		}
	}

	@OnWebSocketError
	public void onError(Throwable t) {
		t.printStackTrace();
		ServerManager.getLogger().error("SocketServer.onError");
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws IOException, EncodeException {
		ServerManager.getLogger().info("SocketServer.onConnect::" + session.getRemoteAddress());
		sessions.add(session);
	}

	@OnWebSocketMessage
	public void onMessage(String message) throws IOException, EncodeException {
		try {
			ServerManager.getLogger().out("SocketServer.onMessage::Message=" + message);
			if (!message.startsWith("AUTH")) {
				broadcast("Authentication not provided in a Websocket Message... returning...");
				return;
			}
			String args[] = StringUtils.split(message, "|");
			if (args.length < 2)
				return;
			String authString = args[0].split(":")[1];
			if (APIConfigurationHandler.authenticatedSessions.containsKey(authString)) {
				message = args[1];
				if (!message.startsWith("CMD"))
					return;
				try {
					JLineTerminalCLI.runCommand(message.substring(4));
				} catch (Exception e) {
					broadcast("Command could not be sent... Server not running...");
				}
			} else {
				broadcast("Authentication failed in a Websocket Message... returning...");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void broadcast(String message) throws IOException, EncodeException {
		if (sessions.size() > 0) {
			sessions.forEach(endpoint -> {
				synchronized (endpoint) {
					try {
						endpoint.getRemote().sendString(message);
					} catch (Exception e) {
						sessions.remove(endpoint);
					}
				}
			});
		}
	}
}
