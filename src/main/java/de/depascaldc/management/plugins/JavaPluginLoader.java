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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import de.depascaldc.management.util.Utils;

public class JavaPluginLoader {

	@SuppressWarnings("rawtypes")
	private final Map<String, Class> classes = new HashMap<>();
	private final Map<String, PluginClassLoader> classLoaders = new HashMap<>();
	@SuppressWarnings("unchecked")
	 public Plugin loadPlugin(File file) throws Exception {
        PluginDescription description = this.getPluginDescription(file);
        if (description != null) {
            File dataFolder = new File(file.getParentFile(), description.getName());
            if (dataFolder.exists() && !dataFolder.isDirectory()) {
                throw new IllegalStateException("Projected dataFolder '" + dataFolder.toString() + "' for " + description.getName() + " exists and is not a directory");
            }
            String className = description.getMain();
            PluginClassLoader classLoader = new PluginClassLoader(this, this.getClass().getClassLoader(), file);
            this.classLoaders.put(description.getName(), classLoader);
            JavaPlugin plugin;
            try {
                @SuppressWarnings("rawtypes")
				Class javaClass = classLoader.loadClass(className);
                if (!JavaPlugin.class.isAssignableFrom(javaClass)) {
                    throw new PluginException("Main class `" + description.getMain() + "' does not extend PluginBase");
                }
                try {
                    Class<JavaPlugin> pluginClass = (Class<JavaPlugin>) javaClass.asSubclass(JavaPlugin.class);
                    plugin = pluginClass.newInstance();
                    this.initPlugin(plugin, description, dataFolder);
                    return plugin;
                } catch (ClassCastException e) {
                    throw new PluginException("Error whilst initializing main class `" + description.getMain() + "'", e);
                } catch (InstantiationException | IllegalAccessException e) {
                }
            } catch (ClassNotFoundException e) {
                throw new PluginException("Couldn't load plugin " + description.getName() + ": main class not found");
            }
        }
        return null;
    }

	public Plugin loadPlugin(String filename) throws Exception {
		return this.loadPlugin(new File(filename));
	}

	public void enablePlugin(Plugin plugin) {
		if (plugin instanceof JavaPlugin && !plugin.isEnabled()) {
			((JavaPlugin) plugin).setEnabled(true);
			((JavaPlugin) plugin).onEnable();
		}
	}

	public PluginDescription getPluginDescription(File file) {
		try (JarFile jar = new JarFile(file)) {
			JarEntry entry = jar.getJarEntry("plugin.yml");
			if (entry == null) {
				return null;
			}
			try (InputStream stream = jar.getInputStream(entry)) {
				return new PluginDescription(Utils.readFile(stream));
			}
		} catch (IOException e) {
			return null;
		}
	}

	public PluginDescription getPluginDescription(String filename) {
		return this.getPluginDescription(new File(filename));
	}

	public Pattern[] getPluginFilters() {
		return new Pattern[] { Pattern.compile("^.+\\.jar$") };
	}

	private void initPlugin(JavaPlugin plugin, PluginDescription description, File dataFolder) {
		plugin.initPluginInstance(this, description, dataFolder);
		plugin.onLoad();
	}

	public void disablePlugin(Plugin plugin) {
		if (plugin instanceof JavaPlugin && plugin.isEnabled()) {
			((JavaPlugin) plugin).setEnabled(false);
		}
	}

	Class<?> getClassByName(final String name) {
		Class<?> cachedClass = classes.get(name);

		if (cachedClass != null) {
			return cachedClass;
		} else {
			for (PluginClassLoader loader : this.classLoaders.values()) {

				try {
					cachedClass = loader.findClass(name, false);
				} catch (ClassNotFoundException e) {
					// ignore
				}
				if (cachedClass != null) {
					return cachedClass;
				}
			}
		}
		return null;
	}

	void setClass(final String name, final Class<?> clazz) {
		if (!classes.containsKey(name)) {
			classes.put(name, clazz);
		}
	}

	@SuppressWarnings("unused")
	private void removeClass(String name) {
		Class<?> clazz = classes.remove(name);
	}
}
