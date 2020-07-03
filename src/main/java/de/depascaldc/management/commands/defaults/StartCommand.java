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
