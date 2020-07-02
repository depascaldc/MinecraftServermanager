package de.depascaldc.management.rest;

public class APIAction {
	
	public APIAction(Runnable run) {
		run.run();
	}
	
	public APIAction(Runnable run, Runnable whenDone) {
		run.run();
		whenDone(whenDone);
	}
	
	public void whenDone(Runnable whenDone) {
		whenDone.run();
	}

}
