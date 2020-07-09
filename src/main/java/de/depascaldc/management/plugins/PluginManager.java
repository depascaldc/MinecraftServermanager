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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

import de.depascaldc.management.main.ServerManager;

public class PluginManager {

	private String pluginPath;
	private File pluginFolder;

	private Map<String, Plugin> plugins = new LinkedHashMap<>();
	private Map<String, JavaPluginLoader> fileAssociations = new HashMap<>();

	public PluginManager(String pluginsPath) {
		this.pluginPath = pluginsPath;
		this.pluginFolder = new File(pluginsPath);
		if (!pluginFolder.exists())
			pluginFolder.mkdirs();
	}

	public Plugin getPlugin(String name) {
		if (this.plugins.containsKey(name)) {
			return this.plugins.get(name);
		}
		return null;
	}

	public Map<String, Plugin> getPlugins() {
		return plugins;
	}

	public Plugin loadPlugin(File file) {
		JavaPluginLoader loader = new JavaPluginLoader();
		PluginDescription description = loader.getPluginDescription(file);
		if (description != null) {
			ServerManager.getLogger().info("Load plugin: " + description.getName());
			try {
				return loader.loadPlugin(file);
			} catch (Exception e) {
				ServerManager.getLogger().error(
						"Could not load plugin " + file.getName() + " in " + file.getParentFile().toPath().toString());
				e.printStackTrace();
				return null;
			}
		} else {
			ServerManager.getLogger().error("Could not load plugin " + file.getName() + " no plugin.yml found.");
		}
		return null;
	}

	public Map<String, Plugin> loadPlugins(String dictionary) {
		return this.loadPlugins(new File(dictionary));
	}

	public Map<String, Plugin> loadPlugins(File dictionary) {
		if (dictionary.isDirectory()) {
			Map<String, Plugin> loadedPlugins = new LinkedHashMap<>();
			for (File f : dictionary.listFiles()) {
				if (!f.isDirectory() && f.getName().endsWith(".jar")) {
					JavaPluginLoader loader = new JavaPluginLoader();
					PluginDescription desc = loader.getPluginDescription(f);
					try {
						Plugin plugin = loadPlugin(f);
						loadedPlugins.put(desc.getName(), plugin);
					} catch (Exception e) {
					}
				}
			}
			return loadedPlugins;
		} else {
			return new HashMap<>();
		}
	}

	public void enablePlugin(Plugin plugin) {
		if (!plugin.isEnabled()) {
			try {
				plugin.getPluginLoader().enablePlugin(plugin);
				plugins.put(plugin.getDescription().getName(), plugin);
			} catch (Throwable e) {
				ServerManager.getLogger().error("error: ", new RuntimeException(e));
				this.disablePlugin(plugin);
			}
		}
	}

	public void disablePlugins() {
		ListIterator<Plugin> plugins = new ArrayList<>(this.getPlugins().values())
				.listIterator(this.getPlugins().size());
		while (plugins.hasPrevious()) {
			this.disablePlugin(plugins.previous());
		}
	}

	public void disablePlugin(Plugin plugin) {
		if (plugin.isEnabled()) {
			try {
				plugin.getPluginLoader().disablePlugin(plugin);
				plugin.onDisable();
			} catch (Exception e) {
				ServerManager.getLogger().error("error: ", e);
			}
		}
	}

	public void clearPlugins() {
		this.disablePlugins();
		this.plugins.clear();
		this.fileAssociations.clear();
	}

	public File getPluginFolder() {
		return pluginFolder;
	}

	public String getPluginPath() {
		return pluginPath;
	}

}
