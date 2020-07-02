package de.depascaldc.management.main.exceptions;

public class ServerException extends RuntimeException {

	private static final long serialVersionUID = 5862661773230888865L;

	public ServerException(String message) {
		super(message);
	}

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}
}
