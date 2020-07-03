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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

import de.depascaldc.management.logger.Logger;
import de.depascaldc.management.main.serverjars.ServerJars;

public class ServerInstaller {
	private static String mainPath;
	private Logger log;

	private File eulaProperties;

	private File serverJarProperties;

	private String jarName;

	private boolean setupDone = false;

	private int setupStep = 0;

	private String t;
	private String v;

	public ServerInstaller(String mp) {
		log = new Logger();
		run();
	}

	private void run() {
		this.eulaProperties = new File(mainPath, "eula.txt");
		this.serverJarProperties = new File(mainPath, "serverjars.properties");
		if (serverJarProperties.exists()) {
			initInstaller();
		} else {
			log.warn("serverjars.properties not found... Starting setup mode...");
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			log.info("STARTING ServerJars Setupmanager...");
			log.info("Choose one of the Following ServerTypes...");
			log.info("BEDROCK: nukkitx");
			log.info("PROXYS: bungeecord, waterfall");
			log.info("BUKKIT: bukkit, paper, spigot");
			log.info("VANILLA: snapshot, vanilla");
			List<String> availabletypes = new ArrayList<String>();
			availabletypes.add("nukkitx");
			availabletypes.add("bungeecord");
			availabletypes.add("waterfall");
			availabletypes.add("bukkit");
			availabletypes.add("paper");
			availabletypes.add("spigot");
			availabletypes.add("snapshot");
			availabletypes.add("vanilla");
			while (!setupDone) {
				if (scanner.hasNextLine()) {
					String line;
					if ((line = scanner.next()) != null) {
						String answerString = line.toLowerCase();
						if (line.isEmpty())
							continue;
						if (setupStep == 0) {
							if (!availabletypes.contains(answerString)) {
								log.info("Choose one of the Following ServerTypes...");
								log.info("BEDROCK: nukkitx");
								log.info("PROXYS: bungeecord, waterfall");
								log.info("BUKKIT: bukkit, paper, spigot");
								log.info("VANILLA: snapshot, vanilla");
							} else {
								this.t = answerString;
								setupStep = 1;
								log.info("Choose The ServerVersion... Format examples shown below.");
								log.info("[latest, 1.15.2, 1.15.1, 1.14.2, 1.13.2, 1.12.2, 1.8.0]");
							}
						} else if (setupStep == 1) {
							this.v = line;
							setupStep = 2;
							log.info("Your choice was: ServerType: " + this.t + " Verison: " + this.v);
							log.info("If you agree... Type Yes(y)/No(n)");
						} else if (setupStep == 2) {
							if (answerString.equalsIgnoreCase("yes") || answerString.equalsIgnoreCase("y")) {
								setupDone = true;
								log.info("Finished setup...");
								log.info("Starting ");
							} else if (answerString.equalsIgnoreCase("no") || answerString.equalsIgnoreCase("n")) {
								setupStep = 0;
								log.info("RE STARTING ServerJars Setupmanager...");
								log.info("Choose one of the Following ServerTypes...");
								log.info("BEDROCK: nukkitx");
								log.info("PROXYS: bungeecord, waterfall");
								log.info("BUKKIT: bukkit, paper, spigot");
								log.info("VANILLA: snapshot, vanilla");
							} else {
								log.info("Your choice was: ServerType: " + this.t + " Verison: " + this.v);
								log.info("If you agree... Type Yes(y)/No(n)");
							}
						}
					}
				}
			}
			try {
				saveServerJarsProps(v, t);
			} catch (IOException e) {
				e.printStackTrace();
			}
			initInstaller();
		}
	}

	private void initInstaller() {
		try {
			saveEula(true);
		} catch (IOException e) {
		}
		log.info("Starting ServerJars Installer/Updater...");
		String args[] = ServerJars.main(log).toString().split("/");
		jarName = args[args.length - 1];
	}

	private void saveEula(boolean accepted) throws IOException {
		Files.write(Paths.get(eulaProperties.getPath()), getEulaPropertiesString(accepted).getBytes());
	}

	private void saveServerJarsProps(String version, String type) throws IOException {
		Files.write(Paths.get(serverJarProperties.getPath()), getServerJarsPropertiesString(version, type).getBytes());
	}

	private String getServerJarsPropertiesString(String version, String type) throws IOException {
		InputStream inputStream = ServerManager.class.getResourceAsStream("/serverjars.properties");
		String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
		text = text.replaceFirst("%version%", version);
		text = text.replaceFirst("%type%", type);
		return text;
	}

	private String getEulaPropertiesString(boolean accepted) throws IOException {
		InputStream inputStream = ServerManager.class.getResourceAsStream("/eula.txt");
		String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
		text = text.replaceFirst("%boolean%", Boolean.toString(accepted));
		return text;
	}

	public static void runAsync(Runnable run) {
		new Thread(run).start();
	}

	public static String getMainPath() {
		return mainPath;
	}

	public Logger getLogger() {
		return log;
	}

	public String getJarName() {
		return jarName;
	}

}
