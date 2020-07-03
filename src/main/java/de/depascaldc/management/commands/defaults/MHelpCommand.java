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

import java.util.ArrayList;
import java.util.List;

import de.depascaldc.management.commands.ConsoleCommand;
import de.depascaldc.management.console.JLineTerminalCLI;
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
		JLineTerminalCLI.runCommand("help help");
		return false;

	}

}
