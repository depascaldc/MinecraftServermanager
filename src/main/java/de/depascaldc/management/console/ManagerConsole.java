package de.depascaldc.management.console;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import de.depascaldc.management.commands.ConsoleCommand;
import de.depascaldc.management.logger.ConsoleColors;
import de.depascaldc.management.main.ServerManager;

public class ManagerConsole {

	private boolean running;
	private final BlockingQueue<String> consoleQueue = new LinkedBlockingQueue<>();
	private AtomicBoolean executingCommands = new AtomicBoolean(false);

	public ManagerConsole() {
	}

	public void start() {
		running = true;
		ServerManager.getLogger().info("Listening for Commands! Try /help for a list of commands.");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while (isRunning()) {
			if (scanner.hasNextLine()) {
				String line;
				if ((line = scanner.next()) != null) {
					runCommand(line);
				}
			}
		}
	}

	public static boolean runConsoleCommand(String cmd) {
		return runCommand(cmd);
	}

	private static boolean runCommand(String command) {
		ServerManager.getLogger().debug("Command ran: " + ConsoleColors.RED + command);
		String label = "";
		if (command.startsWith("/")) {
			command = command.substring(1);
		}
		String[] args = StringUtils.split(command);
		label = args[0];
		args = StringUtils.split(command.replaceFirst(label, ""));
		ConsoleCommand cmd = ServerManager.getCommandMap().getCommands().get(label);
		if (cmd == null) {
			try {
				ServerManager.getManagedProcess().sendCommand(command);
			} catch (Exception ex) {
				ex.printStackTrace();
				ServerManager.getLogger().error("Process dead... Command could not be sent...");
			}
			return true;
		}
		try {
			return cmd.executeCommand(label, args);
		} catch (Exception e) {
			ServerManager.getLogger().error("Error except in Command: " + cmd.getName(), e);
		}
		return false;
	}

	public boolean isExecutingCommands() {
		return executingCommands.get();
	}

	public String readLine() {
		try {
			return consoleQueue.take();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	protected void shutdown() {
	}

	public boolean isRunning() {
		return running;
	}

	public void setExecutingCommands(boolean executingCommands) {
		if (this.executingCommands.compareAndSet(!executingCommands, executingCommands) && executingCommands) {
			consoleQueue.clear();
		}
	}

}