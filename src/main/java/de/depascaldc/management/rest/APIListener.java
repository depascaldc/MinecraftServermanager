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
