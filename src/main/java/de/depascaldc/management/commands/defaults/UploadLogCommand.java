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

import java.io.IOException;
import java.util.List;

import de.depascaldc.management.commands.ConsoleCommand;
import de.depascaldc.management.logger.Logger;
import de.depascaldc.management.main.ServerManager;
import de.depascaldc.management.util.HastebinUtils;

public class UploadLogCommand extends ConsoleCommand {

	public UploadLogCommand() {
		super("logupload");
		setDescription("Get a Paste of a specified amount of lines out of the log. Default = 100 Max = 1000");
		setUsage("/lup [counter lines]");
		setAliases("lup");
	}

	@Override
	public boolean executeCommand(String label, String[] args) {
		List<String> log = Logger.CACHED_LOG;
		int count = 100;
		if (args.length > 0) {
			try {
				count = Integer.valueOf(args[0]);
			} catch (Exception e) {
				count = 100;
			}
			if (count < 1) {
				count = 100;
			}
		}
		if(count > 1000) {
			count = 1000;
		}
		if (count > log.size()) {
			count = log.size();
		}
		List<String> uploadList = log.subList(log.size()-count, log.size());
		
		StringBuilder sb = new StringBuilder();
		for (String line : uploadList) {
			sb.append(line);
			sb.append("\n");
		}
		String url;
		try {
			url = HastebinUtils.upload(sb.toString());
			ServerManager.getLogger().log("YOUR LOGGER PASTE YOU CAN FIND HERE: " + url + ".log");
		} catch (IOException e) {
			ServerManager.getLogger().log("Log couldnt be uploaded...");
		}
		return true;

	}

}
