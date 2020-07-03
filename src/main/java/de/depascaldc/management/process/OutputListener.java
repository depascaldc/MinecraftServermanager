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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import de.depascaldc.management.main.ServerManager;

public class OutputListener {

	private Process process;
	private BufferedReader stdin;
	private ManagedProcess managedProcess;
	
	private boolean alive;

	public OutputListener(ManagedProcess managedProcess, Process process) {
		this.managedProcess = managedProcess;
		this.process = process;
		alive = true;
		this.stdin = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
		ServerManager.getLogger().info("Output Listener started...");
		ServerManager.getLogger().info("----------------------------------------------------------------");
	}

	public OutputListener start() {
		ServerManager.runAsync(new Runnable() {
			@Override
			public void run() {
				while (isAlive()) {
					try {
						if (stdin.ready()) {
							String msg = stdin.readLine();
							if (msg != null) {
								ServerManager.getLogger().out(msg);
								Thread.sleep(40L);
							} else
								Thread.sleep(100L);
						}
					} catch (Exception e) {
					}
				}
			}
		});
		return this;
	}

	public ManagedProcess getManagedProcess() {
		return managedProcess;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void interrupt() {
		alive = false;
	}

}
