package de.depascaldc.management.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

class LogUtil {
	static String translate(LogType lt, String message) {
		Date date = new Date();
		switch (lt) {
		case DEBUG: {
			message = String.valueOf(ConsoleColors.CYAN_BRIGHT) + "[DEBUG | " + formatDateTime(date) + "] " + message;
			break;
		}
		case WARN: {
			message = String.valueOf(ConsoleColors.YELLOW_BOLD_BRIGHT) + "[WARN | " + formatDateTime(date) + "] "
					+ message;
			break;
		}
		case INFO:
		case LOG: {
			message = String.valueOf(ConsoleColors.GREEN) + "[LOG/INFO | " + formatDateTime(date) + "] " + message;
			break;
		}
		case ERROR: {
			message = String.valueOf(ConsoleColors.RED_BOLD) + "[ERROR | " + formatDateTime(date) + "] " + message;
			break;
		}
		}
		return message + String.valueOf(ConsoleColors.RESET);
	}

	static String translate(LogType lt, String message, Exception exception) {
		return translate(lt, message) + " \r\n" + String.valueOf(ConsoleColors.RED_BRIGHT) + exception.getMessage()
				+ " \r\n" + exception.getStackTrace() + String.valueOf(ConsoleColors.RESET);
	}

	static String formatDateTime(Date d) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY / HH:mm");
		return format.format(d);
	}

	static String transLateStackElement(StackTraceElement el) {
		return "Class: " + el.getClassName() + " Method: " + el.getMethodName() + " Line: " + el.getLineNumber();
	}

	enum LogType {
		INFO, DEBUG, WARN, LOG, ERROR;
	}

}
