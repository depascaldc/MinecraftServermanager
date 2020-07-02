package de.depascaldc.management.commands.defaults;

import de.depascaldc.management.commands.ConsoleCommand;
import de.depascaldc.management.main.ServerManager;

public class GarbageCollectionCommand extends ConsoleCommand {

	public GarbageCollectionCommand() {
		super("garbagecollection");
		setUsage("/gc");
		setAliases("gc");
		setDescription("Runs JavaRuntime GarbageCollect Method... prints the RamUsage before and after...");
	}

	@Override
	public boolean executeCommand(String label, String[] args) {
		Runtime r = Runtime.getRuntime();
		ServerManager.getLogger().info("--------------------------- Process Informations ---------------------------");
		ServerManager.getLogger().info("Before GC Total memory: " + r.totalMemory());
		ServerManager.getLogger().info("Before GC Free memory: " + r.freeMemory());
		ServerManager.getLogger().info("Before GC Memory occupied: " + (r.totalMemory() - r.freeMemory()));
		r.gc();
		ServerManager.getLogger().info("======== RUNTIME.RUN GARBAGE COLLECTION (Object Recycling) ========");
		ServerManager.getLogger().info("AFTER GC Total memory: " + r.totalMemory());
		ServerManager.getLogger().info("AFTER GC Free memory: " + r.freeMemory());
		ServerManager.getLogger().info("AFTER GC Memory occupied: " + (r.totalMemory() - r.freeMemory()));
		ServerManager.getLogger().info("======== RUNTIME.RUN GARBAGE COLLECTION (Object Recycling) ========");
		ServerManager.getLogger().info("--------------------------- Process Informations ---------------------------");
		return false;
	}

}
