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
package de.depascaldc.management.commands;

import java.util.ArrayList;
import java.util.List;

public abstract class ConsoleCommand {
	
	private String name;
	private String description;
	
	private String usage;
	
	private List<String> aliases;
	
	public ConsoleCommand(String name) {
		this.name = name;
		setDescription("Command " + name);
		setUsage("/" + name);
		setAliases(new ArrayList<String>());
	}
	
	public abstract boolean executeCommand(String label, String[] args);
	
	public String getName() {
		return name;
	}
	
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
	
	public void setAliases(String ... aliases) {
		List<String> als = new ArrayList<String>();
		for(String a : aliases)
			als.add(a);
		this.aliases = als;
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setUsage(String usage) {
		this.usage = usage;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public boolean isAliasOfCommand(String alias) {
		return aliases.contains(alias);
	}

}
