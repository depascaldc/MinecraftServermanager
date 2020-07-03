package de.depascaldc.management.commands.defaults;

import de.depascaldc.management.commands.ConsoleCommand;
import de.depascaldc.management.main.ServerManager;

public class MStopCommand extends ConsoleCommand {

	public MStopCommand() {
		super("mstop");
		setUsage("/mstop /exit /kill");
		setAliases("exit", "kill");
		setDescription("Stops the ManagerClient save...");
	}

	@Override
	public boolean executeCommand(String label, String[] args) {
		try {
			if (label.equalsIgnoreCase("mstop")) {
				ServerManager.getManagedProcess().stopProcess();
			} else {
				System.exit(0);
			}
		} catch (Exception e) {
		}
		return true;
	}

}
