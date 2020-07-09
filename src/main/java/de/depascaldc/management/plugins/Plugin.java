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
package de.depascaldc.management.plugins;

import java.io.File;
import java.io.InputStream;

import de.depascaldc.management.config.Config;

public interface Plugin {
	
	void initPluginInstance(JavaPluginLoader loader,PluginDescription description, File dataFolder);

	void onLoad();

	void onEnable();

	void onDisable();

	String getName();

	String getAuthor();

	String getVersion();
	
	String getDescriptionString();

	PluginDescription getDescription();
	
	void saveDefaultConfig();
	
	void reloadConfig();
	
	Config getConfig();
	
	File getDataFolder();
	
	boolean isEnabled();
	
	void setEnabled(boolean enabled);
	
	JavaPluginLoader getPluginLoader();

	void saveConfig();

	InputStream getResource(String filename);

	boolean saveResource(String filename);

	boolean saveResource(String filename, boolean replace);
	
	PluginLogger getLogger();

}
