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
