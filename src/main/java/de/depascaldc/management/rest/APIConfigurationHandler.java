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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.grizzly.http.Cookie;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.util.HttpStatus;

import de.depascaldc.management.console.JLineTerminalCLI;
import de.depascaldc.management.logger.ConsoleColors;
import de.depascaldc.management.main.ServerManager;
import de.depascaldc.management.util.Utils;

public class APIConfigurationHandler {

	private String AUTH_KEY;
	private String AUTHORIZATION;

	public static Map<String, Long> authenticatedSessions = new HashMap<String, Long>();

	public APIConfigurationHandler(String AUTH_KEY, String AUTHORIZATION) {
		this.AUTH_KEY = AUTH_KEY;
		this.AUTHORIZATION = AUTHORIZATION;
	}

	public void setConfigurationOptions(ServerConfiguration config) {
		if (ServerManager.getProperties().getProperty("rcon-enabled").equalsIgnoreCase("true")) {
			config.addHttpHandler(new HttpHandler() {
				@Override
				public void service(Request request, Response response) throws Exception {
					if (request.getMethod() == Method.GET) {
						if (isSessionValid(request)) {
							ServerManager.getLogger().debug("RCON REDIR: /rcon (valid session provided)");
							response.sendRedirect("/rcon");
							return;
						}
						InputStream inputStream = ServerManager.class.getResourceAsStream("/rcon/rclogin.html");
						String html = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
						respondHtmlText(request, response,
								html.replace("%%port%%", ServerManager.getProperties().getProperty("rcon-port"))
										.replace("%%apiport%%", ServerManager.getProperties().getProperty("api-port")));
						return;
					}
					if (request.getMethod() == Method.POST) {
						ServerManager.getLogger().debug("RCONLOGIN POST");
						boolean matchKey = false;
						boolean matchAuth = false;
						for (String param : request.getParameterMap().keySet()) {
							if (param.equalsIgnoreCase("apikey")) {
								matchKey = ServerManager.getProperties().getProperty("api-key")
										.equals(StringUtils.join(request.getParameterMap().get(param)));
							}
							if (param.equalsIgnoreCase("apiauth")) {
								matchAuth = ServerManager.getProperties().getProperty("api-auth")
										.equals(StringUtils.join(request.getParameterMap().get(param)));
							}
						}
						boolean loginValid = matchAuth && matchKey;
						ServerManager.getLogger().debug("RCON LOGIN, Login Valid? = " + loginValid);
						if (loginValid) {
							response.addCookie(getNewSessionCookie());
							response.sendRedirect("/rcon");
						} else {
							InputStream inputStream = ServerManager.class.getResourceAsStream("/rcon/rclogin.html");
							String html = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
							respondHtmlText(request, response,
									html.replace("%%port%%", ServerManager.getProperties().getProperty("rcon-port"))
											.replace("%%apiport%%",
													ServerManager.getProperties().getProperty("api-port"))
											.replace("<!-- %%on_invalid_login%% -->",
													"<h1>Invalid login... Try again...</h1>"));
						}
						return;
					}
				}
			}, "/rconlogin");
			config.addHttpHandler(new HttpHandler() {
				@Override
				public void service(Request request, Response response) throws Exception {
					if (isSessionValid(request)) {
						InputStream inputStream = ServerManager.class.getResourceAsStream("/rcon/rc.html");
						String html = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
						respondHtmlText(request, response,
								html.replace("%%port%%", ServerManager.getProperties().getProperty("rcon-port"))
										.replace("%%apiport%%", ServerManager.getProperties().getProperty("api-port")));
					} else
						response.sendRedirect("/rconlogin");
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
								respondHtmlText(request, response, "PONG");
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
							ServerManager.getLogger().warn("API ->> Pinged path \"/restart/\" + AUTH");
							ServerManager.runAsync(new Runnable() {
								@Override
								public void run() {
									ServerManager.getManagedProcess().stopProcess();
									try {
										ServerManager.getManagedProcess().initProcess();
									} catch (IOException e) {
									}
								}
							});
						}
					}, new Runnable() {
						@Override
						public void run() {
							try {
								respondHtmlText(request, response, "OK");
							} catch (IOException e) {
							}
						}
					});
				} else {
					sendUnauthorized(response);
				}
			}
		}, "/restart/" + AUTH_KEY);
		config.addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response) throws Exception {
				if (checkAuthorization(request)) {
					new APIAction(new Runnable() {
						@Override
						public void run() {
							ServerManager.getLogger().warn("API ->> Pinged path \"/stop/\" + AUTH");
							try {
								ServerManager.getManagedProcess().stopProcess();
							} catch (Exception e) {
							}
						}
					}, new Runnable() {
						@Override
						public void run() {
							try {
								respondHtmlText(request, response, "OK");
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
							try {
								ServerManager.getManagedProcess().initProcess();
							} catch (IOException e) {
							}
						}
					}, new Runnable() {
						@Override
						public void run() {
							try {
								respondHtmlText(request, response, "OK");
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
								respondHtmlText(request, response, "OK");
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
										ServerManager.runAsync(new Runnable() {
											@Override
											public void run() {
												try {
													Thread.sleep(5000);
												} catch (InterruptedException e) {
													System.exit(0);
												}
												System.exit(0);
											}
										});
									} catch (Exception e) {
									}
								}
							}).start();
						}
					}, new Runnable() {
						@Override
						public void run() {
							try {
								respondHtmlText(request, response, "OK");
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
						ServerManager.getLogger().info("RAN COMMAND PER API: " + ConsoleColors.RED + command);
						JLineTerminalCLI.runCommand(command);
						respondHtmlText(request, response, "OK");
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

	private Cookie getNewSessionCookie() {
		String cookieVal = Utils.generateRandomPasswordString(24);
		long timestampNow = new Date().getTime();
		Cookie cookie = new Cookie("rcon_session_auth_" + ServerManager.getProperties().getProperty("rcon-port"),
				cookieVal);
		cookie.setHttpOnly(false);
		cookie.setMaxAge(3600); // set cookie is valid for 1h
		authenticatedSessions.put(cookieVal, timestampNow);
		return cookie;
	}

	private boolean isSessionValid(Request r) {
		boolean isAutorizedSession = false;
		Cookie cookies[] = r.getCookies();
		if (cookies.length > 0) {
			for (Cookie cookie : cookies) {
				ServerManager.getLogger().debug("COOKIE: " + cookie.getName() + " VAL: " + cookie.getValue());
				if (authenticatedSessions.containsKey(cookie.getValue())) {
					long timestampNow = new Date().getTime();
					if (timestampNow - authenticatedSessions.get(cookie.getValue()) < 3600000) {
						isAutorizedSession = true;
					}
				}
			}
		}
		ServerManager.getLogger().debug("SessionValid Request: " + isAutorizedSession);
		return isAutorizedSession;
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
		response.setStatus(HttpStatus.OK_200);
		response.setContentType("text/html");
		response.setContentLength(resp.length());
		response.getWriter().write(resp);
		return true;
	}

}
