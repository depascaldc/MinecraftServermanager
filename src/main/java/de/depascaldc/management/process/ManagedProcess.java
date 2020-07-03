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
package de.depascaldc.management.process;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.IOUtils;

import de.depascaldc.management.logger.ConsoleColors;
import de.depascaldc.management.main.ServerManager;

public class ManagedProcess {

	private Process process;

	private String initmem;
	private String maxmem;

	private String jarPath;

	private OutputListener outListener;

	private Timer t;

	public ManagedProcess() {
		this.jarPath = ServerManager.getProperties().getProperty("service");
		this.initmem = ServerManager.getProperties().getProperty("initmem");
		this.maxmem = ServerManager.getProperties().getProperty("maxmem");
		try {
			this.initProcess();
		} catch (Exception e) {
			ServerManager.getLogger().error("Server process could not be initialized... ", e);
			e.printStackTrace();
		}
		ServerManager.runAsync(new Runnable() {
			@Override
			public void run() {
				t = new Timer();
				t.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						if (process != null) {
							try {
								if(process.isAlive()) {
									@SuppressWarnings("unused")
									int exitCode = process.exitValue();
									if (outListener != null) {
										if (outListener.isAlive()) {
											outListener.interrupt();
											outListener = null;
										}
									}
								} else {
									process = null;
								}
							} catch (Exception e) {
							}
						}
					}
				}, 1000, 500);
			}
		});
	}

	public synchronized void initProcess() throws IOException {
		if (this.process == null || !this.process.isAlive()) {
			ArrayList<String> startCmd = new ArrayList<String>();
			startCmd.add("java");
			startCmd.add("-server");
			startCmd.add("-Xms" + this.initmem);
			startCmd.add("-Xmx" + this.maxmem);
			startCmd.add("-XX:+ExitOnOutOfMemoryError");
			startCmd.add("-jar");
			startCmd.add(this.jarPath);
			startCmd.add("nogui");
			ServerManager.getLogger().info("Starting ServerProcess... InitMem: " + ConsoleColors.RED + initmem
					+ ConsoleColors.GREEN + " MaxMem: " + ConsoleColors.RED + maxmem);
			this.process = new ProcessBuilder(new String[0]).command(startCmd)
					.directory(new File(ServerManager.getDataPath())).start();
			ServerManager.getLogger().info("ServerProcess initialized " + process.isAlive());
			ServerManager.getLogger().info("----------------------------------------------------------------");
			outListener = new OutputListener(this, this.process).start();
		} else {
			ServerManager.getLogger().warn("ServerProcess is already initialized...");
		}
	}

	public synchronized void stopProcess() {
		if (this.process != null) {
			sendCommand("serverstop");
			process = null;
		} else {
			System.exit(0);
		}
	}

	public synchronized void killProcess() {
		if (this.process != null) {
			process.destroy();
			process = null;
		} else {
			System.exit(0);
		}
	}

	public synchronized void sendCommand(String command) {
		if (this.process != null || !this.process.isAlive()) {
			if (command.equalsIgnoreCase("serverstop")) {
				if (this.process != null && this.process.isAlive()) {
					this.sendCommand("stop");
				} else {
					ServerManager.getLogger().info("No active Serverprocess... ");
				}
				return;
			}
			// finally send commands to serverconsole
			try {
				sendCommandToProcess(command, process);
			} catch (Exception ex) {
				this.process = null;
				ServerManager.getLogger()
						.warn("Command not executed... No Server instance running... try /start to restart...");
			}
		} else {
			ServerManager.getLogger().warn("Server process is NOT running...");
		}
	}

	private boolean sendCommandToProcess(String cmd, Process proc) {
		try {
			OutputStreamWriter writer = new OutputStreamWriter(proc.getOutputStream());
			writer.write(cmd);
			writer.write("\n");
			writer.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			ServerManager.getLogger().warn("Command not executed...");
			return false;
		}
	}

	public static void closeProcessConplete(Process p) throws IOException {
		try {
			closeStreams(p);
		} finally {
			if (p != null) {
				p.destroy();
			}
		}
	}

	public static void closeStreams(Process p) throws IOException {
		if (p != null) {
			IOUtils.close(p.getInputStream());
			IOUtils.close(p.getOutputStream());
			IOUtils.close(p.getErrorStream());
		}
	}

	public Process getProcess() {
		return process;
	}

}
