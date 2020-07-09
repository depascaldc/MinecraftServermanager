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
package de.depascaldc.management.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import de.depascaldc.management.commands.CommandMap;
import de.depascaldc.management.commands.defaults.*;
import de.depascaldc.management.console.JLineTerminalCLI;
import de.depascaldc.management.console.rc.WebsocketServer;
import de.depascaldc.management.logger.Logger;
import de.depascaldc.management.plugins.Plugin;
import de.depascaldc.management.plugins.PluginManager;
import de.depascaldc.management.process.ManagedProcess;
import de.depascaldc.management.rest.RESTApi;

public enum ServerManager {

	INSTANCE;

	private static Logger logger;
	static JLineTerminalCLI console;

	private static String dataPath;

	private static CommandMap commandMap;

	private static File propertiesFile;
	private static Properties properties;

	private static ManagedProcess managedProcess;

	private static RESTApi restApi;
	private static WebsocketServer websocketServer;

	private static PropertiesManager propertiesManager;

	private static PluginManager pluginManager;

	public static void initialize(String mainPath, String service) throws IOException {
		propertiesFile = new File(mainPath, "servermanager.properties");
		propertiesManager = new PropertiesManager(propertiesFile);
		properties = propertiesManager.getProperties();
		if (service != null) {
			properties.setProperty("service", service);
		}
		initialize(mainPath);
	}

	public static void initialize(String mainPath) throws IOException {
		if (propertiesFile == null) {
			propertiesFile = new File(mainPath, "servermanager.properties");
			propertiesManager = new PropertiesManager(propertiesFile);
			properties = propertiesManager.getProperties();
		}
		logger = new Logger(mainPath);
		dataPath = mainPath;
		getLogger().info("============================== PROPERTIES ==============================");
		properties.forEach((o, o2) -> {
			String key = (String) o;
			String value = (String) o2;
			getLogger().info(key + " = " + value);
		});
		getLogger().info("============================== PROPERTIES ==============================");
		getLogger().info("servermanager.properties succesfully loaded...");
		getLogger().info("============================== PROPERTIES ==============================");
		File shFile = new File(mainPath, "start.sh");
		if (!shFile.exists()) {
			getLogger().info("saving example start.sh bash script");
			saveFile(shFile, ServerManager.class.getResourceAsStream("/start.sh"));
		}

		addShutdownHook();
		commandMapInit();
		pluginManager = new PluginManager(mainPath + "managerplugins/");
		Map<String, Plugin> plsMap = pluginManager.loadPlugins(mainPath + "managerplugins/");
		
		if (getProperties().getProperty("rcon-enabled").equalsIgnoreCase("true")) {
			websocketServer = new WebsocketServer();
			websocketServer.runServer();
		}

		runAsync(new Runnable() {
			@Override
			public void run() {
				restApi = new RESTApi();
				restApi.start();
			}
		});
		
		runAsync(new Runnable() {
			@Override
			public void run() {
				managedProcess = new ManagedProcess();
			}
		});
		
		for (Plugin plugin : plsMap.values()) {
			try {
				ServerManager.getLogger().info("Enabling Plugin " + plugin.getDescription().getFullName());
				if (!plugin.isEnabled()) {
					enablePlugin(plugin);
					ServerManager.getLogger().info("Enabled Plugin " + plugin.getDescription().getFullName());
				} else {
					ServerManager.getLogger().info("Plugin " + plugin.getDescription().getFullName() + " is already enabled.");
				}
			} catch (Exception e) {
				ServerManager.getLogger().error("Enabling Plugin " + plugin.getName() + " failed..");
			}
		}
		
		new Thread() {
			@Override
			public void run() {
				setName("ConsoleThread");
				try {
					console = new JLineTerminalCLI(logger);
				} catch (Exception e) {
					getLogger().error("Terminal/ConsoleReader could not be initialized...", e);
					System.exit(0);
				}
			}
		}.run();
		
	}
	
	public static void enablePlugin(Plugin plugin) {
        pluginManager.enablePlugin(plugin);
    }
	
	public static void disablePlugins() {
        pluginManager.disablePlugins();
    }

	public static void runAsync(Runnable run) {
		new Thread(run).start();
	}

	private static void commandMapInit() {
		commandMap = new CommandMap();
		commandMap.register(new MHelpCommand());
		commandMap.register(new MStopCommand());
		commandMap.register(new StartCommand());
		commandMap.register(new GarbageCollectionCommand());
		commandMap.register(new APIAuthenticationCommand());
		commandMap.register(new UploadLogCommand());
	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(200);
					getLogger().info("Shutting down running shutdown hook...");
					pluginManager.disablePlugins();
			        pluginManager.clearPlugins();
					Thread.sleep(200);
					if (managedProcess != null) {
						if (managedProcess.getProcess() != null) {
							managedProcess.killProcess();
							try {
								while (managedProcess.getProcess().isAlive()) {
									Thread.sleep(500);
								}
							} catch (Exception e) {
							}
						}
					}
					getLogger().info("Closing Webserver API...");
					try {
						restApi.getServer().shutdownNow();
						getLogger().info("Webserver API closed...");
					} catch (Exception e) {
					}
					getLogger().info("Closing Websocketserver...");
					try {
						websocketServer.stopServer();
						getLogger().info("Websocketserver closed...");
					} catch (Exception e) {
					}
					getLogger().info("Closing Console / Terminal...");
					console.close();
					getLogger().info("Shutdownhook executed succesfully... Bye!");
				} catch (Exception e) {
				}
			}
		}));
	}

	public static String getDataPath() {
		return dataPath;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static CommandMap getCommandMap() {
		return commandMap;
	}

	public static void saveFile(File file, InputStream input) throws IOException {
		String stringFromInputStream = IOUtils.toString(input, "UTF-8");
		Files.write(Paths.get(file.getPath()), stringFromInputStream.getBytes());
	}

	public static boolean dispatchCommand(String command) {
		return JLineTerminalCLI.runCommand(command);
	}

	public static Properties getProperties() {
		return properties;
	}

	public static ManagedProcess getManagedProcess() {
		return managedProcess;
	}

	public static RESTApi getRestApi() {
		return restApi;
	}

	public static PluginManager getPluginManager() {
		return pluginManager;
	}

}
