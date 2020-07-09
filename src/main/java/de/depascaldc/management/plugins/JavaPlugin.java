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
import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.internal.guava.Preconditions;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import de.depascaldc.management.config.Config;
import de.depascaldc.management.main.ServerManager;
import de.depascaldc.management.util.Utils;

public class JavaPlugin implements Plugin {

	private PluginDescription description;

	private File dataFolder;
	private File configFile;
	private Config config;

	private PluginLogger logger;

	private JavaPluginLoader loader;

	boolean enabled;

	@Override
	public void initPluginInstance(JavaPluginLoader loader, PluginDescription description, File dataFolder) {
		if (description == null) {
			throw new PluginException("PluginDescription could not be resolved..");
		}
		if (description.getName() == null) {
			throw new PluginException("Plugin Name in PluginDescription cannot be null.");
		}
		this.loader = loader;
		this.description = description;
		this.logger = new PluginLogger(description.getName(), "");
		this.dataFolder = dataFolder;
		this.configFile = new File(this.dataFolder, "config.yml");
	}

	@Override
	public void onLoad() {
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public String getName() {
		return description.getName() != null ? description.getName() : "null";
	}

	@Override
	public String getAuthor() {
		return description.getAuthors() != null ? StringUtils.join(description.getAuthors()) : "null";
	}

	@Override
	public String getVersion() {
		return description.getVersion() != null ? description.getVersion() : "null";
	}

	@Override
	public String getDescriptionString() {
		return description.getDescription() != null ? description.getDescription() : "null";
	}
	
	@Override
    public InputStream getResource(String filename) {
        return this.getClass().getClassLoader().getResourceAsStream(filename);
    }

    @Override
    public boolean saveResource(String filename) {
        return saveResource(filename, false);
    }

    @Override
    public boolean saveResource(String filename, boolean replace) {
        return saveResource(filename, filename, replace);
    }

	public boolean saveResource(String filename, String outputName, boolean replace) {
		Preconditions.checkArgument(filename != null && outputName != null, "Filename can not be null!");
		Preconditions.checkArgument(filename.trim().length() != 0 && outputName.trim().length() != 0,
				"Filename can not be empty!");
		File out = new File(dataFolder, outputName);
		if (!out.exists() || replace) {
			try (InputStream resource = getResource(filename)) {
				if (resource != null) {
					File outFolder = out.getParentFile();
					if (!outFolder.exists()) {
						outFolder.mkdirs();
					}
					Utils.writeFile(out, resource);

					return true;
				}
			} catch (IOException e) {
				ServerManager.getLogger().error("IOException", e);
			}
		}
		return false;
	}
	
	@Override
    public Config getConfig() {
        if (this.config == null) {
            this.reloadConfig();
        }
        return this.config;
    }

    @Override
    public void saveConfig() {
        if (!this.getConfig().save()) {
            this.getLogger().error("Could not save config to " + this.configFile.toString());
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.saveResource("config.yml", false);
        }
    }


    @SuppressWarnings("unchecked")
	@Override
    public void reloadConfig() {
        this.config = new Config(this.configFile);
        InputStream configStream = this.getResource("config.yml");
        if (configStream != null) {
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(dumperOptions);
            try {
                this.config.setDefault(yaml.loadAs(Utils.readFile(this.configFile), LinkedHashMap.class));
            } catch (IOException e) {
            	ServerManager.getLogger().error("IOException", e);
            }
        }
    }
    
	@Override
	public PluginDescription getDescription() {
		return description;
	}

	@Override
	public File getDataFolder() {
		return dataFolder;
	}

	@Override
	public PluginLogger getLogger() {
		return logger;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public JavaPluginLoader getPluginLoader() {
		return loader;
	}

	public File getConfigFile() {
		return configFile;
	}

}
