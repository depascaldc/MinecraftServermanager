package de.depascaldc.management.commands.defaults;

import java.util.ArrayList;
import java.util.List;

import de.depascaldc.management.commands.ConsoleCommand;
import de.depascaldc.management.console.ManagerConsole;
import de.depascaldc.management.logger.Logger;
import de.depascaldc.management.main.ServerManager;

public class MHelpCommand extends ConsoleCommand {

	public MHelpCommand() {
		super("mhelp");
		setDescription("Help Command (shows al available commands.)");
		setUsage("/mhelp [command]");
	}

	@Override
	public boolean executeCommand(String label, String[] args) {
		Logger l = ServerManager.getLogger();
		if (args.length == 1) {
			String command = args[0];
			ConsoleCommand cmd = ServerManager.getCommandMap().getCommands().get(command);
			if (cmd == null) {
				ServerManager.getLogger().error("Command does not exist. Try /help for a list of commands.");
				return true;
			}
			l.info("======== Command: " + cmd.getName() + " ========");
			l.info(">> Description:" + cmd.getDescription());
			l.info(">> Usage:" + cmd.getUsage());
			l.info(">> Aliases: " + cmd.getAliases().toString());
			l.info("===================================================");
			return true;
		}
		if (args.length == 0) {
			List<ConsoleCommand> cmds = new ArrayList<ConsoleCommand>();
			for (String cmd : ServerManager.getCommandMap().getCommands().keySet()) {
				if (!ServerManager.getCommandMap().getCommands().get(cmd).isAliasOfCommand(cmd))
					cmds.add(ServerManager.getCommandMap().getCommands().get(cmd));
			}
			l.info("<<< ---------------------------------- Command Help ---------------------------------- >>>");
			for (ConsoleCommand cmd : cmds) {
				l.info("======== Command: " + cmd.getName() + " ========");
				l.info(">> Description:" + cmd.getDescription());
				l.info(">> Usage:" + cmd.getUsage());
				l.info(">> Aliases: " + cmd.getAliases().toString());
				l.info("===================================================");
			}
			l.info("<<< ---------------------------------- Command Help ---------------------------------- >>>");
			return true;
		}
		ManagerConsole.runConsoleCommand("help help");
		return false;

	}

}
