package de.depascaldc.management.rest;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
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
