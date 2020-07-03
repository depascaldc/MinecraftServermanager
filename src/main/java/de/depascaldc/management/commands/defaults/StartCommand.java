package de.depascaldc.management.commands.defaults;

import de.depascaldc.management.commands.ConsoleCommand;
import de.depascaldc.management.main.ServerManager;

public class StartCommand extends ConsoleCommand {

	public StartCommand() {
		super("start");
		setUsage("/start");
		setDescription("Start a new instance of the Server.jar");
	}

	@Override
	public boolean executeCommand(String label, String[] args) {
		try {
			if(ServerManager.getManagedProcess().getProcess() != null) {
				ServerManager.getLogger().info("Server instance still running... Use the /mstop or /stop command to stop it.");
			} else {
				ServerManager.getLogger().info("Initializing new Serverprocess...");
				ServerManager.getManagedProcess().initProcess();
			}
		} catch (Exception e) {
		}
		return true;
	}

}
