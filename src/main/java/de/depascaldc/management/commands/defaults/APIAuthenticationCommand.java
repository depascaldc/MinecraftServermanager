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
