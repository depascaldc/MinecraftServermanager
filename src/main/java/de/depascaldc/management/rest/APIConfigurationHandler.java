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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.grizzly.http.Cookie;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.util.HttpStatus;

import de.depascaldc.management.logger.ConsoleColors;
import de.depascaldc.management.main.ServerManager;

public class APIConfigurationHandler {

	private String AUTH_KEY;
	private String AUTHORIZATION;

	public APIConfigurationHandler(String AUTH_KEY, String AUTHORIZATION) {
		this.AUTH_KEY = AUTH_KEY;
		this.AUTHORIZATION = AUTHORIZATION;
	}

	public void setConfigurationOptions(ServerConfiguration config) {
		config.addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response) throws Exception {
				new APIAction(new Runnable() {
					@Override
					public void run() {
						ServerManager.getLogger().warn("API ->> Pinged path /");
					}
				}, new Runnable() {
					@Override
					public void run() {
					}
				});
			}
		}, "/");
		if (ServerManager.getProperties().getProperty("rcon-enabled").equalsIgnoreCase("true")) {
			config.addHttpHandler(new HttpHandler() {
				@Override
				public void service(Request request, Response response) throws Exception {
					// todo session auth and session valid checks
					response.addCookie(
							new Cookie("rcon_session_auth:" + ServerManager.getProperties().getProperty("rcon-port"),
									UUID.randomUUID().toString()));
					InputStream inputStream = ServerManager.class.getResourceAsStream("/rcon/rc.html");
					String html = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
					respondHtmlText(request, response,
							html.replace("%%port%%", ServerManager.getProperties().getProperty("rcon-port"))
									.replace("%%apiport%%", ServerManager.getProperties().getProperty("api-port")));
				}
			}, "/rcon");
		}
		config.addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response) throws Exception {
				if (checkAuthorization(request)) {
					new APIAction(new Runnable() {
						@Override
						public void run() {
							ServerManager.getLogger().warn("API ->> Pinged path \"/ping/\" + AUTH");
						}
					}, new Runnable() {
						@Override
						public void run() {
							try {
								respondHtmlText(request, response, "Not implemented yet.");
							} catch (IOException e) {
							}
						}
					});
				} else {
					sendUnauthorized(response);
				}
			}
		}, "/ping/" + AUTH_KEY);
		config.addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response) throws Exception {
				if (checkAuthorization(request)) {
					new APIAction(new Runnable() {
						@Override
						public void run() {
							ServerManager.getLogger().warn("API ->> Pinged path \"/stop/\" + AUTH");
						}
					}, new Runnable() {
						@Override
						public void run() {
							try {
								respondHtmlText(request, response, "Not implemented yet.");
							} catch (IOException e) {
							}
						}
					});
				} else {
					sendUnauthorized(response);
				}
			}
		}, "/stop/" + AUTH_KEY);
		config.addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response) throws Exception {
				if (checkAuthorization(request)) {
					new APIAction(new Runnable() {
						@Override
						public void run() {
							ServerManager.getLogger().warn("API ->> Pinged path \"/start/\" + AUTH");
						}
					}, new Runnable() {
						@Override
						public void run() {
							try {
								respondHtmlText(request, response, "Not implemented yet.");
							} catch (IOException e) {
							}
						}
					});
				} else {
					sendUnauthorized(response);
				}
			}
		}, "/start/" + AUTH_KEY);
		config.addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response) throws Exception {
				if (checkAuthorization(request)) {
					new APIAction(new Runnable() {
						@Override
						public void run() {
							ServerManager.getLogger().warn("API ->> Pinged path \"/forcestop/\" + AUTH");
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										Thread.sleep(5000);
										ServerManager.getManagedProcess().stopProcess();
									} catch (Exception e) {
									}
								}
							}).start();
						}
					}, new Runnable() {
						@Override
						public void run() {
							try {
								respondHtmlText(request, response, "Stopped Serverprocess...");
							} catch (IOException e) {
							}
						}
					});
				} else {
					sendUnauthorized(response);
				}
			}
		}, "/forcestop/" + AUTH_KEY);
		config.addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response) throws Exception {
				if (checkAuthorization(request)) {
					new APIAction(new Runnable() {
						@Override
						public void run() {
							ServerManager.getLogger().warn("API ->> Pinged path \"/kill/\" + AUTH");
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										Thread.sleep(5000);
										System.exit(0);
									} catch (Exception e) {
									}
								}
							}).start();
						}
					}, new Runnable() {
						@Override
						public void run() {
							try {
								respondHtmlText(request, response, "Process Killed...");
							} catch (IOException e) {
							}
						}
					});
				} else {
					sendUnauthorized(response);
				}
			}
		}, "/kill/" + AUTH_KEY);
		config.addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response) throws Exception {
				if (checkAuthorization(request)) {
					try {
						String cinQ = StringUtils.split(request.getQueryString(), "=")[0];
						String command = StringUtils.split(request.getQueryString(), "=")[1];
						if (cinQ == null || command == null) {
							response.setStatus(HttpStatus.CONFLICT_409);
							response.setContentType("application/json");
							String JSONanswer = "{\n"
									+ "    \"error\": \"You ust specify a query ?cmd/command=command args here\"\n"
									+ "}";
							response.setContentLength(JSONanswer.length());
							response.getWriter().write(JSONanswer);
							return;
						}
						if (!cinQ.equalsIgnoreCase("command") && !cinQ.equalsIgnoreCase("cmd")) {
							response.setStatus(HttpStatus.CONFLICT_409);
							response.setContentType("application/json");
							String JSONanswer = "{\n"
									+ "    \"error\": \"You ust specify a query ?cmd/command=command args here\"\n"
									+ "}";
							response.setContentLength(JSONanswer.length());
							response.getWriter().write(JSONanswer);
							return;
						}
						ServerManager.getManagedProcess().sendCommand(command);
						ServerManager.getLogger().info("RAN COMMAND PER API: " + ConsoleColors.RED + command);
						respondHtmlText(request, response, "OK EXECUTED");
					} catch (Exception e) {
						response.setStatus(HttpStatus.CONFLICT_409);
						response.setContentType("application/json");
						String JSONanswer = "{\n" + "    \"error\": \"Error while executing Command\"\n" + "}";
						response.setContentLength(JSONanswer.length());
						response.getWriter().write(JSONanswer);
					}
				} else {
					sendUnauthorized(response);
				}
			}
		}, "/runCommand/" + AUTH_KEY);
	}

	private boolean checkAuthorization(Request request) {
		if (request.getAuthorization() == null) {
			return false;
		}
		return request.getAuthorization().equals(AUTHORIZATION);
	}

	private void sendUnauthorized(Response response) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED_401);
		response.setContentType("application/json");
		String JSONanswer = "{\n" + "    \"error\": \"unauthorized\"\n" + "}";
		response.setContentLength(JSONanswer.length());
		response.getWriter().write(JSONanswer);
	}

	private boolean respondHtmlText(Request request, Response response, String resp) throws IOException {
		response.setContentType("text/html");
		response.setContentLength(resp.length());
		response.getWriter().write(resp);
		return true;
	}

}
