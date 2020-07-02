package de.depascaldc.management.commands;

import java.util.ArrayList;
import java.util.List;

public abstract class ConsoleCommand {
	
	private String name;
	private String description;
	
	private String usage;
	
	private List<String> aliases;
	
	public ConsoleCommand(String name) {
		this.name = name;
		setDescription("Command " + name);
		setUsage("/" + name);
		setAliases(new ArrayList<String>());
	}
	
	public abstract boolean executeCommand(String label, String[] args);
	
	public String getName() {
		return name;
	}
	
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
	
	public void setAliases(String ... aliases) {
		List<String> als = new ArrayList<String>();
		for(String a : aliases)
			als.add(a);
		this.aliases = als;
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setUsage(String usage) {
		this.usage = usage;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public boolean isAliasOfCommand(String alias) {
		return aliases.contains(alias);
	}

}
