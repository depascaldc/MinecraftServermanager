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
package de.depascaldc.management.commands.defaults;

import de.depascaldc.management.commands.ConsoleCommand;
import de.depascaldc.management.logger.ConsoleColors;
import de.depascaldc.management.main.ServerManager;
import de.depascaldc.management.rest.RESTApi;

public class APIAuthenticationCommand extends ConsoleCommand {

	public APIAuthenticationCommand() {
		super("apiauth");
		setAliases("aa");
		setUsage("/aa // /apiauth");
		setDescription("Prints the API authentication (KEY/AUTH)");
	}

	@Override
	public boolean executeCommand(String label, String[] args) {

		ServerManager.getLogger().info("================= API AUTH =================");
		String ad = RESTApi.SSL_ENABLED ? "https://" : "http://";
		ServerManager.getLogger()
				.info("URL: " + ad + ConsoleColors.YELLOW + ServerManager.getProperties().getProperty("api-ip") + ":"
						+ ServerManager.getProperties().getProperty("api-port") + "/");
		ServerManager.getLogger()
				.info("AUTH: " + ConsoleColors.RED_BRIGHT + ServerManager.getProperties().getProperty("api-auth"));
		ServerManager.getLogger()
				.info("KEY: " + ConsoleColors.RED + ServerManager.getProperties().getProperty("api-key"));
		ServerManager.getLogger().info("================= API AUTH =================");

		return false;
	}

}
