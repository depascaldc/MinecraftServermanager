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
