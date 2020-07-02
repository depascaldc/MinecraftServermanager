package de.depascaldc.management.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandMap {

	private Map<String, ConsoleCommand> commands;

	public CommandMap() {
		commands = new HashMap<String, ConsoleCommand>();
	}

	public void register(ConsoleCommand command) {
		commands.put(command.getName(), command);
		if (command.getAliases().size() > 0) {
			for (String alias : command.getAliases()) {
				commands.put(alias, command);
			}
		}
	}

	public void unregister(String command) {
		try {
			commands.remove(command);
		} catch (Exception e) {
		}
	}

	public Map<String, ConsoleCommand> getCommands() {
		return commands;
	}

}
