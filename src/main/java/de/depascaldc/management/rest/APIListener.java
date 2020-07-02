package de.depascaldc.management.rest;

import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;

import de.depascaldc.management.main.ServerManager;
import de.depascaldc.management.rest.RESTApi;

public class APIListener extends NetworkListener {

	private RESTApi restApi;

	private String apiAuthentication;

	public APIListener(String IP, int port, RESTApi restApi) {
		super("RESTApiListener", IP, port);
		this.restApi = restApi;
		this.apiAuthentication = ServerManager.getProperties().getProperty("api-auth");
		if (RESTApi.SSL_ENABLED) {
			setSecure(true);
			SSLEngineConfigurator ssle = new SSLEngineConfigurator(restApi.sslCon);
			setSSLEngineConfig(ssle);
		} else
			setSecure(false);
		setTraceEnabled(false);
	}

	public String getApiAuthentication() {
		return apiAuthentication;
	}

	public RESTApi getRestApi() {
		return restApi;
	}

}
