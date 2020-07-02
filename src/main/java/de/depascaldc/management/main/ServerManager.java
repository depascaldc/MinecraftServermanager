package de.depascaldc.management.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import de.depascaldc.management.commands.CommandMap;
import de.depascaldc.management.commands.defaults.*;
import de.depascaldc.management.console.ManagerConsole;
import de.depascaldc.management.logger.Logger;
import de.depascaldc.management.process.ManagedProcess;
import de.depascaldc.management.rest.RESTApi;

public enum ServerManager {

	INSTANCE;

	private static Logger logger;
	static ManagerConsole console;

	private static String dataPath;

	private static CommandMap commandMap;

	private static File propertiesFile;
	private static Properties properties;

	private static ManagedProcess managedProcess;

	private static RESTApi restApi;

	private static PropertiesManager propertiesManager;

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

		runAsync(new Runnable() {
			@Override
			public void run() {
				restApi = new RESTApi();
				restApi.start();
			}
		});
		console = new ManagerConsole();
		runAsync(new Runnable() {
			@Override
			public void run() {
				managedProcess = new ManagedProcess();
			}
		});
		new Thread() {
			@Override
			public void run() {
				setName("ConsoleThread");
				try {
					ServerManager.console.start();
				} catch (Exception e) {
					getLogger().error("Terminal/ConsoleReader could not be initialized...", e);
					System.exit(0);
				}
			}
		}.run();
	}

	public static void runAsync(Runnable run) {
		new Thread(run).start();
	}

	private static void commandMapInit() {
		commandMap = new CommandMap();
		commandMap.register(new MHelpCommand());
		commandMap.register(new MStopCommand());
		commandMap.register(new GarbageCollectionCommand());
		commandMap.register(new APIAuthenticationCommand());
	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(200);
					getLogger().info("Shutting down running shutdown hook...");
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
					getLogger().info("Shutdownhook executed succesfully... Bye!");
					Thread.sleep(200);
					Thread.currentThread().interrupt();
				} catch (InterruptedException e) {
					getLogger().error("Shutting down ERROR... Shutdownhook failed...");
					Thread.currentThread().interrupt();
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
		return ManagerConsole.runConsoleCommand(command);
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

}
