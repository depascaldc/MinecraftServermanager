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
package de.depascaldc.management;

import java.io.File;
import java.util.Properties;

import de.depascaldc.management.main.PropertiesManager;
import de.depascaldc.management.main.ServerInstaller;
import de.depascaldc.management.main.ServerManager;

public class Initializer {

	public static final String MAIN_PATH = System.getProperty("user.dir") + "/";
	public static boolean initialized;

	private static File propertiesFile;

	public static void main(String[] args) {
		propertiesFile = new File(MAIN_PATH, "servermanager.properties");
		if (!propertiesFile.exists()) {
			String service = initInstaller();
			initManager(service);
		} else {
			PropertiesManager propertiesManager = new PropertiesManager(propertiesFile);
			Properties properties = propertiesManager.getProperties();
			if(properties.getProperty("service") != null) {
				if(properties.getProperty("auto-update").equalsIgnoreCase("true")) {
					String service = initInstaller();
					initManager(service);
				}
				initManager();
			} else {
				if(properties.getProperty("auto-update").equalsIgnoreCase("true")) {
					initInstaller();
					initManager();
				} else {
					initManager();
				}
			}
		}

	}

	private static String initInstaller() {
		try {
			ServerInstaller installer = new ServerInstaller(MAIN_PATH);
			return installer.getJarName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static void initManager(String service) {
		try {
			ServerManager.initialize(MAIN_PATH, service);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initManager() {
		try {
			ServerManager.initialize(MAIN_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
